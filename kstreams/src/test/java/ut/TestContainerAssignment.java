package ut;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.model.Container;
import ibm.labs.kc.streams.containerManager.CityDAO;


/**
 * A container is at a location with (long,lat) 
 * @author jeromeboyer
 *
 */
public class TestContainerAssignment {
	private static CityDAO dao;
	
	@BeforeClass
	public static void init() {
		dao = new CityDAO();
	}
	
	@Test
	public void testIfContainerIsInCity() {
		Container c = new Container("c01", "Reefer", 37.8000,-122.25);
		String city = dao.getCity(c.getLatitude(), c.getLongitude());
		Assert.assertNotNull(city);
	}

}
