package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.ContainerOrder;

/*
 * Represent the payload for OrderEvent when a container is assigned successfully to an order
 */
public class OrderContainerAssignmentEvent extends OrderEvent<ContainerOrder>{

	public OrderContainerAssignmentEvent(String orderID, String containerID) {
			 super(orderID,OrderEvent.TYPE_CONTAINER_ALLOCATED);
			 this.payload = new ContainerOrder(containerID,orderID);
	}

	public OrderContainerAssignmentEvent(ContainerOrder co) {
		 super(co.getOrderID(),OrderEvent.TYPE_CONTAINER_ALLOCATED);
		 this.payload = co;
	}

	 public String toString() {
		 return "OrderContainerAssignmentEvent " + getTimestamp() + " " +  getOrderID() + " " + getPayload().getContainerID();
	 }
	 
	 
}
