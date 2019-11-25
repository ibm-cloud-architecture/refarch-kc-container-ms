package ibm.labs.kc.model.events;

import java.util.HashMap;
import java.util.Map;

import ibm.labs.kc.model.container.ContainerSpec;

public class ContainerAnomalyEvent extends ContainerEvent<ContainerSpec>  {
	
	
	public ContainerAnomalyEvent() {
		super();
	}
	
	public ContainerAnomalyEvent(String type,String version, ContainerSpec p) {
		super();
		//this.setContainerID(p.getContainerID());
		this.setType(ContainerEvent.CONTAINER_ANOMALY);
		this.setVersion(version);
		this.payload = p;
	}

	public void setPayload(ContainerSpec payload) {
		this.payload = payload;
	}

	@Override
	public boolean equals(Object o) {
		if ( o == null) return false;
		if (o instanceof ContainerAnomalyEvent) {
			ContainerAnomalyEvent e = (ContainerAnomalyEvent)o;
			return (e.getType().equals(ContainerEvent.CONTAINER_ANOMALY) 
					&& e.getContainerID().equals(this.getContainerID()));
		} else return false;
	}
	
	public String toString(){
		return "{timestamp: " + this.getTimestamp() + ", Type: " + this.getType() + ", Version: " + this.getVersion() + ", containerID: " + this.getContainerID() + ", Payload: "  + this.getPayload().toString() + "}";
	}

	public Map<String,String> getBPMMessage(){
		Map<String, String> map = new HashMap<>();
		map.put("ContainerID",this.getContainerID());
		map.put("temperature",String.valueOf(this.getPayload().getTemperature()));
		map.put("target_temperature",String.valueOf(this.getPayload().getTarget_temperature()));
		map.put("ambiant_temperature",String.valueOf(this.getPayload().getAmbiant_temperature()));
		map.put("kilowatts",String.valueOf(this.getPayload().getKilowatts()));
		map.put("content_type",String.valueOf(this.getPayload().getContent_type()));
		map.put("oxygen_level",String.valueOf(this.getPayload().getOxygen_level()));
		map.put("nitrogen_level",String.valueOf(this.getPayload().getNitrogen_level()));
		map.put("carbon_dioxide_level",String.valueOf(this.getPayload().getCarbon_dioxide_level()));
		map.put("humidity_level",String.valueOf(this.getPayload().getHumidity_level()));
		map.put("latitude",String.valueOf(this.getPayload().getLatitude()));
		map.put("longitude",String.valueOf(this.getPayload().getLongitude()));
		map.put("vent_1",String.valueOf(this.getPayload().isVent_1()));
		map.put("vent_2",String.valueOf(this.getPayload().isVent_2()));
		map.put("vent_3",String.valueOf(this.getPayload().isVent_3()));
		map.put("time_door_open",String.valueOf(this.getPayload().getTime_door_open()));
		map.put("defrost_cycle",String.valueOf(this.getPayload().getDefrost_cycle()));
		return map;
	}
}
