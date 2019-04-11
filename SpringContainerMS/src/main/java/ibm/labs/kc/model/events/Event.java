package ibm.labs.kc.model.events;

public interface Event {

    public long getTimestamp();

    public void setTimestamp(long timestampMillis);

    public String getType();

    public void setType(String type);

    public void setVersion(String version);

    public String getVersion();

    public Object getPayload();
}
