package ibm.labs.kc.model.events;

import ibm.labs.kc.order.model.Order;

public class OrderRejectedEvent extends OrderEvent<Order>{

	public OrderRejectedEvent(String type,String version, Order o) {
		super();
		this.setOrderID(o.getOrderID());
		this.setType(OrderEvent.TYPE_REJECTED);
		this.setVersion(version);
		this.payload = o;
	}

	public void setPayload(Order payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof OrderCreationEvent) {
			OrderCreationEvent e = (OrderCreationEvent)o;
			return (e.getType().equals(OrderEvent.TYPE_CREATED) 
					&& e.getOrderID().equals(this.getOrderID()));
		} else return false;
	}
	
}
