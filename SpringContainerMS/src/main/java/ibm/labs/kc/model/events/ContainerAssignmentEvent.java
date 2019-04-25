package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.ContainerOrder;

/*
 * Represent the payload for ContainerEvent when a container is assigned successfully to an order
 */
public class ContainerAssignmentEvent extends ContainerEvent<ContainerOrder> {
	 
	 
	 public ContainerAssignmentEvent(String oid, String cid) {
		 super(cid,ContainerEvent.CONTAINER_ORDER_ASSIGNED);
		 this.payload = new ContainerOrder(cid,oid);
	 }
	 
	 public ContainerAssignmentEvent(String oid, String cid, String type) {
		 super(cid,type);
		 this.payload = new ContainerOrder(cid,oid);
	 }
	 
	 public ContainerAssignmentEvent(ContainerOrder co) {
		 super(co.getContainerID(),ContainerEvent.CONTAINER_ORDER_ASSIGNED);
		 this.payload = co;
	 }
	 
	 public String toString() {
		 return "ContainerAssignmentEvent " + getTimestamp() + " " +  getOrderID() + " " + getContainerID();
	 }

	public String getOrderID() {
		return getPayload().getOrderID();
	}

	public void setOrderID(String orderID) {
		getPayload().setOrderID(orderID); 
	}

	
}
