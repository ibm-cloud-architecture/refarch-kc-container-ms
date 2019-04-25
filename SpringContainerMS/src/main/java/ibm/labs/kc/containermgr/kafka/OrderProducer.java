package ibm.labs.kc.containermgr.kafka;

import java.util.List;

import ibm.labs.kc.model.events.OrderEvent;

/**
 * Produce order and container assignment event
 * @author jeromeboyer
 *
 */
public interface OrderProducer {

	public void emit(OrderEvent co);

	public List<OrderEvent> getEventsSent();
}
