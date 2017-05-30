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
import android.os.Handler;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.UsbDriverAdapter;

public class Controller implements Observer {

    private static final String TAG = "ArduinoConsole";

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private Model model = new Model();

    private MainActivity mainActivity;

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                        }
                    } else {
                        Log.d("AC", "permission denied for device " + device);
                    }
                }
            }
        }
    };

    public Controller(MainActivity mainActivity) {
        model.addObserver(this);
        this.mainActivity = mainActivity;
        model.addObserver(this.mainActivity);
        registerListeners();
        loadUsbDevices();
        findPorts();
        registerDataAdapter();

        setDefaultValues();
    }

    private void setDefaultValues() {
        mainActivity.getDeviceBaudSpinner().setSelection(4);
        mainActivity.getParitySpinner().setSelection(0);
        mainActivity.getStopbitsSpinner().setSelection(0);
        mainActivity.getDatabitSpinner().setSelection(3);
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
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            deviceUsbList.add(device);
        }
        model.setUsbDevices(deviceUsbList);
    }

    private void registerListeners() {
        mainActivity.getDeviceBaudSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((TextView) view).getText().toString();
                int baudrate = Integer.parseInt(value);
                model.setBaudrate(baudrate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainActivity.getDriverSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UsbSerialDriver driver = (UsbSerialDriver) mainActivity.getDriverSpinner().getSelectedItem();
                model.setDriver(driver);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainActivity.getDatabitSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((TextView) view).getText().toString();
                int databits = Integer.parseInt(value);
                model.setDatabits(databits);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainActivity.getStopbitsSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((TextView) view).getText().toString();
                int stopbits = Integer.parseInt(value);
                model.setStopbits(stopbits);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainActivity.getParitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = ((TextView) view).getText().toString();
                model.setParity(EParity.getByName(value));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mainActivity.getConnectBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!model.isConnected()) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });

        this.mainActivity.getClearBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.setMessages(new StringBuffer());
            }
        });

        this.mainActivity.getRefreshDeviceBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPorts();
                loadUsbDevices();
            }
        });

        this.mainActivity.getHyperlinkTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.draeger-it.blog"));
                mainActivity.startActivity(i);
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
    public void update(Observable o, Object arg) {
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
            e.printStackTrace();
        }

        UsbSerialPort port = driver.getPorts().get(0);
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
            e.printStackTrace();
        }
    }

    public void unregisterReceiver() {
        mainActivity.unregisterReceiver(mUsbReceiver);
    }

    private class SerialCommunicationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            while (model.isConnected()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String value = null;
                try {
                    value = getValueFromSerialPort();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                model.addMessage(value);

            }
        }
    }

    public String getValueFromSerialPort() throws IOException {
        byte buffer[] = new byte[16];
        int numBytesRead = model.getPort().read(buffer, 1000);
        return new String(buffer);
    }

    private void readFromUSB() {
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
            e.printStackTrace();
        }

        UsbSerialPort port = driver.getPorts().get(0);
        try {
            port.open(connection);
            port.setParameters(model.getBaudrate(), model.getDatabits(), model.getStopbits(), model.getParity().getValue());
            model.setPort(port);
            model.setConnected(true);
            String value = getValueFromSerialPort();
            model.addMessage(value);
        } catch (Exception e) {
            MessageHandler.showErrorMessage(mainActivity, e.getMessage());
            e.printStackTrace();
        }
    }

}
