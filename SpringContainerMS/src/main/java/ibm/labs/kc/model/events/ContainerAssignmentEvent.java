package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.ContainerOrder;

/*
 * This represents the payload for an OrderEvent and ContainerEvent
 */
public class ContainerAssignmentEvent extends ContainerEvent<ContainerOrder> {
	 
	 
	 public ContainerAssignmentEvent(String oid, String cid) {
		 super();
		 this.payload = new ContainerOrder(cid,oid);
		 this.containerID = cid;
		 this.type = ContainerEvent.CONTAINER_ORDER_ASSIGNED;
	 }
	 
	 public ContainerAssignmentEvent(String oid, String cid, String type) {
		 super();	 
		 this.containerID = cid;
		 this.type = type;
	 }
	 
	 public String toString() {
		 return getOrderID() + " " + getContainerID();
	 }

	public String getOrderID() {
		return getPayload().getOrderID();
	}

	public void setOrderID(String orderID) {
		getPayload().setOrderID(orderID); 
	}

	
}
