package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.Container;

public class ContainerOffMaintenanceEvent extends ContainerEvent<Container>  {
	
	
	public ContainerOffMaintenanceEvent() {
		super();
	}
	
	public ContainerOffMaintenanceEvent(Container p) {
		super();
		this.setContainerID(p.getContainerID());
		this.setType(ContainerEvent.CONTAINER_OFF_MAINTENANCE);
		this.payload = p;
	}

	public void setPayload(Container payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerOffMaintenanceEvent) {
			ContainerOffMaintenanceEvent e = (ContainerOffMaintenanceEvent)o;
			return (e.getType().equals(ContainerEvent.CONTAINER_OFF_MAINTENANCE) 
					&& e.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
}
