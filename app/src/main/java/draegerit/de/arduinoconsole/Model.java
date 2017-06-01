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

import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.Message;

public class Model extends Observable {

    private static final String TAG = "ArduinoConsole";

    private int baudrate = 9600;

    private UsbSerialDriver driver;

    private UsbSerialPort port;

    private UsbDeviceConnection connection;

    private PendingIntent permissionIntent;

    private List<Message> messages = new ArrayList<>();
    //private StringBuffer messages = new StringBuffer();

    private List<UsbSerialDriver> usbSerialDrivers;

    private boolean isConnected;

    private int databits = 8;

    private int stopbits = 1;

    private EParity parity = EParity.NONE;

    private int configurePaneVisibility = View.GONE;

    public static Model SINGLETON;
    private boolean autoScroll;

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

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
        setChanged();
        notifyObservers();
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

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        setChanged();
        notifyObservers();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        setChanged();
        notifyObservers(ArduinoConsoleStatics.ActionCommand.ChangeConnectionStatus);
    }

    public int getDatabits() {
        return databits;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public int getStopbits() {
        return stopbits;
    }

    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    public EParity getParity() {
        return parity;
    }

    public void setParity(EParity parity) {
        this.parity = parity;
        setChanged();
        notifyObservers();
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
}
