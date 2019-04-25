package ibm.labs.kc.containermgr.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ibm.labs.kc.model.events.ContainerEvent;

@Component
public class ContainerProducerImpl implements ContainerProducer {

	@Value("${kcsolution.containers}")
    public String CONTAINERS_TOPIC;
	
	@Override
	public void emit(ContainerEvent co) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ContainerEvent> getEventsSent() {
		// TODO Auto-generated method stub
		return null;
	}

}
