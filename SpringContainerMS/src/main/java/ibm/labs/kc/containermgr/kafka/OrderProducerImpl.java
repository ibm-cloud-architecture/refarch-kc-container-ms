package ibm.labs.kc.containermgr.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ibm.labs.kc.model.events.OrderEvent;

@Component
public class OrderProducerImpl implements OrderProducer {
	@Value("${kcsolution.orders}")
    public  String ORDERS_TOPIC;
	
	@Override
	public void emit(OrderEvent co) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<OrderEvent> getEventsSent() {
		// TODO Auto-generated method stub
		return null;
	}

}
