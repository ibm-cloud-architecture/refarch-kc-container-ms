package ibm.labs.kc.containermgr;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import ibm.labs.kc.model.container.Container;
import ibm.labs.kc.model.events.ContainerCreationEvent;

public class ParseSomeContainerTest {
	static Gson parser = new Gson();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testContainerToJson() {
		Container c = new Container("C2","UT-Brand","Reefer",200,37,-123);
		ContainerCreationEvent ce = new ContainerCreationEvent("ContainerAdded","V1",c);
		System.out.println(parser.toJson(ce));
	}

	@Test
	public void testJsonToContainer() {
		String data = "{\"containerID\": \"ut-C02\", \"timestamp\": 1556234258, \"type\": \"ContainerAdded\", \"payload\": {\"containerID\": \"ut-C02\", \"type\": \"Reefer\", \"status\": \"Empty\", \"latitude\": 37.93542101679729, \"longitude\": -123.25324039061178, \"capacity\": 110, \"brand\": \"itg-brand\"}}";
		ContainerCreationEvent ce = parser.fromJson(data,ContainerCreationEvent.class);
		Container c = ce.getPayload();
		Assert.assertTrue(c.getContainerID().equals("ut-C02"));
	}
}
