package draegerit.de.arduinoconsole;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.UsbDriverAdapter;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

class Controller implements Observer {

    private static final String TAG = "ArduinoConsole";

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final int FIRST_ENTRY = 0;
    public static final int TIMEOUT_MILLIS = 1000;
    public static final int DEFAULT_BAUD_POS = 4;
    public static final int DEFAULT_PARIRTY_POS = 0;
    public static final int DEFAULT_STOPBITS_POS = 0;
    public static final int DEFAULT_DATABITS_POS = 3;

    private Model model = new Model();

    private MainActivity mainActivity;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

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

    Controller(final MainActivity inMainActivity) {
        model.addObserver(this);
        this.mainActivity = inMainActivity;
        model.addObserver(this.mainActivity);
        registerListeners();
        loadUsbDevices();
        findPorts();
        registerDataAdapter();

        setDefaultValues();
    }

    void unregisterReceiver() {
        mainActivity.unregisterReceiver(mUsbReceiver);
    }


    private void setDefaultValues() {
        mainActivity.getDeviceBaudSpinner().setSelection(DEFAULT_BAUD_POS);
        mainActivity.getParitySpinner().setSelection(DEFAULT_PARIRTY_POS);
        mainActivity.getStopbitsSpinner().setSelection(DEFAULT_STOPBITS_POS);
        mainActivity.getDatabitSpinner().setSelection(DEFAULT_DATABITS_POS);
    }

    private void findPorts() {
        UsbManager manager = (UsbManager) this.mainActivity.getSystemService(Context.USB_SERVICE);
        model.setUsbSerialDrivers(UsbSerialProber.getDefaultProber().findAllDrivers(manager));
    }

    private void registerDataAdapter() {
        UsbDriverAdapter driverAdapter = new UsbDriverAdapter(this.mainActivity, R.layout.devicespinnerlayout, R.id.deviceName, model.getUsbSerialDrivers());
        this.mainActivity.getDriverSpinner().setAdapter(driverAdapter);
    }

    private void loadUsbDevices() {
        List<UsbDevice> deviceUsbList = new ArrayList<>();

        UsbManager manager = (UsbManager) mainActivity.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            deviceUsbList.add(device);
        }
        model.setUsbDevices(deviceUsbList);
    }

    private void registerListeners() {
        mainActivity.getDeviceBaudSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                String value = ((TextView) view).getText().toString();
                int baudrate = Integer.parseInt(value);
                model.setBaudrate(baudrate);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getDriverSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                UsbSerialDriver driver = (UsbSerialDriver) mainActivity.getDriverSpinner().getSelectedItem();
                model.setDriver(driver);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getDatabitSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                String value = ((TextView) view).getText().toString();
                int databits = Integer.parseInt(value);
                model.setDatabits(databits);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getStopbitsSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                String value = ((TextView) view).getText().toString();
                int stopbits = Integer.parseInt(value);
                model.setStopbits(stopbits);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getParitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                String value = ((TextView) view).getText().toString();
                model.setParity(EParity.getByName(value));
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getConnectBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!model.isConnected()) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });

        this.mainActivity.getClearBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.setMessages(new StringBuffer());
            }
        });

        this.mainActivity.getRefreshDeviceBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                findPorts();
                loadUsbDevices();
            }
        });

        this.mainActivity.getHyperlinkTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(HTTP_ADRESS));
                mainActivity.startActivity(i);
            }
        });

        this.mainActivity.getConfigureBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });
    }

    private void disconnect() {
        if (model.getConnection() != null) {
            try {
                model.getPort().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.getConnection().close();
        }
        model.setConnected(false);
    }

    private void registerReciever() {
        final PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this.mainActivity, 0, new Intent(ACTION_USB_PERMISSION), 0);
        model.setPermissionIntent(mPermissionIntent);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        this.mainActivity.registerReceiver(mUsbReceiver, filter);
    }

    @Override
    public void update(final Observable o, final Object arg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                registerDataAdapter();
            }
        });
    }


    private void connect() {

        registerReciever();

        UsbManager manager = (UsbManager) mainActivity.getSystemService(Context.USB_SERVICE);
        UsbSerialDriver driver = model.getDriver();
        UsbDeviceConnection connection = null;
        try {
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) {
                manager.requestPermission(driver.getDevice(), model.getPermissionIntent());
                return;
            }
        } catch (Exception e) {
            MessageHandler.showErrorMessage(mainActivity, e.getMessage());
            Log.e(TAG, e.getMessage(), e);
        }

        UsbSerialPort port = driver.getPorts().get(FIRST_ENTRY);
        try {
            port.open(connection);
            model.setConnection(connection);
            port.setParameters(model.getBaudrate(), model.getDatabits(), model.getStopbits(), model.getParity().getValue());
            model.setPort(port);
            model.setConnected(true);
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SerialCommunicationAsyncTask().execute();
                }
            });

        } catch (Exception e) {
            MessageHandler.showErrorMessage(mainActivity, e.getMessage());
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private class SerialCommunicationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(final Void... params) {
            while (model.isConnected()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    MessageHandler.showErrorMessage(mainActivity, e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }

                String value = null;
                try {
                    value = getValueFromSerialPort();
                } catch (IOException e) {
                    MessageHandler.showErrorMessage(mainActivity, e.getMessage());
                    Log.e(TAG, e.getMessage(), e);
                }
                model.addMessage(value);

            }
            return null;
        }
    }

    private String getValueFromSerialPort() throws IOException {
        byte[] buffer = new byte[16];
        if (model.getPort() != null) {
            try {
                int numBytesRead = model.getPort().read(buffer, TIMEOUT_MILLIS);
                Log.i(TAG, String.format("Read %d bytes from %s", numBytesRead, model.getPort().getDriver().getDevice().getDeviceName()));
            } catch (NullPointerException ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
        }
        return new String(buffer);
    }


}
