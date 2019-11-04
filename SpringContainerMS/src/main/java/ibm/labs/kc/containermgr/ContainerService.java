package ibm.labs.kc.containermgr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ibm.labs.kc.containermgr.dao.CityDAO;
import ibm.labs.kc.containermgr.dao.ContainerDAO;
import ibm.labs.kc.containermgr.kafka.ContainerProducer;
import ibm.labs.kc.containermgr.kafka.OrderProducer;
import ibm.labs.kc.containermgr.model.ContainerEntity;
import ibm.labs.kc.containermgr.model.ContainerStatus;
import ibm.labs.kc.model.container.ContainerOrder;
import ibm.labs.kc.model.events.ContainerAssignmentEvent;
import ibm.labs.kc.model.events.OrderContainerAssignmentEvent;
import ibm.labs.kc.order.model.City;
import ibm.labs.kc.order.model.Order;

@Component
public class ContainerService {
	
		protected CityDAO cityDAO;
		protected ContainerDAO containerDAO;
		protected OrderProducer orderProducer;
		private ContainerProducer containerProducer;
		
		public ContainerService(CityDAO cityDAO,
				ContainerDAO containerDAO,
				OrderProducer orderProducer,
				ContainerProducer containerProducer) {
			this.cityDAO = cityDAO;
			this.containerDAO = containerDAO;
			this.orderProducer = orderProducer;
			this.containerProducer = containerProducer;
		}
	
		public List<ContainerOrder> assignContainerToOrder(Order order) {
			List<ContainerOrder> l = new ArrayList<ContainerOrder>();
			City city = this.cityDAO.getCity(order.getPickupAddress().getCity());
			if (city == null) return l;
			
			int quantityToFill = order.getQuantity();
			// this is not the best implementation but as of now we have few containers so we can do that
			// a static query at the database will be better to assess status
			for (ContainerEntity ce : containerDAO.getAllContainers(city)) {				
				// In real life we should test the type of product ... 
				if (ce.getStatus() == null 
					|| ContainerStatus.Empty.equals(ce.getStatus())
					|| ContainerStatus.PartiallyLoaded.equals(ce.getStatus())
						) {
							quantityToFill = manageCapacity(quantityToFill,ce);
							ContainerOrder co = new ContainerOrder(ce.getId(), order.getOrderID());
							l.add(co);
							containerDAO.save(ce);
							
							orderProducer.emit(new OrderContainerAssignmentEvent(co));
							containerProducer.emit(new ContainerAssignmentEvent(co));
				} // candidate container
				if (quantityToFill <= 0) return l;
			} 
			// still quantity to assign ?
			return l;
	}
		
	public int manageCapacity(int quantityToFill,ContainerEntity ce) {
		int currentCapa = ce.getCapacity();
		if ( ce.getCapacity() > quantityToFill) {  
			  ce.setCapacity(currentCapa-quantityToFill);
			  ce.setStatus(ContainerStatus.PartiallyLoaded);
			  return 0;		 
		  } else {
			  ce.setCapacity(0);
			  ce.setStatus(ContainerStatus.Loaded);
			  return quantityToFill - currentCapa;
		  }
	}
}
