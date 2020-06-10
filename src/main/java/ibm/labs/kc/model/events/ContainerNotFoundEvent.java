package ibm.labs.kc.model.events;

import ibm.labs.kc.order.model.ContainerNotFoundPayload;

/*
 * Represent the payload for OrderEvent when an order is rejected
 */
public class ContainerNotFoundEvent extends OrderEvent<ContainerNotFoundPayload>{

	public ContainerNotFoundEvent(String orderID, String reason) {
			 super(orderID,OrderEvent.TYPE_CONTAINER_NOT_FOUND);
			 this.payload = new ContainerNotFoundPayload(orderID, reason);
	}

	public ContainerNotFoundEvent(String orderID, ContainerNotFoundPayload cnf) {
		 super(orderID,OrderEvent.TYPE_CONTAINER_NOT_FOUND);
		 this.payload = cnf;
	}
}
