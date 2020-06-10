package ibm.labs.kc.model.container;

public class ContainerSpec {
	
	protected double temperature;
	protected double target_temperature;
	protected double ambiant_temperature;
	protected double kilowatts;
	protected int content_type;
	protected double oxygen_level;
	protected double nitrogen_level;
	protected double carbon_dioxide_level;
	protected double humidity_level;
	protected double latitude;
	protected double longitude;
	protected boolean vent_1;
	protected boolean vent_2;
	protected boolean vent_3;
	protected double time_door_open;
	protected int defrost_cycle;




	public ContainerSpec(double temperature, double target_temperature, double ambiant_temperature, double kilowatts, int content_type, double oxygen_level, double nitrogen_level, double carbon_dioxide_level, double humidity_level, double latitude, double longitude, boolean vent_1, boolean vent_2, boolean vent_3, double time_door_open, int defrost_cycle) {
		this.temperature = temperature;
		this.target_temperature = target_temperature;
		this.ambiant_temperature = ambiant_temperature;
		this.kilowatts = kilowatts;
		this.content_type = content_type;
		this.oxygen_level = oxygen_level;
		this.nitrogen_level = nitrogen_level;
		this.carbon_dioxide_level = carbon_dioxide_level;
		this.humidity_level = humidity_level;
		this.latitude = latitude;
		this.longitude = longitude;
		this.vent_1 = vent_1;
		this.vent_2 = vent_2;
		this.vent_3 = vent_3;
		this.time_door_open = time_door_open;
		this.defrost_cycle = defrost_cycle;
	}
	
	// need default constructor for jackson deserialization
	public ContainerSpec() {}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getTarget_temperature() {
		return target_temperature;
	}

	public void setTarget_temperature(double target_temperature) {
		this.target_temperature = target_temperature;
	}

	public double getAmbiant_temperature() {
		return ambiant_temperature;
	}

	public void setAmbiant_temperature(double ambiant_temperature) {
		this.ambiant_temperature = ambiant_temperature;
	}

	public double getKilowatts() {
		return kilowatts;
	}

	public void setKilowatts(double kilowatts) {
		this.kilowatts = kilowatts;
	}

	public int getContent_type() {
		return content_type;
	}

	public void setContent_type(int content_type) {
		this.content_type = content_type;
	}

	public double getOxygen_level() {
		return oxygen_level;
	}

	public void setOxygen_level(double oxygen_level) {
		this.oxygen_level = oxygen_level;
	}

	public double getNitrogen_level() {
		return nitrogen_level;
	}

	public void setNitrogen_level(double nitrogen_level) {
		this.nitrogen_level = nitrogen_level;
	}

	public double getCarbon_dioxide_level() {
		return carbon_dioxide_level;
	}

	public void setCarbon_dioxide_level(double carbon_dioxide_level) {
		this.carbon_dioxide_level = carbon_dioxide_level;
	}

	public double getHumidity_level() {
		return humidity_level;
	}

	public void setHumidity_level(double humidity_level) {
		this.humidity_level = humidity_level;
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

	public boolean isVent_1() {
		return vent_1;
	}

	public void setVent_1(boolean vent_1) {
		this.vent_1 = vent_1;
	}

	public boolean isVent_2() {
		return vent_2;
	}

	public void setVent_2(boolean vent_2) {
		this.vent_2 = vent_2;
	}

	public boolean isVent_3() {
		return vent_3;
	}

	public void setVent_3(boolean vent_3) {
		this.vent_3 = vent_3;
	}

	public double getTime_door_open() {
		return time_door_open;
	}

	public void setTime_door_open(double time_door_open) {
		this.time_door_open = time_door_open;
	}

	public int getDefrost_cycle() {
		return defrost_cycle;
	}

	public void setDefrost_cycle(int defrost_cycle) {
		this.defrost_cycle = defrost_cycle;
	}

	@Override
	public String toString() {
		return "{temperature:" + temperature + ", target_temperature:" + target_temperature
				+ ", ambiant_temperature:" + ambiant_temperature + ", kilowatts:" + kilowatts + ", content_type:"
				+ content_type + ", oxygen_level:" + oxygen_level + ", nitrogen_level:" + nitrogen_level
				+ ", carbon_dioxide_level:" + carbon_dioxide_level + ", humidity_level:" + humidity_level
				+ ", latitude:" + latitude + ", longitude:" + longitude + ", vent_1:" + vent_1 + ", vent_2:" + vent_2
				+ ", vent_3:" + vent_3 + ", time_door_open:" + time_door_open + ", defrost_cycle:" + defrost_cycle
				+ "}";
	}

}
