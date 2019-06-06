package ibm.labs.kc.containermgr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.order.model.City;

public class ContainerMapDAO implements ContainerDAO {
	
	private static HashMap<String,ContainerEntity> containers = new  HashMap<String,ContainerEntity>();

	public ContainerMapDAO() {
		
		containers.put("c1", new ContainerEntity("c1","Brand","Reefer",100,0,0,"Oakland"));
		containers.put("c2", new ContainerEntity("c2","Brand","Reefer",110,0,0,"Oakland"));
		containers.put("c3", new ContainerEntity("c3","Brand","Reefer",100,0,0,"Los Angeles"));
		containers.put("c4", new ContainerEntity("c4","Brand","Reefer",100,0,0,"Los Angeles"));
	}
	@Override
	public ContainerEntity getById(String containerId) {
		return containers.get(containerId);
	}


	@Override
	public List<ContainerEntity> getAllContainers(City city) {
		return getAllContainers(city.getName());
	}

	@Override
	public List<ContainerEntity> getAllContainers(String city) {
		List<ContainerEntity> l = new ArrayList<ContainerEntity>();
		for (ContainerEntity c : containers.values()) {
			if (c.getCurrentCity().equals(city)) {
				l.add(c);
			}
		}
		return l;
	}
	
	@Override
	public Page<ContainerEntity> getAllContainers(Pageable pageable) {
		return null;
	}
	
	@Override
	public ContainerEntity save(ContainerEntity e) {
		containers.put(e.getId(),e);
		return e;
	}
	@Override
	public ContainerEntity update(String containerId, ContainerEntity container) {
		// TODO Auto-generated method stub
		return null;
	}

}
