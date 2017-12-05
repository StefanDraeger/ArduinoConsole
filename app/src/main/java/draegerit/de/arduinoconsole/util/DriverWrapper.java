package draegerit.de.arduinoconsole.util;

public class DriverWrapper<T> {

    private T driver;
    private DriverType type;
    private boolean bonded = true;

    public DriverWrapper(T driver, DriverType type) {
        this.driver = driver;
        this.type = type;
    }

    public boolean isBonded() {
        return bonded;
    }

    public void setBonded(boolean bonded) {
        this.bonded = bonded;
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

    public enum DriverType {
        USB, BLUETOOTH,WIFI;
    }
}
