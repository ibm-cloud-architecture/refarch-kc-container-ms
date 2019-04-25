package ibm.labs.kc.containermgr.dao;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.rest.ResourceNotFoundException;
import ibm.labs.kc.model.City;

@Component
public class ContainerDAOImpl implements ContainerDAO {
	private static final Logger LOG = Logger.getLogger(ContainerDAOImpl.class.toString());
	
	@Autowired
	private ContainerRepository containerRepository;
	
	@Value("${spring.datasource.url}")
	protected String url;
	
	@PostConstruct
	public void trace() {
		LOG.info(url);
	}
	
	@Override
	public ContainerEntity getById(String containerId) {
		return containerRepository.findById(containerId)
		.orElseThrow(() -> 
			new ResourceNotFoundException("Container not found with id " + containerId));
	}

	@Override
	public List<ContainerEntity> getAllContainers(City city) {
		return getAllContainers(city.getName());
	}

	@Override
	public List<ContainerEntity> getAllContainers(String currentCity) {

		return containerRepository.findByCurrentCity(currentCity);
	}
	
	public Page<ContainerEntity> getAllContainers(Pageable pageable) {
	  return containerRepository.findAll(pageable);
	}

	@Override
	public ContainerEntity save(ContainerEntity e) {
		return containerRepository.save(e);
	}
	
	@Override
	public ContainerEntity update(String containerId, ContainerEntity containerRequest) {
		return  containerRepository.findById(containerId)
				.map( container -> {
					  container.setBrand(containerRequest.getBrand());
					  container.setType(containerRequest.getType());
					  container.setCapacity(containerRequest.getCapacity());
					  container.setCurrentCity(containerRequest.getCurrentCity());
					  container.setStatus(containerRequest.getStatus());
					  container.setUpdatedAt(new Date());
					  return containerRepository.save(container);
		}).orElseThrow(() -> new ResourceNotFoundException("Container not found with id " + containerId));
	}
}
