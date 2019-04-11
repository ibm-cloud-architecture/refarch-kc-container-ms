package ibm.labs.kc.containermgr.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ibm.labs.kc.model.City;
import ibm.labs.kc.model.container.Container;

public class ContainerMapDAO implements ContainerDAO {
	
	private static HashMap<String,Container> containers = new  HashMap<String,Container>();

	public ContainerMapDAO() {
		
		containers.put("c1", new Container("c1","Brand","Reefer",100,0,0));
		containers.put("c2", new Container("c2","Brand","Reefer",110,0,0));
	}
	@Override
	public Container getById(String containerId) {
		return containers.get(containerId);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Container> getAllContainers(City city) {
		return new ArrayList<Container>(containers.values());
	}

	@Override
	public List<Container> getAllContainers(String city) {
		return new ArrayList<Container>(containers.values());
	}

}
