package ibm.labs.kc.model.container;

public  class ContainerOrder {
	protected String containerID;
	protected String orderID; 
	
	public ContainerOrder() {}
	
	public ContainerOrder(String cid, String oid) {
		this.containerID = cid;
		this.orderID = oid;
	}
	
	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}
	
	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderId) {
		this.orderID = orderId;
	}
}	 
