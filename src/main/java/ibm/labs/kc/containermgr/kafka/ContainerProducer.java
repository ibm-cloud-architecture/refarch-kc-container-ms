package ibm.labs.kc.containermgr.kafka;

import java.util.List;

import ibm.labs.kc.model.events.ContainerEvent;

public interface ContainerProducer {

	
	public void emit(ContainerEvent co);

	public List<ContainerEvent> getEventsSent();
}
