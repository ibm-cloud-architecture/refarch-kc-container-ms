package ibm.labs.kc.streams.containerManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.utils.ApplicationConfig;
import ibm.labs.kc.utils.JsonPOJODeserializer;
import ibm.labs.kc.utils.JsonPOJOSerializer;

/**
 * Process container events to build a container inventory view.
 * 
 * @author jeromeboyer
 *
 */
public class ContainerInventoryView {
	
	
	public ContainerInventoryView() {
		
	}
	
	
	/**
	 * Can be used as a command tool.
	 * @param args
	 */
	public static void main(String[] args) {
		
        Properties props = ApplicationConfig.getStreamsProperties("container-streams");

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<ContainerEvent> containerSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", ContainerEvent.class);
        containerSerializer.configure(serdeProps, false);
        final Deserializer<ContainerEvent> containerDeserializer = new JsonPOJODeserializer<>();
        containerDeserializer.configure(serdeProps, false);
        
        final Serde<ContainerEvent> containerSerde = Serdes.serdeFrom(containerSerializer, containerDeserializer);
        
        final StreamsBuilder builder = new StreamsBuilder();


        // State stores should only be mutated by the corresponding processor topology
        KTable<String,ContainerEvent> containers = builder.table("containers", 
        		Consumed.with(Serdes.String(), containerSerde),
        		 Materialized.as("queryable-container-store"));

		//.foreach((key,value) -> System.out.println("received container " + key 
		//		+ " " + value));
        final Topology topology = builder.build();
        System.out.println(topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
            	// access to the table of containers
            	ReadOnlyKeyValueStore<String, ContainerEvent> keyValueStore =
            		    streams.store("queryable-container-store", QueryableStoreTypes.keyValueStore());
            	
                // Mockup to read all rows of the table
            	KeyValueIterator<String, ContainerEvent> range = keyValueStore.all();
            	while (range.hasNext()) {
            	  KeyValue<String, ContainerEvent> next = range.next();
            	  System.out.println("Container " + next.key + ": " + next.value.getPayload().getType());
            	}
            	
            	// Mockup call by container ID
            	
                streams.close();
                latch.countDown();
            }
        });

        try {
        	streams.cleanUp(); // delete the app local state
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

}
