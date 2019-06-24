package ibm.labs.kc.model.events;

import java.util.Date;

public class ContainerEvent<T> extends AbstractEvent {

	// those are the list of event types
	public static final String CONTAINER_ADDED = "ContainerAdded";
	public static final String CONTAINER_REMOVED = "ContainerRemoved";
	public static final String CONTAINER_AT_LOCATION = "ContainerAtLocation";
	public static final String CONTAINER_ON_MAINTENANCE = "ContainerOnMaintenance";
	public static final String CONTAINER_OFF_MAINTENANCE =  "ContainerOffMaintenance";
	public static final String CONTAINER_ORDER_ASSIGNED = "ContainerAssignedToOrder";
	public static final String CONTAINER_ORDER_RELEASED = "ContainerReleasedFromOrder";
	public static final String CONTAINER_GOOD_LOADED = "ContainerGoodLoaded";
	public static final String CONTAINER_GOOD_UNLOADED = "ContainerGoodUnLoaded";
	public static final String CONTAINER_ON_SHIP = "ContainerOnShip";
	public static final String CONTAINER_OFF_SHIP = "ContainerOffShip";
	public static final String CONTAINER_ON_TRUCK = "ContainerOnTruck";
	public static final String CONTAINER_OFF_TRUCK = "ContainerOffTruck";
	
	protected String containerID;
	protected T payload;

	public ContainerEvent() {
		this.timestamp = (new Date()).getTime();
	}
	
	public ContainerEvent(String cid) {
		this.timestamp = (new Date()).getTime();
		this.containerID = cid;
	}
	
	public ContainerEvent(String cid,String type) {
		this.containerID = cid;
		this.type = type;
		this.timestamp = (new Date()).getTime();
	}
	
	
	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}

	@Override
	public T getPayload() {
		return this.payload;
	}

}
