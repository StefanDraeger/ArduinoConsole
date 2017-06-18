package draegerit.de.arduinoconsole.connection;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.DriverAdapter;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.Message;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.USBConfiguration;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.EMPTY;

public class USBConnection extends AbstractArduinoConnection<USBConfiguration> {

    private static final String TAG = "ArduinoConsole";

    private static final int FIRST_ENTRY = 0;

    private static final int TIMEOUT_MILLIS = 1000;

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private PendingIntent permissionIntent;

    private List<UsbSerialDriver> usbSerialDrivers;

    private UsbDeviceConnection connection;

    private UsbSerialPort port;

    public USBConnection(USBConfiguration configuration, MainActivity activity) {
        super(configuration, activity);
    }

    /**
     * Erzeugt aus der Liste der Geräte einen Adapter für die Auswahlliste.
     */
    public void registerDataAdapter() {
        findPorts();
        List<DriverWrapper> drivers = new ArrayList<>();
        for (UsbSerialDriver driver : getUsbSerialDrivers()) {
            drivers.add(new DriverWrapper(driver, DriverWrapper.DriverType.USB));
        }

        DriverAdapter driverAdapter = new DriverAdapter(this.getActivity(), R.layout.devicespinnerlayout, R.id.deviceName, drivers, UsbSerialDriver.class);
        this.getActivity().getDriverSpinner().setAdapter(driverAdapter);
    }


    @Override
    public void disconnect() {
        try {
            if (getPort() != null) {
                getPort().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setConnected(false);
        if (getConnection() != null) {
            getConnection().close();
        }
    }

    public UsbSerialPort getPort() {
        return port;
    }

    public void setPort(UsbSerialPort port) {
        this.port = port;
    }

    @Override
    public void connect() {
        registerReciever();

        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbDeviceConnection connection = null;
        UsbSerialDriver driver = (UsbSerialDriver) model.getDriver().getDriver();

        try {
            UsbDevice device = driver.getDevice();

            boolean findDriver = false;
            for (UsbSerialDriver usbSerialDriver : findUsbSerialDriver()) {
                if (usbSerialDriver.getDevice().getDeviceName().equalsIgnoreCase(device.getDeviceName())) {
                    findDriver = true;
                    break;
                }
            }
            if(!findDriver){
                MessageHandler.showErrorMessage(getActivity(), getActivity().getResources().getString(R.string.msg_no_driver_found, device.getDeviceName()));
                setConnected(false);
                return;
            }

            connection = manager.openDevice(device);
            if (connection == null) {
                manager.requestPermission(device, getPermissionIntent());
                return;
            }
        } catch (Exception e) {
            MessageHandler.showErrorMessage(getActivity(), e.getMessage());
            Log.e(TAG, e.getMessage(), e);
        }

        if (driver == null) {
            return;
        }

        UsbSerialPort port = driver.getPorts().get(FIRST_ENTRY);
        try {
            port.open(connection);
            setConnection(connection);

            USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(getActivity().getApplicationContext());
            port.setParameters(usbConfiguration.getBaudrate(), usbConfiguration.getDataBits(), usbConfiguration.getStopbits(), usbConfiguration.getParity());
            setPort(port);

            setConnected(true);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SerialCommunicationAsyncTask().execute();
                }
            });

        } catch (Exception e) {
            MessageHandler.showErrorMessage(getActivity(), e.getMessage());
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void sendCommand(String command) {
        /**
         try {
         model.getPort().write(command.getBytes(), TIMEOUT_MILLIS);
         model.addMessage(Message.Type.TO, command + "\r\n");
         } catch (IOException e) {
         MessageHandler.showErrorMessage(mainActivity, e.getMessage());
         Log.e(TAG, e.getMessage(), e);
         }
         **/
    }

    @Override
    public void registerReciever() {
        final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        setPermissionIntent(mPermissionIntent);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        getActivity().registerReceiver(broadcastReceiver, filter);
        setReciverIsRegistered(true);
    }

    @Override
    public void createBroadcastReciver() {
        this.broadcastReceiver = new BroadcastReceiver() {

            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (device != null) {
                                Log.d(TAG, "call method to set up device communication");
                            }
                        } else {
                            Log.d(TAG, "permission denied for device " + device);
                        }
                    }
                }
            }
        };
    }

    @Override
    public void refresh() {
        findPorts();
        model.updateDataAdapter();
    }

    /**
     * Sucht angegeschlossene Geräte und speichert die Liste im {@link Model}
     */
    private void findPorts() {
        setUsbSerialDrivers(findUsbSerialDriver());
    }

    private List<UsbSerialDriver> findUsbSerialDriver() {
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        return UsbSerialProber.getDefaultProber().findAllDrivers(manager);
    }

    public PendingIntent getPermissionIntent() {
        return permissionIntent;
    }

    public void setPermissionIntent(PendingIntent permissionIntent) {
        this.permissionIntent = permissionIntent;
    }

    public List<UsbSerialDriver> getUsbSerialDrivers() {
        return usbSerialDrivers;
    }

    public void setUsbSerialDrivers(List<UsbSerialDriver> usbSerialDrivers) {
        this.usbSerialDrivers = usbSerialDrivers;
    }

    public UsbDeviceConnection getConnection() {
        return connection;
    }

    public void setConnection(UsbDeviceConnection connection) {
        this.connection = connection;
    }

    private class SerialCommunicationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... params) {
            while (isConnected()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    MessageHandler.showErrorMessage(getActivity(), e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }

                String value = null;
                try {
                    value = getValueFromSerialPort();
                } catch (IOException e) {
                    MessageHandler.showErrorMessage(getActivity(), e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }
                synchronized (model) {
                    if (value != null && value.trim().length() > 0) {
                        model.addMessage(Message.Type.FROM, value);
                    } else {
                        Log.i(TAG, "No value read!");
                    }
                }
            }
            return null;
        }
    }

    private String getValueFromSerialPort() throws IOException {
        String result = EMPTY;
        byte[] buffer = new byte[32];
        if (getPort() != null) {
            try {
                int readBytes = getPort().read(buffer, TIMEOUT_MILLIS);
                if (readBytes > 0) {
                    result = new String(buffer);
                }
            } catch (NullPointerException ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
        return result;
    }

}
