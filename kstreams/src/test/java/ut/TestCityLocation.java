package ut;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;

import ibm.labs.kc.streams.containerManager.CityDAO;

public class TestCityLocation {
	
	private static CityDAO dao;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new CityDAO();
	}

	@Test
	public void testCityMatch() {
		String city = dao.getCity(37.8000,-122.25);
		assertNotNull(city);
	}
	
	@Test
	public void testCityUnMatch() {
		String city = dao.getCity(38.8000,-124.25);
		assertNull(city);
	}

}
