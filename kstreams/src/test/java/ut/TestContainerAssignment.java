package ut;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.model.Container;


/**
 * A container is at a location with (long,lat) 
 * @author jerome boyer
 *
 */
public class TestContainerAssignment {
	private static CityDAO dao;
	
	@BeforeClass
	public static void init() {
		dao = new CityDAO();
	}
	
	@Test
	public void shouldContainerBeInCity() {
		Container c = new Container("c01", "Brand", "Reefer",100, 37.8000,-122.25);
		String city = dao.getCity(c.getLatitude(), c.getLongitude());
		Assert.assertNotNull(city);
	}
	
	@Test
	public void shouldNotContainerBeInCity() {
		Container c = new Container("c01", "Brand", "Reefer",100, 45,-120);
		String city = dao.getCity(c.getLatitude(), c.getLongitude());
		Assert.assertNull(city);
	}

}
