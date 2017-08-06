package draegerit.de.arduinoconsole.util.configuration;

public class GeneralConfiguration {

    private boolean stayAwakeWhileConnected;

    public GeneralConfiguration(boolean stayAwakeWhileConnected) {
        this.stayAwakeWhileConnected = stayAwakeWhileConnected;
    }

    public boolean isStayAwakeWhileConnected() {
        return stayAwakeWhileConnected;
    }

    public void setStayAwakeWhileConnected(boolean stayAwakeWhileConnected) {
        this.stayAwakeWhileConnected = stayAwakeWhileConnected;
    }
}
