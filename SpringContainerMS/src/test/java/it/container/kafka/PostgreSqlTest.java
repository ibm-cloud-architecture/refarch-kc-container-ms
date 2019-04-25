package it.container.kafka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ibm.labs.kc.containermgr.dao.ContainerRepository;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.model.ContainerStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostgreSqlTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Autowired
	public ContainerRepository containerRepo;

	@Test
	public void jdbcConnectionToCloud() throws ClassNotFoundException, SQLException {
		 Map<String, String> env = System.getenv();
		Class.forName("org.postgresql.Driver");
		String url = env.get("POSTGRESQL_URL");
		Properties props = new Properties();
		props.setProperty("user",env.get("POSTGRESQL_USER"));
		// following property allows SSL connections to be made without validating the server's certificate.
		props.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
		props.setProperty("password",env.get("POSTGRESQL_PWD"));
		props.setProperty("ssl","true");
		Connection conn = DriverManager.getConnection(url, props);
	}
	
	@Test
	public void testAccessToRemoteDBSpringDatasource() {
		Optional<ContainerEntity> cOut = containerRepo.findById("c1");
		if (!cOut.isPresent()) {
			ContainerEntity c = new ContainerEntity("c1","Brand","Reefer",100,0,0,"Oakland");
			c.setCreatedAt(new Date());
			c.setUpdatedAt(c.getCreatedAt());
			containerRepo.save(c);
			cOut = containerRepo.findById("c1");
			Assert.assertNotNull(cOut);
			Assert.assertNotNull(cOut.get());
			Assert.assertTrue(cOut.get().getBrand().equals(c.getBrand()));
		}
	}
	
	@Test
	public void testUpdateContainer() {
		Optional<ContainerEntity> cOut = containerRepo.findById("c2");
		if (!cOut.isPresent()) {
			ContainerEntity c = new ContainerEntity("c2","Brand","Reefer",100,0,0,"RedwoodCity");
			c.setCreatedAt(new Date());
			c.setUpdatedAt(c.getCreatedAt());
			containerRepo.save(c);
			cOut = containerRepo.findById("c2");
			Assert.assertNotNull(cOut);
			Assert.assertNotNull(cOut.get());
			cOut.get().setCurrentCity("Oakland");
			cOut.get().setStatus(ContainerStatus.Empty);
			containerRepo.save(cOut.get());	
		}
	}

}
