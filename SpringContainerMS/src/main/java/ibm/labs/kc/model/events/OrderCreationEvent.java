package ibm.labs.kc.model.events;

import ibm.labs.kc.model.Order;

public class OrderCreationEvent extends OrderEvent<Order>{

	public OrderCreationEvent(String type,String version, Order o) {
		super();
		this.setOrderID(o.getOrderID());
		this.setType(OrderEvent.TYPE_CREATED);
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
