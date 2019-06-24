package ibm.labs.kc.containermgr;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.dao.ContainerMapDAO;
import ibm.labs.kc.containermgr.model.ContainerEntity;

public class ContainerDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetAllContainers() {
		ContainerDAO dao = new ContainerMapDAO();
		List<ContainerEntity> l =dao.getAllContainers("Oakland");
		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() >= 2 );
	}
	
	@Test
	public void testGetNoContainer() {
		ContainerDAO dao = new ContainerMapDAO();
		List<ContainerEntity> l =dao.getAllContainers("LosAlamos");
		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() == 0 );
	}

	@Test
	public void testGetAllContainerById() {
		ContainerDAO dao = new ContainerMapDAO();
		ContainerEntity c =dao.getById("c1");
		Assert.assertNotNull(c);
	}
}
