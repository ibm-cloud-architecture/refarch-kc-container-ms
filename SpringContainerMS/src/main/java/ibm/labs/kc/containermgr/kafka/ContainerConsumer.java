package ibm.labs.kc.containermgr.kafka;

import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import ibm.labs.kc.containermgr.dao.ContainerRepository;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.model.container.Container;
import ibm.labs.kc.model.events.ContainerEvent;
/*
 * Consume events from 'containers' topic. Started when the spring application context
 * is initialized.
 */
@Component
public class ContainerConsumer {
	public static String CONSUMER_GROUPID = "ContainerSpringConsumer";
	private Gson parser = new Gson();
	@Autowired
	private ContainerRepository containerRepository;
	
	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ContainerProperties containerProps = new ContainerProperties(KCKafkaConfiguration.CONTAINERS_TOPIC);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
		        @Override
		        public void onMessage(ConsumerRecord<Integer, String> message) {
		        	System.out.println(message.value());
		        	if (message.value().contains(ContainerEvent.CONTAINER_ADDED)) {
		        		ContainerEvent<Container> ce = parser.fromJson(message.value(), ContainerEvent.class);
		        		ContainerEntity cce = new ContainerEntity(ce.getPayload());
		        		containerRepository.save(cce);
		        	}
		        	
		        }
		    });
		KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
		container.setBeanName(CONSUMER_GROUPID);
		container.start();
	}
	
	private KafkaMessageListenerContainer<Integer, String> createContainer(
            ContainerProperties containerProps) {
		Map<String, Object> props = KCKafkaConfiguration.getConsumerProperties(CONSUMER_GROUPID);
		DefaultKafkaConsumerFactory<Integer, String> cf =
			                new DefaultKafkaConsumerFactory<Integer, String>(props);
		KafkaMessageListenerContainer<Integer, String> container =
			                new KafkaMessageListenerContainer<>(cf, containerProps);
			return container;
	}
}
