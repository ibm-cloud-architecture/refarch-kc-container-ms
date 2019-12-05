package ibm.labs.kc.containermgr.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.order.model.City;

public interface ContainerDAO {

	public ContainerEntity getById(String containerId);

	public List<ContainerEntity> getAllContainers(City city);
	
	public List<ContainerEntity> getAllContainers(String city);

	public Page<ContainerEntity> getAllContainers(Pageable pageable);
	
	public ContainerEntity save(ContainerEntity e);
	
	public ContainerEntity update(String containerId, ContainerEntity container);

	public boolean existsById(String containerId);
}
