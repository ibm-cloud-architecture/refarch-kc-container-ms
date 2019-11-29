package ibm.labs.kc.model.events;

import java.util.Date;

public class OrderEvent<T> extends AbstractEvent {

    public static final String TYPE_CREATED = "OrderCreated";
    public static final String TYPE_UPDATED = "OrderUpdated";
    public static final String TYPE_BOOKED = "OrderBooked";
    public static final String TYPE_ASSIGNED = "OrderAssigned"; // from voyage ms
    public static final String TYPE_TRANSIT = "OrderInTransit";
    public static final String TYPE_COMPLETED = "OrderCompleted";
    public static final String TYPE_REJECTED = "OrderRejected";
    public static final String TYPE_CANCELLED = "OrderCancelled";
    public static final String TYPE_SPOILT = "OrderSpoilt"; // from containers ms
   
    public static final String TYPE_CONTAINER_ALLOCATED = "ContainerAllocated";
    public static final String TYPE_FULL_CONTAINER_VOYAGE_READY = "FullContainerVoyageReady";
    public static final String TYPE_CONTAINER_ON_SHIP = "ContainerOnShip";
    public static final String TYPE_CONTAINER_OFF_SHIP = "ContainerOffShip";
    public static final String TYPE_CONTAINER_DELIVERED = "ContainerDelivered";
     
    protected String orderID;
    protected T payload;

    public OrderEvent(long timestampMillis, String type, String version,T o) {
        super(timestampMillis, type, version);
        this.payload = o;
    }

    public OrderEvent() {}

	public OrderEvent(String orderID,String type) {
		this.orderID = orderID;
		this.type = type;
		this.timestamp = (new Date()).getTime();
	}
	
	@Override
	public T getPayload() {
		return this.payload;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
