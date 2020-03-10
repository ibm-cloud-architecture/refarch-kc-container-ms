package ibm.labs.kc.model.events;

public class ContainerAnomalyEventRetry extends ContainerAnomalyEvent  {
	
	protected int retries;

	public ContainerAnomalyEventRetry() {
		super();
		this.retries=0;
		this.setType(ContainerEvent.CONTAINER_ANOMALY_RETRY);
	}
	
	public ContainerAnomalyEventRetry(ContainerAnomalyEvent cae, int retries) {
		super();
		this.setTimestamp(cae.getTimestamp());
		this.setType(ContainerEvent.CONTAINER_ANOMALY_RETRY);
		this.setVersion(cae.getVersion());
		this.setContainerID(cae.getContainerID());
		this.setPayload(cae.getPayload());
		this.retries=retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getRetries() {
		return this.retries;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerAnomalyEventRetry) {
			ContainerAnomalyEventRetry caer = (ContainerAnomalyEventRetry)o;
			return (caer.getRetries() == this.getRetries() && caer.getType().equals(ContainerEvent.CONTAINER_ANOMALY_RETRY)
					&& caer.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
	public String toString(){
		return "{retries: " + this.retries + ", timestamp: " + this.getTimestamp() + ", Type: " + this.getType() + ", Version: " + this.getVersion() + ", containerID: " + this.getContainerID() + ", Payload: "  + this.getPayload().toString() + "}";
	}
}
