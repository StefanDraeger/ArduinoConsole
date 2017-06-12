package draegerit.de.arduinoconsole;


import android.app.PendingIntent;
import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;
import android.view.View;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import draegerit.de.arduinoconsole.connection.AbstractArduinoConnection;
import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.Message;
import draegerit.de.arduinoconsole.util.USBConfiguration;

public class Model extends Observable {

    private UsbSerialDriver driver;

    private UsbSerialPort port;

    private UsbDeviceConnection connection;

    private PendingIntent permissionIntent;

    private List<Message> messages = new ArrayList<>();

    private List<UsbSerialDriver> usbSerialDrivers;

    private boolean isConnected;

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


    public List<UsbSerialDriver> getUsbSerialDrivers() {
        return usbSerialDrivers;
    }

    public void setUsbSerialDrivers(List<UsbSerialDriver> usbSerialDrivers) {
        this.usbSerialDrivers = usbSerialDrivers;
        setChanged();
        notifyObservers(ArduinoConsoleStatics.ActionCommand.UpdateUsbDevice);
    }

    public UsbSerialDriver getDriver() {
        return driver;
    }

    public void setDriver(UsbSerialDriver driver) {
        this.driver = driver;
        setChanged();
        notifyObservers();
    }

    public UsbSerialPort getPort() {
        return port;
    }

    public void setPort(UsbSerialPort port) {
        this.port = port;
    }

    public UsbDeviceConnection getConnection() {
        return connection;
    }

    public void setConnection(UsbDeviceConnection connection) {
        this.connection = connection;
    }

    public PendingIntent getPermissionIntent() {
        return permissionIntent;
    }

    public void setPermissionIntent(PendingIntent mPermissionIntent) {
        this.permissionIntent = mPermissionIntent;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        setChanged();
        notifyObservers(ArduinoConsoleStatics.ActionCommand.ChangeConnectionStatus);
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
}
