package ibm.labs.kc.model.events;

public abstract class AbstractEvent implements Event {

    protected long timestamp;
    protected String type;
    protected String version;
  
    
    public AbstractEvent() {
    }

    public AbstractEvent(long timestampMillis, String type, String version) {
        this.timestamp = timestampMillis;
        this.type = type;
        this.version = version;
    }

    
    public long getTimestamp() {
        return timestamp;
    }

    
    public void setTimestamp(long timestampMillis) {
        this.timestamp = timestampMillis;
    }

    
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }

    
    public void setVersion(String version) {
        this.version = version;
    }

    
    public String getVersion() {
        return version;
    }

}
