package ibm.labs.kc.containermgr;

import java.util.ArrayList;
import java.util.List;

import ibm.labs.kc.containermgr.kafka.OrderProducer;
import ibm.labs.kc.model.events.OrderContainerAssignmentEvent;
import ibm.labs.kc.model.events.OrderEvent;

public class OrderProducerMockup implements OrderProducer {
	List<OrderEvent> eventsSent = new ArrayList<OrderEvent>();
	
	@Override
	public void emit(OrderEvent co) {
		OrderContainerAssignmentEvent ocae = (OrderContainerAssignmentEvent)co;
		System.out.println("Emit order event " + ocae.toString());
		eventsSent.add(ocae);
	}

	@Override
	public List<OrderEvent> getEventsSent() {
		return eventsSent;
	}

}
