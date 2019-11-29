package ibm.labs.kc.containermgr.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import ibm.labs.kc.model.container.ContainerOrder;
import ibm.labs.kc.order.model.City;

/**
 * Data access to the orders associated to containers
 */
@Component
public class OrderDAO {
	private static final Logger LOG = Logger.getLogger(OrderDAO.class.toString());
	private Map<String,List<String>> containerOrders = new ConcurrentHashMap<String,List<String>>();
	
	public void save(ContainerOrder containerorder) {
		if (this.containerOrders.containsKey(containerorder.getContainerID())){
			// Add this orderID to the list of orders for this containerID
			if (this.containerOrders.get(containerorder.getContainerID()) instanceof List<?>){
				List<String> orders_list = containerOrders.get(containerorder.getContainerID());
				orders_list.add(containerorder.getOrderID());
				containerOrders.put(containerorder.getContainerID(), orders_list);
			}
			else {
				// An error occurred when getting the list of anomaly events for this containerID
				LOG.severe("[ERROR] - There was a problem retrieving the orders for ContainerID " + containerorder.getContainerID());
			}
		}
		// This is the first order for this containerID
		else {
			List<String> new_list = new ArrayList<String>();
			new_list.add(containerorder.getOrderID());
			containerOrders.put(containerorder.getContainerID(), new_list);
		}
	}
	
	public List<String> getOrders(String containerId) {
		return containerOrders.get(containerId);
	}
	
	public void removeOrders(String containerId){
		this.containerOrders.remove(containerId);
	}
}
