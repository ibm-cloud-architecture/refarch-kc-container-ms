package ibm.labs.kc.containermgr;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.kafka.ContainerProducer;
import ibm.labs.kc.containermgr.kafka.OrderProducer;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.model.ContainerStatus;
import ibm.labs.kc.containermgr.rest.ResourceNotFoundException;
import ibm.labs.kc.model.container.Container;
import ibm.labs.kc.model.container.ContainerOrder;
import ibm.labs.kc.model.events.ContainerAssignmentEvent;
import ibm.labs.kc.model.events.ContainerOffMaintenanceEvent;
import ibm.labs.kc.model.events.ContainerOnMaintenanceEvent;
import ibm.labs.kc.model.events.OrderContainerAssignmentEvent;
import ibm.labs.kc.model.events.OrderSpoiltEvent;
import ibm.labs.kc.order.model.City;
import ibm.labs.kc.order.model.Order;

@Component
public class ContainerService {
	private static final Logger LOG = Logger.getLogger(ContainerService.class.toString());
	
	protected CityDAO cityDAO;
	protected ContainerDAO containerDAO;
	protected OrderProducer orderProducer;
	private ContainerProducer containerProducer;
		
	public ContainerService(CityDAO cityDAO, ContainerDAO containerDAO, OrderProducer orderProducer, ContainerProducer containerProducer) {
		this.cityDAO = cityDAO;
		this.containerDAO = containerDAO;
		this.orderProducer = orderProducer;
		this.containerProducer = containerProducer;
	}
	
	public List<ContainerOrder> assignContainerToOrder(Order order) {
		List<ContainerOrder> listOfContainersForOrder = new ArrayList<ContainerOrder>();
			
		// Get the city for the order's pickup address
		City city = this.cityDAO.getCity(order.getPickupAddress().getCity());
		if (city == null){
			LOG.severe("[ERROR] - Pickup address for order " + order.getOrderID() + " does not belong to any known city.");
			return listOfContainersForOrder;
		}
			
		int quantityToFill = order.getQuantity();
		// this is not the best implementation but as of now we have few containers so we can do that
		// a static query at the database will be better to assess status
		for (ContainerEntity ce : containerDAO.getAllContainers(city)) {				
			// In real life we should test the type of product ... 
			// Check if this container has space available
			if (ce.getStatus() == null || ContainerStatus.Empty.equals(ce.getStatus()) || ContainerStatus.PartiallyLoaded.equals(ce.getStatus())) {
				// Calculate available load for the container for the quantity to be sent in this order
				quantityToFill = manageCapacity(quantityToFill,ce);
				// Save ContainerEntity with new load
				containerDAO.save(ce);
				// Create new ContainerOrder to be sent to the order microservice
				ContainerOrder co = new ContainerOrder(ce.getId(), order.getOrderID());
				// Add the ContainerOrder to the list of containers used for this particular order
				listOfContainersForOrder.add(co);
				// Send the ContainerAllocated event for the order and microservice
				orderProducer.emit(new OrderContainerAssignmentEvent(co));
				// Send the ContainerAssignedToOrder event for order and container microservices
				containerProducer.emit(new ContainerAssignmentEvent(co));
			}
			if (quantityToFill <= 0) return listOfContainersForOrder;
		} 
		// still quantity to assign ?
		return listOfContainersForOrder;
	}

	public Boolean unAssignContainerToOrder(Order order) {
		if (!containerDAO.existsById(order.getContainerID())){
			LOG.severe("[ERROR] - Received OrderReject Event with orderID: " + order.getOrderID() + " and with containerID: " + order.getContainerID() + " but that containerID does not exist.");
			return false;
		}
		else {
			// Get the existing container
			ContainerEntity ce = containerDAO.getById(order.getContainerID());
			// Set the capacity to its intial capacity (i.e. remaining capacity + order quantity)
			ce.setCapacity(ce.getCapacity() + order.getQuantity());
			// Set the container status to empty so that it can be re-assigned
			ce.setStatus(ContainerStatus.Empty);
			// Update the container repository
			containerDAO.update(order.getContainerID(), ce);
			LOG.info("ContainerID: " + order.getContainerID() + " unassigned. Current capacity: " + ce.getCapacity() + ". Current status: " + ce.getStatus());
			return true;
		}
	}
		
	public int manageCapacity(int quantityToFill,ContainerEntity ce) {
		int currentCapa = ce.getCapacity();
		if ( ce.getCapacity() > quantityToFill) {  
			  ce.setCapacity(currentCapa-quantityToFill);
			  ce.setStatus(ContainerStatus.PartiallyLoaded);
			  return 0;		 
		  } else {
			  ce.setCapacity(0);
			  ce.setStatus(ContainerStatus.Loaded);
			  return quantityToFill - currentCapa;
		  }
	}

	public boolean setContainerToMaintenance(Container container){
		try {
			LOG.info("Sending ContainerOnMaintenance event for container " + container.getContainerID());
			ContainerEntity ce = containerDAO.getById(container.getContainerID());
			if (ce.getStatus() != ContainerStatus.InMaintenance){
				containerProducer.emit(new ContainerOnMaintenanceEvent(container));
				LOG.info("ContainerOnMaintenance event successfully sent");
			} else{
				LOG.info("[INFO] - The container " + container.getContainerID() + " is already in maintenance.");
			}
			return true;
		} catch(ResourceNotFoundException e){
			LOG.severe("[ERROR] - The container " + container.getContainerID() + " does not exist");
			return false;
		}
	}

	public boolean setContainerOffMaintenance(Container container){
		try {
			LOG.info("Sending ContainerOffMaintenance event for container " + container.getContainerID());
			ContainerEntity ce = containerDAO.getById(container.getContainerID());
			if (ce.getStatus() == ContainerStatus.InMaintenance){
				containerProducer.emit(new ContainerOffMaintenanceEvent(container));
				LOG.info("ContainerOffMaintenance event successfully sent");
				return true;
			} else{
				LOG.severe("[ERROR] - The container " + container.getContainerID() + " is not in maintenance.");
				return false;
			}
		} catch(ResourceNotFoundException e){
			LOG.severe("[ERROR] - The container " + container.getContainerID() + " does not exist");
			return false;
		}
	}
	
    public ContainerEntity createContainer(Container container) {
		LOG.info("Creating a new container from: " + container.toString());
		if (!containerDAO.existsById(container.getContainerID())) {
			ContainerEntity ce = new ContainerEntity(container);
			ce.setCurrentCity(cityDAO.getCityName(ce.getLatitude(),ce.getLongitude()));
			LOG.info("Container successfully created.");
			return containerDAO.save(ce);
		} else {
			LOG.severe("[ERROR] - There is already an existing container with id " + container.getContainerID() + ". A new container will not be created");
			return null;
		}
	}
	
	public ContainerEntity updateContainer(Container container){
		LOG.info("Updating container from: " + container.toString());
		try {
			ContainerEntity ce = new ContainerEntity(container);
			ce.setCurrentCity(cityDAO.getCityName(ce.getLatitude(),ce.getLongitude()));
			ContainerEntity ce_returned = containerDAO.update(ce.getId(), ce);
			LOG.info("Container successfully updated.");
			return ce_returned;
		} catch (ResourceNotFoundException e){
			LOG.severe("[ERROR] - " + e.getMessage());
			return null;
		}	
	}

	public void spoilOrders(String containerId, List<String> orders){
		LOG.info("Creating OrderSpoilt events for containerId: " + containerId);
		if (orders != null){
			for (String order: orders){
				ContainerOrder containerOrder = new ContainerOrder(containerId, order);
				orderProducer.emit(new OrderSpoiltEvent(containerOrder));
				LOG.info("OrderSpoilt event sent for containerId: " + containerId + " and orderId: " + order);
			}
		} else LOG.severe("[ERROR] - Order list for containerId: " + containerId + " is null");
	}
}
