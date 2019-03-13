package ibm.labs.kc.model.events;

import ibm.labs.kc.model.Container;

public class ContainerEvent {
	protected long timestamp;
	protected String type;
	protected String version;
	protected Container payload;
	
	public ContainerEvent() {}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Container getPayload() {
		return payload;
	}

	public void setPayload(Container payload) {
		this.payload = payload;
	}
	
}
