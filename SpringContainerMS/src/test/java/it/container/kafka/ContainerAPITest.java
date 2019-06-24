package it.container.kafka;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import ibm.labs.kc.containermgr.SBApplication;
import ibm.labs.kc.model.container.Container;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SBApplication.class,webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContainerAPITest {

    @Autowired
    private TestRestTemplate server;

    @LocalServerPort
    private int port;
    
    
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testGetContainerById() {
		String endpoint = "http://localhost:" + port + "/containers/c1";
		Container entity = server.getForObject(endpoint, Container.class);
		Assert.assertNotNull(entity);
	}

	 @Test
	 public void testGetContainers() throws Exception {
		 String endpoint = "http://localhost:" + port + "/containers";
	     String response = server.getForObject(endpoint, String.class);
	     System.out.println(response);
	     assertTrue("Invalid response from server : " + response, response.startsWith("["));
	 }
}
