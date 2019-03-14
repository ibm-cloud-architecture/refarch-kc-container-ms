
package ut;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.utils.ApplicationConfig;
import ibm.labs.kc.utils.JsonPOJODeserializer;
import ibm.labs.kc.utils.JsonPOJOSerializer;

/**
 * Validate a set of expected behavior for container inventory
 * @author jeromeboyer
 *
 */
public class TestContainerInventory {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	public static Topology buildProcessFlow(Serde<ContainerEvent> valueSerde,Serde<Container> newValue) {
		 final StreamsBuilder builder = new StreamsBuilder();
	        /*
	        builder.stream("containers",Consumed.with(Serdes.String(), valueSerde))
	        		// containers topic has ContainerEvent with the key being the containerID
	        		.groupByKey()
	        		.reduce((key,value) -> {
	        			System.out.println("received container event " + key + " " + value.getType());
	        			return value;})
	        		.mapValues(new ValueMapper<ContainerEvent, Container>() {
	        			public Container apply(ContainerEvent ce) {
	        				return ce.getPayload();
	        			}
	        		}, 
	        		Materialized.as("queryable-container-store"));
*/
	       builder.table("containers",Consumed.with(Serdes.String(), valueSerde))
	       .mapValues(new ValueMapper<ContainerEvent,Container>() {
   			public Container apply(ContainerEvent ce) {
   				return ce.getPayload();
   			}
   		}, 
   		Materialized.as("queryable-container-store"));

	        return builder.build();
	}
	
	public Serde<ContainerEvent> buildContainerEventSerde() {
		Map<String, Object> serdeProps = new HashMap<>();
		final Serializer<ContainerEvent> containerEventSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", ContainerEvent.class);
        containerEventSerializer.configure(serdeProps, false);
        final Deserializer<ContainerEvent> containerEventDeserializer = new JsonPOJODeserializer<>();
        containerEventDeserializer.configure(serdeProps, false);
        
        return Serdes.serdeFrom(containerEventSerializer, containerEventDeserializer);
	}
	
	public Serde<Container> buildContainerSerde() {
		Map<String, Object> serdeProps = new HashMap<>();
		final Serializer<Container> containerSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Container.class);
        containerSerializer.configure(serdeProps, false);
        final Deserializer<Container> containerDeserializer = new JsonPOJODeserializer<>();
        containerDeserializer.configure(serdeProps, false);
        
        return Serdes.serdeFrom(containerSerializer, containerDeserializer);
	}
	
	
	//@Test
	public void testContainerCreated() {
		Container c = new Container("c01", "Brand", "Reefer",100, 37.8000,-122.25);
		c.setStatus("atDock");
		ContainerEvent ce =  new ContainerEvent(ContainerEvent.CONTAINER_ADDED,"1.0",c);
		Gson parser = new Gson();
		String ceStr = parser.toJson(ce);
		Properties props = ApplicationConfig.getStreamsProperties("test");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");

		
        //props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, containerSerde);
    	
		Serde<Container> containerSerde = buildContainerSerde();
		Serde<ContainerEvent> containerEventSerde = buildContainerEventSerde();
		TopologyTestDriver testDriver = new TopologyTestDriver(
				buildProcessFlow(containerEventSerde, containerSerde), props);
		ConsumerRecordFactory<String, ContainerEvent> factory = new ConsumerRecordFactory<String, ContainerEvent>("containers",
				new StringSerializer(), containerEventSerde.serializer());
		ConsumerRecord<byte[],byte[]> record = factory.create("containers","c01", ce);
		testDriver.pipeInput(record);
		
		KeyValueStore<String, Container> store = testDriver.getKeyValueStore("queryable-container-store");
		KeyValueIterator<String, Container> i = store.all();
		while (i.hasNext()) {
			//Container cStored = i.next().value;
			//System.out.println(cStored.getContainerID());
			System.out.println( i.next().value);
		}
		//fail("Not yet implemented");
	}

}
