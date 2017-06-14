package draegerit.de.arduinoconsole.util;

public class DriverWrapper<T> {

    public enum DriverType {
        USB, BLUETOOTH;
    }

    private T driver;

    private DriverType type;

    public DriverWrapper(T driver, DriverType type) {
        this.driver = driver;
        this.type = type;
    }

    public T getDriver() {
        return driver;
    }

    public void setDriver(T driver) {
        this.driver = driver;
    }

    public DriverType getType() {
        return type;
    }

    public void setType(DriverType type) {
        this.type = type;
    }
}
