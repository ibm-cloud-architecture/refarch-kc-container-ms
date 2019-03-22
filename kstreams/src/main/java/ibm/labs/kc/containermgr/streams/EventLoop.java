package ibm.labs.kc.containermgr.streams;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class EventLoop implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// Initialize the Container consumer
		ContainerInventoryView cView = (ContainerInventoryView)ContainerInventoryView.instance();
		cView.start();
		ContainerOrderAssignment oAssignment = ContainerOrderAssignment.instance();
		oAssignment.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ContainerInventoryView cView = (ContainerInventoryView)ContainerInventoryView.instance();
		cView.stop();
		ContainerOrderAssignment oAssignment = ContainerOrderAssignment.instance();
		oAssignment.stop();
	}

}
