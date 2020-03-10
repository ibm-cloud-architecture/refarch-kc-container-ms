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

import ibm.labs.kc.model.events.ContainerAnomalyEventRetry;
import ibm.labs.kc.model.events.ContainerEvent;
/*
 * Consume events from the container anomaly retry topic.
 * Started when the spring application context is initialized.
 */
@Component
public class ContainerAnomalyEventRetryConsumer {
	
	private static final Logger LOG = Logger.getLogger(ContainerAnomalyEventRetryConsumer.class.toString());
	
	@Value("${kafka.container.anomaly.retry.consumer.groupid}")
	public String CONSUMER_GROUPID;
	@Value("${kcsolution.container.anomaly.retry.topic}")
  	public String CONTAINERS_TOPIC;
	private Gson parser = new Gson();

	@Autowired
	private BpmAgent bpmAgent;

	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ContainerProperties containerProps = new ContainerProperties(CONTAINERS_TOPIC);
		LOG.info(" Topic:" + CONTAINERS_TOPIC + " " + CONSUMER_GROUPID);
		containerProps.setMessageListener(new MessageListener<Integer, String>() {
			@Override
			public void onMessage(ConsumerRecord<Integer, String> message) {
				if (message.value().contains(ContainerEvent.CONTAINER_ANOMALY_RETRY)) {
					// --------------------------------------------
					// ContainerAnomalyEventRetry events
					//---------------------------------------------
					LOG.info("Received new ContainerAnomalyEventRetry event: " + message.value());
					// Get the ContainerAnomalyRetryEvent objet from the event received
					ContainerAnomalyEventRetry caer = parser.fromJson(message.value(), ContainerAnomalyEventRetry.class);
					int time_to_wait = caer.getRetries() * 10;
					LOG.info("This is a BPM call retry. Applying a delay of " + time_to_wait + " seconds");
					try {
						Thread.sleep(time_to_wait * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bpmAgent.callBPM(caer,false);
				}
				else {
					// --------------------------------------------
					// Others
					//---------------------------------------------
					LOG.severe("[ERROR] -  Received unexpected event: " + message.value());
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
