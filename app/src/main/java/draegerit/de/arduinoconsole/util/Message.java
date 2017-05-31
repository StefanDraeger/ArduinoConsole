package draegerit.de.arduinoconsole.util;

public class Message {

    public enum Type {
        FROM, TO;
    }

    private Type type;

    private String value;

    private long timestamp;

    public Message(Type type, String value) {
        this.type = type;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
