package ibm.labs.kc.containermgr;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.dao.ContainerMapDAO;
import ibm.labs.kc.model.container.Container;

public class ContainerDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetAllContainers() {
		ContainerDAO dao = new ContainerMapDAO();
		List<Container> l =dao.getAllContainers("Oakland");
		Assert.assertNotNull(l);
		Assert.assertTrue(l.size() >= 2 );
	}

	@Test
	public void testGetAllContainerById() {
		ContainerDAO dao = new ContainerMapDAO();
		Container c =dao.getById("c1");
		Assert.assertNotNull(c);
	}
}
