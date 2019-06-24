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

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.model.events.ContainerCreationEvent;
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
		        	LOG.info("Received container event: " + message.value());
		        	if (message.value().contains(ContainerEvent.CONTAINER_ADDED)) {
		        		ContainerCreationEvent ce = parser.fromJson(message.value(), ContainerCreationEvent.class);
		        		ContainerEntity cce = new ContainerEntity(ce.getPayload());
		        		cce.setCurrentCity(cityDAO.getCityName(cce.getLatitude(),cce.getLongitude()));
		        		containerDAO.save(cce);
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
