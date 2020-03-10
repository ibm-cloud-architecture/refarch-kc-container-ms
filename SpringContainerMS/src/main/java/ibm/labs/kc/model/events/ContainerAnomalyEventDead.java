package ibm.labs.kc.model.events;

public class ContainerAnomalyEventDead extends ContainerAnomalyEvent  {
	
	protected String reason;

	public ContainerAnomalyEventDead() {
		super();
		this.reason="No reason";
		this.setType(ContainerEvent.CONTAINER_ANOMALY_DEAD);
	}
	
	public ContainerAnomalyEventDead(ContainerAnomalyEvent cae, String reason) {
		super();
		this.setTimestamp(cae.getTimestamp());
		this.setType(ContainerEvent.CONTAINER_ANOMALY_DEAD);
		this.setVersion(cae.getVersion());
		this.setContainerID(cae.getContainerID());
		this.setPayload(cae.getPayload());
		this.reason=reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return this.reason;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerAnomalyEventDead) {
			ContainerAnomalyEventDead caed = (ContainerAnomalyEventDead)o;
			return (caed.getReason() == this.getReason() && caed.getType().equals(ContainerEvent.CONTAINER_ANOMALY_DEAD)
					&& caed.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
	public String toString(){
		return "{reason: " + this.reason + ", timestamp: " + this.getTimestamp() + ", Type: " + this.getType() + ", Version: " + this.getVersion() + ", containerID: " + this.getContainerID() + ", Payload: "  + this.getPayload().toString() + "}";
	}
}
