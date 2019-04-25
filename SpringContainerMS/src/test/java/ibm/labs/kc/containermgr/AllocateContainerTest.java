package ibm.labs.kc.containermgr;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.dao.ContainerMapDAO;
import ibm.labs.kc.containermgr.kafka.ContainerProducer;
import ibm.labs.kc.containermgr.kafka.OrderProducer;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.model.ContainerStatus;
import ibm.labs.kc.model.Address;
import ibm.labs.kc.model.Order;
import ibm.labs.kc.model.container.ContainerOrder;


public class AllocateContainerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	public static Order prepareOrder() {
		Address pA = new Address();
		pA.setCity("Oakland");
		Address dA = new Address();
		dA.setCity("Beijing");
		Order o = new Order("ORD01","FreshProduct01","Customer01",50,pA,"",dA,"",Order.PENDING_STATUS);
		return o;
	}

	@Test
	public void shouldHaveRemainingCapacityInContainer() {
		ContainerService serv = new ContainerService(null, null,null,null);
		ContainerEntity ce = new ContainerEntity();
		ce.setCapacity(100);
		int q = serv.manageCapacity(60, ce);
		Assert.assertTrue( q == 0);
		Assert.assertTrue(ce.getCapacity() == 40);
		Assert.assertTrue(ce.getStatus().equals(ContainerStatus.PartiallyLoaded));
	}
	
	@Test
	public void shouldHaveNoMoreCapacityInContainer() {
		ContainerService serv = new ContainerService(null, null,null,null);
		ContainerEntity ce = new ContainerEntity();
		ce.setCapacity(100);
		int q = serv.manageCapacity(120, ce);
		Assert.assertTrue( q == 20);
		Assert.assertTrue(ce.getCapacity() == 0);
		Assert.assertTrue(ce.getStatus().equals(ContainerStatus.Loaded));
	}
	
	
	@Test
	public void testReceivedOrderAllocateContainer() {
		Order o =prepareOrder();
		CityDAO cityDAO = new CityDAO();
		ContainerDAO containerDAO = new ContainerMapDAO();
		OrderProducer op = new OrderProducerMockup();
		ContainerProducer cp = new ContainerProducerMockup();
		ContainerService serv = new ContainerService(cityDAO, containerDAO,op,cp);
		List<ContainerOrder> l = serv.assignContainerToOrder(o);
		Assert.assertTrue(l.size() == 1);
		Assert.assertNotNull(l.get(0).getContainerID());	
		Assert.assertTrue(op.getEventsSent().size() == 1);
	}
	
	@Test
	public void testReceivedOrderNoMatchingContainer() {
		Address pA = new Address();
		pA.setCity("Los Alamos");
		Address dA = new Address();
		dA.setCity("Beijing");
		Order o = new Order("ORD01","FreshProduct01","Customer01",10,pA,"",dA,"",Order.PENDING_STATUS);
		CityDAO cityDAO = new CityDAO();
		ContainerDAO containerDAO = new ContainerMapDAO();
		OrderProducer op = new OrderProducerMockup();
		ContainerProducer cp = new ContainerProducerMockup();
		ContainerService serv = new ContainerService(cityDAO, containerDAO,op,cp);
		List<ContainerOrder> l = serv.assignContainerToOrder(o);
		Assert.assertTrue(l.size() == 0);
		Assert.assertTrue(op.getEventsSent().size() == 0);
	}
	
	@Test
	public void shouldHaveTwoContainersAllocated() {
		Order o =prepareOrder();
		o.setQuantity(160);
		CityDAO cityDAO = new CityDAO();
		ContainerDAO containerDAO = new ContainerMapDAO();
		OrderProducer op = new OrderProducerMockup();
		ContainerProducer cp = new ContainerProducerMockup();
		ContainerService serv = new ContainerService(cityDAO, containerDAO,op,cp);
		List<ContainerOrder> l = serv.assignContainerToOrder(o);
		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() == 2);
		Assert.assertNotNull(l.get(0).getContainerID());	
		Assert.assertTrue(op.getEventsSent().size() == 2);
	}

}
