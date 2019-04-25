package ibm.labs.kc.containermgr.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ibm.labs.kc.model.container.Container;

@Entity
@Table(name = "containers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdAt", "updatedAt"},
        allowGetters = true
)
public class ContainerEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 2113526845183839138L;
	@Id
	protected String id;
	protected double latitude;
	protected double longitude;
	protected String type;
	protected ContainerStatus status;
	protected String brand;
	protected String currentCity;
	protected int capacity;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Date updatedAt;
	
    public ContainerEntity() {
    	this.status = ContainerStatus.Empty;
    }
    
	public ContainerEntity(String cid, String brand, String type, int capacity, double lat, double lo,String city) {
		this.id = cid;
		this.type = type;
		this.latitude = lat;
		this.brand = brand;
		this.capacity = capacity;
		this.longitude = lo;
		this.currentCity = city;
		this.status = ContainerStatus.Empty;
	}

	public ContainerEntity(Container container) {
		this.id = container.getContainerID();
		this.type = container.getType();
		this.latitude = container.getLatitude();
		this.brand = container.getBrand();
		this.capacity = container.getCapacity();
		this.longitude = container.getLongitude();
		try {
			this.status = ContainerStatus.valueOf(container.getStatus());
		} catch (Exception e) {
			this.status = ContainerStatus.Empty;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ContainerStatus getStatus() {
		return status;
	}

	public void setStatus(ContainerStatus status) {
		this.status = status;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
