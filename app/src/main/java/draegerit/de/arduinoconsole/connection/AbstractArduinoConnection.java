package draegerit.de.arduinoconsole.connection;

import android.content.BroadcastReceiver;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.Model;

public abstract class AbstractArduinoConnection<T> {

    protected T configuration;

    protected MainActivity activity;

    private boolean connected;

    private boolean reciverIsRegistered;

    protected BroadcastReceiver broadcastReceiver;

    protected Model model = Model.getInstance();

    public AbstractArduinoConnection() {
    }

    public AbstractArduinoConnection(T configuration, MainActivity activity) {
        this.configuration = configuration;
        this.activity = activity;
        registerDataAdapter();
    }

    public abstract void registerDataAdapter();

    public abstract void disconnect();

    public abstract void connect();

    public abstract void sendCommand(String command);

    public abstract void registerReciever();

    public abstract void createBroadcastReciver();

    public abstract void refresh();

    public abstract boolean settingsValid();

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        model.setConnected(connected);
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return broadcastReceiver;
    }

    public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
    }

    public MainActivity getActivity() {
        return activity;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public T getConfiguration() {
        return configuration;
    }

    public void setConfiguration(T configuration) {
        this.configuration = configuration;
    }

    public boolean isReciverIsRegistered() {
        return reciverIsRegistered;
    }

    public void setReciverIsRegistered(boolean reciverIsRegistered) {
        this.reciverIsRegistered = reciverIsRegistered;
    }
}

