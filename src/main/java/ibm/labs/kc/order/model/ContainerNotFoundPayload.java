package ibm.labs.kc.order.model;

public  class ContainerNotFoundPayload {
    protected String orderID;
    protected String reason;
	
	public ContainerNotFoundPayload() {}
	
	public ContainerNotFoundPayload(String oid, String reason) {
        this.orderID = oid;
        this.reason=reason;
	}
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderId) {
		this.orderID = orderId;
	}
}	 
