package ibm.labs.kc.containermgr.kafka;

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
import ibm.labs.kc.model.events.OrderCreationEvent;
import ibm.labs.kc.model.events.OrderEvent;
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
	@Value("${kcsolution.orders}")
    public String ORDERS_TOPIC;
	private Gson parser = new Gson();
	
	@Autowired
	private ContainerService containerService;
	
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
		        		Order o = oe.getPayload();
						containerService.assignContainerToOrder(o);
		        	}
		        }
		    });
		KafkaMessageListenerContainer<Integer, String> kafkaEventListener = createSpringKafkaListener(containerProps);
		kafkaEventListener.setBeanName(CONSUMER_GROUPID);
		kafkaEventListener.start();
	}
	
	private KafkaMessageListenerContainer<Integer, String> createSpringKafkaListener(
            ContainerProperties containerProps) {
		Map<String, Object> props = KCKafkaConfiguration.getConsumerProperties(CONSUMER_GROUPID);
		DefaultKafkaConsumerFactory<Integer, String> cf =
			                new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container =
			                new KafkaMessageListenerContainer<>(cf, containerProps);
			return container;
	}
}
