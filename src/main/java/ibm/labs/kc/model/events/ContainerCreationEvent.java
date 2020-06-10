package ibm.labs.kc.model.events;

import ibm.labs.kc.model.container.Container;

public class ContainerCreationEvent extends ContainerEvent<Container>  {
	
	
	public ContainerCreationEvent() {
		super();
		this.setType(ContainerEvent.CONTAINER_ADDED);
	}
	
	public ContainerCreationEvent(String type,String version, Container p) {
		super();
		this.setContainerID(p.getContainerID());
		this.setType(ContainerEvent.CONTAINER_ADDED);
		this.setVersion(version);
		this.payload = p;
	}

	public void setPayload(Container payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerCreationEvent) {
			ContainerCreationEvent e = (ContainerCreationEvent)o;
			return (e.getType().equals(ContainerEvent.CONTAINER_ADDED) 
					&& e.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
}
