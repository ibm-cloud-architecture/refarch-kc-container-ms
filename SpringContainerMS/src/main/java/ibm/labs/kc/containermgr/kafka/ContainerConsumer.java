package ibm.labs.kc.containermgr.kafka;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import ibm.labs.kc.containermgr.ContainerService;
import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.dao.OrderDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.model.ContainerStatus;
import ibm.labs.kc.model.events.ContainerCreationEvent;
import ibm.labs.kc.model.events.ContainerAnomalyEvent;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.model.events.ContainerOffMaintenanceEvent;
import ibm.labs.kc.model.events.ContainerOnMaintenanceEvent;
/*
 * Consume events from 'containers' topic. Started when the spring application context
 * is initialized.
 */
@Component
public class ContainerConsumer {
	private static final Logger LOG = Logger.getLogger(ContainerConsumer.class.toString());
	@Value("${kafka.containers.consumer.groupid}")
	public String CONSUMER_GROUPID;
	@Value("${kcsolution.containers.topic}")
  	public String CONTAINERS_TOPIC;
	private Gson parser = new Gson();

	@Value("${kcsolution.container_anomaly_threshold:3}")
	private int container_anomaly_threshold;
	@Value("${kcsolution.bpm_anomaly_service}")
	private String bpm_anomaly_service;

	private HashMap<String, List<ContainerAnomalyEvent>> maintenance = new HashMap<String, List<ContainerAnomalyEvent>>();

	@Autowired
	private ContainerDAO containerDAO;

	@Autowired
	private CityDAO cityDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ContainerService containerService;

	@Autowired
	private BpmAgent bpmAgent;

	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ContainerProperties containerProps = new ContainerProperties(CONTAINERS_TOPIC);
		LOG.info(" Topic:" + CONTAINERS_TOPIC + " " + CONSUMER_GROUPID);
		System.setProperty("bpm_anomaly_service_enabled","true");
		LOG.info("BPM service enabled: " + System.getProperty("bpm_anomaly_service_enabled"));
		LOG.info("BPM service for container anomaly events at: " + bpm_anomaly_service);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		        	if (message.value().contains(ContainerEvent.CONTAINER_ADDED)) {
						// --------------------------------------------
						// ContainerAdded events
						//---------------------------------------------
						LOG.info("Received create new container event: " + message.value());
						ContainerCreationEvent cce = parser.fromJson(message.value(), ContainerCreationEvent.class);
						if (!containerDAO.existsById(cce.getContainerID())) {
							ContainerEntity ce = new ContainerEntity(cce.getPayload());
							ce.setCurrentCity(cityDAO.getCityName(ce.getLatitude(),ce.getLongitude()));
							containerDAO.save(ce);
						} else {
							LOG.severe("[ERROR] - There is already an existing container with id " + cce.getContainerID() + ". A new container will not be created");
						}
					}
					else if (message.value().contains(ContainerEvent.CONTAINER_ANOMALY)) {
						// --------------------------------------------
						// ContainerAnomaly events
						//---------------------------------------------
						LOG.info("Received new container anomaly event: " + message.value());
						// Get the ContainerAnomalyEvent objet from the event received
						ContainerAnomalyEvent cae = parser.fromJson(message.value(), ContainerAnomalyEvent.class);
						if (containerDAO.existsById(cae.getContainerID())){
							// If the container is not in maintenance mode yet
							if (containerDAO.getById(cae.getContainerID()).getStatus() != ContainerStatus.MaintenanceNeeded &&
								containerDAO.getById(cae.getContainerID()).getStatus() != ContainerStatus.InMaintenance){
								// Check if we have already received an anomaly event for this containerID
								if (maintenance.containsKey(cae.getContainerID())){
									// Add this event to the list of anomaly events already received for this containerID
									if (maintenance.get(cae.getContainerID()) instanceof List<?>){
										List<ContainerAnomalyEvent> anomaly_list = maintenance.get(cae.getContainerID());
										anomaly_list.add(cae);
										maintenance.put(cae.getContainerID(), anomaly_list);
									}
									else {
										// An error occurred when getting the list of anomaly events for this containerID
										LOG.severe("[ERROR] - There was a problem retrieving the ContainerAnomaly events for ContainerID " + cae.getContainerID());
									}
									// Check if we have received 3 anomaly events for this containerID
									if (maintenance.get(cae.getContainerID()).size() == container_anomaly_threshold){
										// Call BPM process. First we set the token expiration to false
										bpmAgent.callBPM(cae,false);
										// Set container to MaintenanceNeeded state
										ContainerEntity ce = containerDAO.getById(cae.getContainerID());
										ce.setStatus(ContainerStatus.MaintenanceNeeded);
										containerDAO.update(ce.getId(), ce);
										// Spoil Orders
										if (orderDAO.getOrders(cae.getContainerID()) != null) containerService.spoilOrders(cae.getContainerID(), orderDAO.getOrders(cae.getContainerID()));
										else LOG.info("There is no order which container " + cae.getContainerID() + " is allocated for. Therefore, there is no need to send any oderSpoilt event.");
										// Remove container from the list
										maintenance.remove(cae.getContainerID());
									}
								}
								// This is the first anomaly event received for this containerID
								else {
									List<ContainerAnomalyEvent> new_list = new ArrayList<ContainerAnomalyEvent>();
									new_list.add(cae);
									maintenance.put(cae.getContainerID(), new_list);
								}
							}
							else {
								LOG.info("Received Container Anomaly event for ContainerID " + cae.getContainerID() + ". The container is already set for maintenance or in maintenance.");
							}
						} else {
							LOG.severe("[ERROR] - Received ContainerAnomalyEvent event for a container which does not exist. ContainerId received: " + cae.getContainerID());
						}
					}
					else if (message.value().contains(ContainerEvent.CONTAINER_ON_MAINTENANCE)) {
						// --------------------------------------------
						// ContainerOnMaintenance event
						//---------------------------------------------
						LOG.info("Received new ContainerOnMaintenance event: " + message.value());
						// Get the ContainerOnMaintenanceEvent objet from the event received
						ContainerOnMaintenanceEvent conm = parser.fromJson(message.value(), ContainerOnMaintenanceEvent.class);
						ContainerEntity ce = new ContainerEntity(conm.getPayload());
						ce.setStatus(ContainerStatus.InMaintenance);
						// Set the capacity to 0 just in case and to reflect the container won't be able to take anything in.
						ce.setCapacity(0);
						// We update the city in case container GPS location has not been updated (this could be the anomaly)
						ce.setCurrentCity(cityDAO.getCityName(ce.getLatitude(),ce.getLongitude()));
						// Supposedly, we don't need to check whether the container exists or not
						// since this was checked before sending the ContainerOnMaintenance event for this container
						containerDAO.update(ce.getId(), ce);
						LOG.info("Container " + conm.getContainerID() + " set to in maintenance.");
					}
					else if (message.value().contains(ContainerEvent.CONTAINER_OFF_MAINTENANCE)) {
						// --------------------------------------------
						// ContainerOffMaintenance event
						//---------------------------------------------
						LOG.info("Received new ContainerOffMaintenance event: " + message.value());
						// Get the ContainerAnomalyEvent objet from the event received
						ContainerOffMaintenanceEvent coffm = parser.fromJson(message.value(), ContainerOffMaintenanceEvent.class);
						if (coffm.getPayload().getCapacity()==0) LOG.info("[WARNING] - You are setting the capacity for the container " + coffm.getContainerID() + " to 0. It won't be able to ship any order.");
						ContainerEntity ce = new ContainerEntity(coffm.getPayload());
						ce.setStatus(ContainerStatus.Empty);
						ce.setCurrentCity(cityDAO.getCityName(ce.getLatitude(),ce.getLongitude()));
						// Supposedly, we don't need to check whether the container exists or not
						// since this was checked before sending the ContainerOffMaintenance event for this container
						containerDAO.update(ce.getId(), ce);
						// Remove the orders associated to this container
						orderDAO.removeOrders(coffm.getContainerID());
						LOG.info("Container " + coffm.getContainerID() + " set out of in maintenance.");
					}
					else if (message.value().contains(ContainerEvent.CONTAINER_ORDER_ASSIGNED)) {
						// --------------------------------------------
						// ContainerAssignedToOrder event
						//---------------------------------------------

						// Do nothing - This event is expected but no action is to be taken.
					}
					else {
						// --------------------------------------------
						// Others
						//---------------------------------------------
						LOG.warning("[WARNING] -  Received unexpected event: " + message.value());
					}
		        }
			});
			
		KafkaMessageListenerContainer<Integer, String> kafkaEventListener = createSpringKafkaListener(containerProps);
		kafkaEventListener.setBeanName(CONSUMER_GROUPID);
		kafkaEventListener.start();
	}

	private KafkaMessageListenerContainer<Integer, String> createSpringKafkaListener(ContainerProperties containerProps) {
		Map<String, Object> props = KCKafkaConfiguration.getConsumerProperties(CONSUMER_GROUPID);
		DefaultKafkaConsumerFactory<Integer, String> cf = new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container = new KafkaMessageListenerContainer<>(cf, containerProps);
		return container;
	}
}
