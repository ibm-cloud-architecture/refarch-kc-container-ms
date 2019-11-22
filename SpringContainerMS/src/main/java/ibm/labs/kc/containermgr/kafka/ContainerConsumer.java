package ibm.labs.kc.containermgr.kafka;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
// import org.springframework.retry.annotation.Recover;
// import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.model.events.ContainerCreationEvent;
import ibm.labs.kc.model.events.ContainerAnomalyEvent;
import ibm.labs.kc.model.events.ContainerEvent;
/*
 * Consume events from 'containers' topic. Started when the spring application context
 * is initialized.
 */
@Component
public class ContainerConsumer {
	private static final Logger LOG = Logger.getLogger(ContainerConsumer.class.toString());
	@Value("${kafka.containers.consumer.groupid}")
	public String CONSUMER_GROUPID;
	@Value("${kcsolution.containers}")
  	public String CONTAINERS_TOPIC;
	private Gson parser = new Gson();

	@Value("${kcsolution.bpm_anomaly}")
	String bpm_anomaly_url;
	private HashMap<String, List<ContainerAnomalyEvent>> maintenance = new HashMap<String, List<ContainerAnomalyEvent>>();

	@Autowired
	private ContainerDAO containerDAO;

	@Autowired
	private CityDAO cityDAO;

	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ContainerProperties containerProps = new ContainerProperties(CONTAINERS_TOPIC);
		LOG.info(" Topic:" + CONTAINERS_TOPIC + " " + CONSUMER_GROUPID);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		        	if (message.value().contains(ContainerEvent.CONTAINER_ADDED)) {
						// --------------------------------------------
						// Create container events
						//---------------------------------------------
						LOG.info("Received create new container event: " + message.value());
		        		ContainerCreationEvent ce = parser.fromJson(message.value(), ContainerCreationEvent.class);
		        		ContainerEntity cce = new ContainerEntity(ce.getPayload());
		        		cce.setCurrentCity(cityDAO.getCityName(cce.getLatitude(),cce.getLongitude()));
		        		containerDAO.save(cce);
					}
					else if (message.value().contains(ContainerEvent.CONTAINER_ANOMALY)) {
						// --------------------------------------------
						// Container anomaly events
						//---------------------------------------------
						LOG.info("Received new container anomaly event: " + message.value());
						// Get the ContainerAnomalyEvent objet from the event received
						ContainerAnomalyEvent ce = parser.fromJson(message.value(), ContainerAnomalyEvent.class);
						// Check if we have already received an anomaly event for this containerID
						if (maintenance.containsKey(ce.getContainerID())){
							// Add this event to the list of anomaly events already received for this containerID
							if (maintenance.get(ce.getContainerID()) instanceof List<?>){
								List<ContainerAnomalyEvent> anomaly_list = maintenance.get(ce.getContainerID());
								anomaly_list.add(ce);
								maintenance.put(ce.getContainerID(), anomaly_list);
							}
							else {
								// An error occurred when getting the list of anomaly events for this containerID
								LOG.info("[ERROR] - There was a problem retrieving the ContainerAnomaly events for ContainerID " + ce.getContainerID());
							}
							// Check if we have received 10 anomaly events for this containerID
							if (maintenance.get(ce.getContainerID()).size() == 3){
								// Send request to BPM
								LOG.info("BPM Call");
								sendBPM();
								maintenance.remove(ce.getContainerID());
							}
						}
						// This is the first anomaly event received for this containerID
						else {
							List<ContainerAnomalyEvent> new_list = new ArrayList<ContainerAnomalyEvent>();
							new_list.add(ce);
							maintenance.put(ce.getContainerID(), new_list);
						}
					}
					else {
						// --------------------------------------------
						// Others
						//---------------------------------------------
						LOG.info("[WARNING] -  Received unexpected event: " + message.value());
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

	// @Retryable(maxAttempts=3,value=Exception.class)
	private void sendBPM() {
		// create headers
		HttpHeaders headers = new HttpHeaders();
		// set `content-type` header
		headers.setContentType(MediaType.APPLICATION_JSON);
		// set `accept` header
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// create an instance of RestTemplate
		RestTemplate restTemplate = new RestTemplate();

		// create a map for post parameters
		Map<String, Object> map = new HashMap<>();
		map.put("test1", "test1");
		map.put("test2", "test2");

		// build the request
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

		try {
			// send POST request
			ResponseEntity<String> response = restTemplate.postForEntity(bpm_anomaly_url, entity, String.class);
			//check response status code
			if (response.getStatusCode() == HttpStatus.OK) {
				LOG.info("Response from BPM service:");
				LOG.info(response.getBody());
			} 
		}
		catch (HttpStatusCodeException ex) {
			LOG.info("[ERROR] - An error occurred calling the BPM service: " + ex.getStatusCode().toString());
			// Get response body
			LOG.info(ex.getResponseBodyAsString());
		}
	}
	// @Recover
    // private void recover(Exception e){
	// 	LOG.info("[ERROR] - Recover function");

    // }
}
