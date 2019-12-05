package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.ContainerOrder;

/*
 * Represent the payload for OrderEvent when an order is spoilt due to container anomaly
 */
public class OrderSpoiltEvent extends OrderEvent<ContainerOrder>{

	public OrderSpoiltEvent(String orderID, String containerID) {
			 super(orderID,OrderEvent.TYPE_SPOILT);
			 this.payload = new ContainerOrder(containerID,orderID);
	}

	public OrderSpoiltEvent(ContainerOrder co) {
		 super(co.getOrderID(),OrderEvent.TYPE_SPOILT);
		 this.payload = co;
	}

	 public String toString() {
		 return "OrderSpoiltEvent " + getTimestamp() + " " +  getOrderID() + " " + getPayload().getContainerID();
	 }
	 
	 
}
