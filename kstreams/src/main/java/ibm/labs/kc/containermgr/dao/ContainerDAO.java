package ibm.labs.kc.containermgr.dao;

import ibm.labs.kc.model.Container;

public interface ContainerDAO {

	Container getById(String containerId);

	void start();

}
