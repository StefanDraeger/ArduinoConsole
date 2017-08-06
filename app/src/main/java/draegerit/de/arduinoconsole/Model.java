package draegerit.de.arduinoconsole;


import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import draegerit.de.arduinoconsole.connection.AbstractArduinoConnection;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.Message;

public class Model extends Observable {

    private DriverWrapper<?> driver;

    private List<Message> messages = new ArrayList<>();

    private boolean connected;

    private int configurePaneVisibility = View.GONE;

    public static Model SINGLETON;

    private boolean autoScroll;

    private AbstractArduinoConnection arduinoConnection;

    private Model() {

    }

    public static Model getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new Model();
        }
        return SINGLETON;
    }

    public DriverWrapper<?> getDriver() {
        return driver;
    }

    public void setDriver(DriverWrapper<?> driver) {
        this.driver = driver;
        setChanged();
        notifyObservers();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message.Type type, String message) {
        Message msg = new Message(type, message);
        getMessages().add(msg);
        setChanged();
        notifyObservers(msg);
    }

    public int getConfigurePaneVisibility() {
        return configurePaneVisibility;
    }

    public void setConfigurePaneVisibility(int configurePaneVisibility) {
        this.configurePaneVisibility = configurePaneVisibility;
    }

    public void setAutoScroll(boolean autoScroll) {
        this.autoScroll = autoScroll;
    }

    public boolean isAutoScroll() {
        return autoScroll;
    }

    public AbstractArduinoConnection getArduinoConnection() {
        return arduinoConnection;
    }

    public void setArduinoConnection(AbstractArduinoConnection arduinoConnection) {
        this.arduinoConnection = arduinoConnection;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        setChanged();
        notifyObservers();
    }

    public void updateDataAdapter() {
        setChanged();
        notifyObservers(ArduinoConsoleStatics.ActionCommand.UpdateUsbDevice);
    }

    public void updateBluetoothAdapter() {
        setChanged();
        notifyObservers(ArduinoConsoleStatics.ActionCommand.UpdateBluetoothAdapter);
    }
}
