package ibm.labs.kc.model.events;

public class ContainerAnomalyEvent extends ContainerEvent<String>  {
	
	
	public ContainerAnomalyEvent() {
		super();
	}
	
	public ContainerAnomalyEvent(String type,String version, String p) {
		super();
		//this.setContainerID(p.getContainerID());
		this.setType(ContainerEvent.CONTAINER_ANOMALY);
		this.setVersion(version);
		this.payload = p;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerAnomalyEvent) {
			ContainerAnomalyEvent e = (ContainerAnomalyEvent)o;
			return (e.getType().equals(ContainerEvent.CONTAINER_ANOMALY) 
					&& e.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
	public String toString(){
		return "{timestamp: " + this.getTimestamp() + ", Type: " + this.getType() + ", Version: " + this.getVersion() + ", containerID: " + this.getContainerID() + ", Payload: "  + this.getPayload() + "}";
	}
}
