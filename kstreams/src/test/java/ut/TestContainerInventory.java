
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
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ibm.labs.kc.containermgr.streams.ContainerInventoryView;
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
	
	static Gson parser = new Gson();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	
	
	private ContainerEvent buildContainerEvent() {
		Container c = new Container("c01", "Brand", "Reefer",100, 37.8000,-122.25);
		c.setStatus("atDock");
		return  new ContainerEvent(ContainerEvent.CONTAINER_ADDED,"1.0",c);
	}
	
	
	@Test
	public void shouldHaveContainerInTableFromContainerCreatedEvent() {
		
		ContainerEvent ce = buildContainerEvent();
		ContainerInventoryView dao = (ContainerInventoryView)ContainerInventoryView.instance();
		
		Properties props = ApplicationConfig.getStreamsProperties("test");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
		
		TopologyTestDriver testDriver = new TopologyTestDriver(
				dao.buildProcessFlow(), props);

		ConsumerRecordFactory<String, String> factory = new ConsumerRecordFactory<String, String>("containers",
				new StringSerializer(), new StringSerializer());
		ConsumerRecord<byte[],byte[]> record = factory.create("containers",ce.getContainerID(), parser.toJson(ce));
		
		testDriver.pipeInput(record);
		
	//	Container container = dao.getById(ce.getContainerID());
		KeyValueStore<String, String> store = testDriver.getKeyValueStore(ContainerInventoryView.CONTAINERS_STORE_NAME);
		String containerStrg = store.get(ce.getContainerID());
		Assert.assertNotNull(containerStrg);
		Assert.assertTrue(containerStrg.contains(ce.getContainerID()));
		Assert.assertTrue(containerStrg.contains("atDock"));
		System.out.println("From store -> " + containerStrg);
		// now send a new event the container is updated in the table - keep last data
		ce.getPayload().setStatus("onTruck");
		record = factory.create("containers",ce.getContainerID(), parser.toJson(ce));
		testDriver.pipeInput(record);
		containerStrg = store.get(ce.getContainerID());
		Assert.assertFalse(containerStrg.contains("atDock"));
		Assert.assertTrue(containerStrg.contains("onTruck"));
		testDriver.close();
	}

}
