package draegerit.de.arduinoconsole;


import android.app.PendingIntent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.util.List;
import java.util.Observable;

import draegerit.de.arduinoconsole.util.EParity;

public class Model extends Observable {

    private int baudrate = 9600;

    private UsbSerialDriver driver;

    private UsbSerialPort port;

    private UsbDeviceConnection connection;

    private PendingIntent permissionIntent;

    private StringBuffer messages = new StringBuffer();

    private List<UsbDevice> usbDevices;

    private List<UsbSerialDriver> usbSerialDrivers;

    private boolean isConnected;

    private int databits = 8;

    private int stopbits = 1;

    private EParity parity = EParity.NONE;

    public Model() {

    }

    public List<UsbSerialDriver> getUsbSerialDrivers() {
        return usbSerialDrivers;
    }

    public void setUsbSerialDrivers(List<UsbSerialDriver> usbSerialDrivers) {
        this.usbSerialDrivers = usbSerialDrivers;
        setChanged();
        notifyObservers();
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

    public StringBuffer getMessages() {
        return messages;
    }

    public void setMessages(StringBuffer messages) {
        this.messages = messages;
        setChanged();
        notifyObservers();
    }

    public List<UsbDevice> getUsbDevices() {
        return usbDevices;
    }

    public void setUsbDevices(List<UsbDevice> usbDevices) {
        this.usbDevices = usbDevices;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        setChanged();
        notifyObservers();
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

    public void addMessage(String message) {
        getMessages().append(message);
        Log.i("Test",message);
        setChanged();
        notifyObservers();
    }

}
