package ibm.labs.kc.containermgr.streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.utils.ApplicationConfig;

/**
 * Process container events to build a container inventory view.
 * 
 * @author jeromeboyer
 *
 */
public class ContainerInventoryView  implements ContainerDAO {
	private static final Logger logger = LoggerFactory.getLogger(ContainerInventoryView.class);
		
	private static ContainerInventoryView  instance;
	private KafkaStreams streams;
	private Gson jsonParser = new Gson();
	public static String CONTAINERS_STORE_NAME = "queryable-container-store";
	public static String CONTAINERS_TOPIC = "containers";
	
	public synchronized static ContainerDAO instance() {
        if (instance == null) {
            instance = new ContainerInventoryView();
        }
        return instance;
    }
	
	public  Topology buildProcessFlow() {
		final StreamsBuilder builder = new StreamsBuilder();
	   
	    builder.stream(CONTAINERS_TOPIC).mapValues((containerEvent) -> {
	    		 // the container payload is of interest to keep in table
	   			 Container c = jsonParser.fromJson((String)containerEvent, ContainerEvent.class).getPayload();
	   			 return jsonParser.toJson(c);
	   		 }).groupByKey()
	   		 	.reduce((key,container) -> {
	   		 		System.out.println("received container " + container );
	   		 		return container;
	   		 	},
	   	    	  Materialized.as(CONTAINERS_STORE_NAME));
	    return builder.build();
	}
	
	public  synchronized void start() {
		if (streams == null) {
			Properties props = ApplicationConfig.getStreamsProperties("container-streams");
		    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		    streams = new KafkaStreams(buildProcessFlow(), props);
			try {
	        	streams.cleanUp(); 
	            streams.start();
	        } catch (Throwable e) {
	            System.exit(1);
	        }
		}
	}
	
	public void stop() {
		streams.close();
	}

	@Override
	public Container getById(String containerId) {
		ReadOnlyKeyValueStore<String,String> view = streams.store(CONTAINERS_STORE_NAME, QueryableStoreTypes.keyValueStore());
		String cStrg = view.get(containerId);
		if (cStrg != null) 
			return jsonParser.fromJson(cStrg, Container.class);
		return null;
	}

}
