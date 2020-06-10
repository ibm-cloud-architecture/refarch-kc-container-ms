package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.Container;

public class ContainerOnMaintenanceEvent extends ContainerEvent<Container>  {
	
	
	public ContainerOnMaintenanceEvent() {
		super();
		this.setType(ContainerEvent.CONTAINER_ON_MAINTENANCE);
	}
	
	public ContainerOnMaintenanceEvent(Container p) {
		super();
		this.setContainerID(p.getContainerID());
		this.setType(ContainerEvent.CONTAINER_ON_MAINTENANCE);
		this.payload = p;
	}

	public void setPayload(Container payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerOnMaintenanceEvent) {
			ContainerOnMaintenanceEvent e = (ContainerOnMaintenanceEvent)o;
			return (e.getType().equals(ContainerEvent.CONTAINER_ON_MAINTENANCE) 
					&& e.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
}
