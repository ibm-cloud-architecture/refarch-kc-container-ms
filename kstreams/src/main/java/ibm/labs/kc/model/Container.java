package ibm.labs.kc.model;

public class Container {
	
	private String containerID; 
	private double latitude;
	private double longitude;
	private String type;
	private String status;
	private String brand;
	
	public Container(String cid, String type, double lat, double lo) {
		this.containerID = cid;
		this.type = type;
		this.latitude = lat;
		this.longitude = lo;
	}

	public String getContainerID() {
		return containerID;
	}

	public void setContainerID(String containerID) {
		this.containerID = containerID;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
}
