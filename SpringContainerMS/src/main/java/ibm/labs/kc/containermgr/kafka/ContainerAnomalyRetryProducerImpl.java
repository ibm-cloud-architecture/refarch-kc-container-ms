package ibm.labs.kc.containermgr.kafka;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import ibm.labs.kc.model.events.ContainerEvent;

@Component
public class ContainerAnomalyRetryProducerImpl implements ContainerProducer {
	private static final Logger LOG = Logger.getLogger(ContainerAnomalyRetryProducerImpl.class.toString());

	@Value("${kcsolution.container.anomaly.retry.topic}")
  	public String CONTAINER_ANOMALY_RETRY_TOPIC;
	
	@Value("${kafka.container.anomaly.retry.producer.clientid}")
	public String CLIENT_ID;
	
	protected KafkaTemplate<String, String> template;

	public ContainerAnomalyRetryProducerImpl() {
		template = createTemplate();
	    template.setDefaultTopic(CONTAINER_ANOMALY_RETRY_TOPIC);
	}

	@Override
	public void emit(ContainerEvent co) {
		String value = new Gson().toJson(co);
		LOG.info("Emit container anomaly retry event:" + value);
		String key = co.getContainerID();
		ProducerRecord<String,String> record = new ProducerRecord<String,String>(CONTAINER_ANOMALY_RETRY_TOPIC,key,value);
		template.send(record);
	}

	@Override
	public List<ContainerEvent> getEventsSent() {
		return null;
	}

	private KafkaTemplate<String, String> createTemplate() {
	    Map<String, Object> senderProps = KCKafkaConfiguration.getPublisherProperties(CLIENT_ID + UUID.randomUUID().toString());
	    LOG.info("@@@@ brokers url:"+senderProps.get(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG));
	    LOG.info("@@@@ brokers apikey:"+senderProps.get(SaslConfigs.SASL_JAAS_CONFIG));

	    ProducerFactory<String, String> pf =
	              new DefaultKafkaProducerFactory<String, String>(senderProps);
	    KafkaTemplate<String, String> template = new KafkaTemplate<>(pf);
	    return template;
	}
}
