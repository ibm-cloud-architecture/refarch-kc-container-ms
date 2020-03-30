package ibm.labs.kc.containermgr;

import java.util.ArrayList;
import java.util.List;

import ibm.labs.kc.containermgr.kafka.ContainerProducer;
import ibm.labs.kc.model.events.ContainerAssignmentEvent;
import ibm.labs.kc.model.events.ContainerEvent;

public class ContainerProducerMockup implements ContainerProducer {
	List<ContainerEvent> eventsSent = new ArrayList<ContainerEvent>();
	
	@Override
	public void emit(ContainerEvent co) {
		ContainerAssignmentEvent cae = (ContainerAssignmentEvent)co;
		System.out.println("Emit container event " + cae.toString());
		eventsSent.add(cae);
	}

	@Override
	public List<ContainerEvent> getEventsSent() {
		return eventsSent;
	}

}
