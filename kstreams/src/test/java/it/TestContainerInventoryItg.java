package it;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.containermgr.streams.ContainerInventoryView;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.events.ContainerEvent;
import ibm.labs.kc.utils.ContainerProducer;
import org.junit.Assert;

public class TestContainerInventoryItg {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void shouldHaveContainerInTableInKafkaFromContainerCreatedEvent() throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println(" ----- Kafka needs to run \n\tshouldHaveContainerInTableInKafkaFromContainerCreatedEvent");
		ContainerProducer cp = new ContainerProducer();
		ContainerEvent ce = cp.buildContainerEvent();
		
		// Start the container Streams process flow
		ContainerInventoryView dao = (ContainerInventoryView)ContainerInventoryView.instance();
		dao.start();
		Thread.currentThread().sleep(5000);
		// emit container event
		cp.emit(ce);
		// verify it is in the container store
		Container cOut = dao.getById(ce.getContainerID());
		Assert.assertEquals(cOut.getStatus(),ce.getPayload().getStatus());
	}

}
