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

	@Test
	public void testDeleteOneContainer() {
		ContainerDAO dao = new ContainerMapDAO();
		ContainerEntity ce = dao.getById("c1");
		Assert.assertTrue(dao.deleteContainer(ce));
		
		List<ContainerEntity> pce = dao.getAllContainers("Oakland");
		Assert.assertEquals(1, pce.size());
		Assert.assertEquals("c2", pce.get(0).getId());
	}

	@Test
	public void testDeleteAllContainers() {
		ContainerDAO dao = new ContainerMapDAO();
		Assert.assertTrue(dao.existsById("c1"));
		Assert.assertTrue(dao.existsById("c2"));
		Assert.assertTrue(dao.existsById("c3"));
		Assert.assertTrue(dao.existsById("c4"));

		Assert.assertTrue(dao.deleteAllContainers());

		Assert.assertFalse(dao.existsById("c1"));
		Assert.assertFalse(dao.existsById("c2"));
		Assert.assertFalse(dao.existsById("c3"));
		Assert.assertFalse(dao.existsById("c4"));
	}
}
