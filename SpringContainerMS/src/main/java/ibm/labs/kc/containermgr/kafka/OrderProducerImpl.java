package ibm.labs.kc.containermgr.kafka;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import ibm.labs.kc.model.events.OrderEvent;

@Component
public class OrderProducerImpl implements OrderProducer {
	private static final Logger LOG = Logger.getLogger(OrderProducerImpl.class.toString());
	
	@Value("${kcsolution.orders}")
    public  String ORDERS_TOPIC;
	@Value("${kafka.orders.producer.clientid}")
	public String CLIENT_ID;
	protected KafkaTemplate<String, String> template;
	
	
	public OrderProducerImpl() {
		template = createTemplate();
	    template.setDefaultTopic(ORDERS_TOPIC);
	}
	
	
	@Override
	public void emit(OrderEvent co) {
		String value = new Gson().toJson(co);
		LOG.info("Emit order event:" + value);
		String key = co.getOrderID();
		ProducerRecord<String,String> record = new ProducerRecord<String,String>(ORDERS_TOPIC,key,value);
		template.send(record);
	}

	@Override
	public List<OrderEvent> getEventsSent() {
		return null;
	}

	private KafkaTemplate<String, String> createTemplate() {
	    Map<String, Object> senderProps = KCKafkaConfiguration.getPublisherProperties(CLIENT_ID + UUID.randomUUID().toString()); 
	    ProducerFactory<String, String> pf =
	              new DefaultKafkaProducerFactory<String, String>(senderProps);
	    return new KafkaTemplate<>(pf);
	}
}
