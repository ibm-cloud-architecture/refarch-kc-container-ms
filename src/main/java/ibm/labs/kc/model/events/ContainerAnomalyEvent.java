package ibm.labs.kc.model.events;

import java.util.HashMap;
import java.util.Map;

import ibm.labs.kc.model.container.ContainerSpec;

public class ContainerAnomalyEvent extends ContainerEvent<ContainerSpec>  {
	
	
	public ContainerAnomalyEvent() {
		super();
		this.setType(ContainerEvent.CONTAINER_ANOMALY);
	}
	
	public ContainerAnomalyEvent(String type,String version, ContainerEvent<ContainerSpec> ce) {
		super();
		this.setContainerID(ce.getContainerID());
		this.setType(ContainerEvent.CONTAINER_ANOMALY);
		this.setVersion(version);
		this.setPayload(ce.getPayload());
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
		map.put("containerID",this.getContainerID());
		map.put("temperature",String.valueOf(this.getPayload().getTemperature()));
		map.put("targetTemperature",String.valueOf(this.getPayload().getTarget_temperature()));
		map.put("ambientTemperature",String.valueOf(this.getPayload().getAmbiant_temperature()));
		map.put("kilowatts",String.valueOf(this.getPayload().getKilowatts()));
		map.put("contentType",String.valueOf(this.getPayload().getContent_type()));
		map.put("oxygenLevel",String.valueOf(this.getPayload().getOxygen_level()));
		map.put("nitrogenLevel",String.valueOf(this.getPayload().getNitrogen_level()));
		map.put("carbonDioxideLevel",String.valueOf(this.getPayload().getCarbon_dioxide_level()));
		map.put("humidityLevel",String.valueOf(this.getPayload().getHumidity_level()));
		map.put("latitude",String.valueOf(this.getPayload().getLatitude()));
		map.put("longitude",String.valueOf(this.getPayload().getLongitude()));
		map.put("vent1",String.valueOf(this.getPayload().isVent_1()));
		map.put("vent2",String.valueOf(this.getPayload().isVent_2()));
		map.put("vent3",String.valueOf(this.getPayload().isVent_3()));
		map.put("timeDoorOpen",String.valueOf(this.getPayload().getTime_door_open()));
		map.put("defrostCycle",String.valueOf(this.getPayload().getDefrost_cycle()));
		return map;
	}
}
