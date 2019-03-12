package ibm.labs.kc.streams.containerManager;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

import com.google.gson.Gson;

import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.utils.ApplicationConfig;

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

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        final StreamsBuilder builder = new StreamsBuilder();
        Gson parser = new Gson();
        
        builder.stream("containers")
        		.foreach((key,value) -> System.out.println("received container " + key 
        				+ " " + parser.fromJson((String)value, ContainerEvent.class)));

        final Topology topology = builder.build();
        System.out.println(topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
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
