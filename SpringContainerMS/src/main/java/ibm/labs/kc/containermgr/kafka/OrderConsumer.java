package ibm.labs.kc.containermgr.kafka;

import java.util.List;
import java.util.Map;
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
import ibm.labs.kc.containermgr.dao.OrderDAO;
import ibm.labs.kc.model.container.ContainerOrder;
import ibm.labs.kc.model.events.ContainerNotFoundEvent;
import ibm.labs.kc.model.events.OrderCreationEvent;
import ibm.labs.kc.model.events.OrderEvent;
import ibm.labs.kc.model.events.OrderRejectedEvent;
import ibm.labs.kc.order.model.Order;
/*
 * Consume events from 'orders' topic. Started when the spring application context
 * is initialized.
 */
@Component
public class OrderConsumer {
	private static final Logger LOG = Logger.getLogger(OrderConsumer.class.toString());
	@Value("${kafka.orders.consumer.groupid}")
	public String CONSUMER_GROUPID;
	@Value("${kcsolution.orders.topic}")
  	public String ORDERS_TOPIC;
	private Gson parser = new Gson();

	@Autowired
	private ContainerService containerService;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private OrderProducer orderProducer;

	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ContainerProperties containerProps = new ContainerProperties(ORDERS_TOPIC);
		LOG.info(" Topic:" + ORDERS_TOPIC + " " + CONSUMER_GROUPID);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		        	LOG.info("Received order event:" + message.value());
		        	if (message.value().contains(OrderEvent.TYPE_CREATED)) {
		        		OrderCreationEvent oe = parser.fromJson(message.value(), OrderCreationEvent.class);
		        		Order order = oe.getPayload();
						List<ContainerOrder> listOfContainers = containerService.assignContainerToOrder(order);
						if (listOfContainers.size()>0){
							String containers="";
							for (ContainerOrder co : listOfContainers){
								orderDAO.save(co);
								containers = containers + co.getContainerID() + " ";
							} 
							LOG.info("These are the containers assigned for the order " + order.getOrderID() + ": " + containers);
						}
						else {
							LOG.info("There is no container available for orderID: " + order.getOrderID() + ". The order will be rejected.");
							orderProducer.emit(new ContainerNotFoundEvent(order.getOrderID(), "A container could not be found for this order"));
						}
					}
					if (message.value().contains(OrderEvent.TYPE_REJECTED)) {
						OrderRejectedEvent orderRejected = parser.fromJson(message.value(), OrderRejectedEvent.class);
						Order order = orderRejected.getPayload();
						// Only unassign container from order when a container has previously been assigned.
						// Otherwise, this OrderReject event comes from this very microservice where a container had not been assigned.
						if (order.getContainerID() != null && order.getContainerID() != "" && !order.getContainerID().isEmpty()){
							if (!containerService.unAssignContainerToOrder(order))
								LOG.severe("[ERROR] - An error occurred unassigning container: " + order.getContainerID() + " from order: " + order.getOrderID());
						}
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
