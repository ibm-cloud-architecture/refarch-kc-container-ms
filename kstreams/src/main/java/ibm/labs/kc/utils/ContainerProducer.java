package ibm.labs.kc.utils;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.gson.Gson;

import ibm.labs.kc.model.Address;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.Order;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.model.events.OrderEvent;

public class ContainerProducer {
	private KafkaProducer<String, String> kafkaProducer;
	 
	public ContainerProducer() {
		 Properties properties = ApplicationConfig.getProducerProperties("container-producer");
	     kafkaProducer = new KafkaProducer<String, String>(properties);
	}

	public ContainerEvent buildContainerEvent() {
		Container c = new Container("cid-01", "IntegrationTests", "Reefer",100, 37.8000,-122.25);
		c.setStatus("atDock");
		return  new ContainerEvent(ContainerEvent.CONTAINER_ADDED,"1.0",c);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ContainerProducer p = new ContainerProducer();
		p.emit(p.buildContainerEvent());
	}

	
	public void emit(ContainerEvent e) throws InterruptedException, ExecutionException, TimeoutException {		
		String key = e.getPayload().getContainerID();
		
		String value = new Gson().toJson(e);
	    ProducerRecord<String, String> record = new ProducerRecord<>(ApplicationConfig.CONTAINER_TOPIC, key, value);

	    Future<RecordMetadata> send = kafkaProducer.send(record);
	    send.get(ApplicationConfig.PRODUCER_TIMEOUT_SECS, TimeUnit.SECONDS);
	    System.out.println(" Emit container event " + e.getPayload().getContainerID());
	    kafkaProducer.close();
	}
}
