package ibm.labs.kc.containermgr.kafka;

import java.util.List;
import java.util.Map;

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
import ibm.labs.kc.model.Order;
import ibm.labs.kc.model.container.ContainerOrder;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.model.events.OrderEvent;
/*
 * Consume events from 'orders' topic. Started when the spring application context
 * is initialized. 
 */
@Component
public class OrderConsumer {
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
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		        	System.out.println(message.value());
		        	if (message.value().contains(ContainerEvent.CONTAINER_ADDED)) {
		        		OrderEvent<Order> oe = parser.fromJson(message.value(), OrderEvent.class);
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
