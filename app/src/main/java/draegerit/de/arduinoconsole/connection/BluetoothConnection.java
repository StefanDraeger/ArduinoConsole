package draegerit.de.arduinoconsole.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.connection.task.CommunicationWriteAsyncTask;
import draegerit.de.arduinoconsole.connection.thread.ConnectionThread;
import draegerit.de.arduinoconsole.util.DriverAdapter;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.Message;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;


public class BluetoothConnection extends AbstractArduinoConnection<BluetoothConfiguration> {

    private static final String TAG = "ArduinoConsole";

    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> bluetoothDevices;

    private BluetoothSocket socket;

    private List<DriverWrapper> drivers;

    private ConnectionThread connectionThread;

    public BluetoothConnection(BluetoothConfiguration configuration, MainActivity activity) {
        this.configuration = configuration;
        this.activity = activity;
        init();
        registerDataAdapter();
    }

    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.msg_no_bluetooth_support), Toast.LENGTH_LONG);
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getActivity().startActivityForResult(enableBtIntent, 0);
        }


        createBroadcastReciver();
    }

    @Override
    public void registerDataAdapter() {
        this.drivers = new ArrayList<>();
        for (BluetoothDevice device : findBluetoothDevices()) {
            this.drivers.add(new DriverWrapper(device, DriverWrapper.DriverType.BLUETOOTH));
        }

        DriverAdapter driverAdapter = new DriverAdapter(this.getActivity(), R.layout.devicespinnerlayout, R.id.deviceName, this.drivers);
        this.getActivity().getDriverSpinner().setAdapter(driverAdapter);
    }

    @Override
    public void disconnect() {
        if (model.isConnected()) {
            try {
                this.connectionThread.setRunThread(false);
                this.socket.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            setConnected(false);
        }
    }

    @Override
    public void connect() {
        BluetoothDevice device = (BluetoothDevice) model.getDriver().getDriver();
        activity.getBluetoothSocket(device);
    }

    @Override
    public void sendCommand(String command) {
        BluetoothConfiguration config = PreferencesUtil.getBluetoothConfiguration(getActivity().getApplicationContext());
        boolean appendLineBreak = config.isAppendLineBreaks();
        if (appendLineBreak) {
            command = command.concat("\r\n");
        }

        try {
            Boolean sendWithoutException = new CommunicationWriteAsyncTask(socket).execute(command.getBytes()).get();
            if (sendWithoutException == null || !sendWithoutException) {
//                successfullTransferd = false;
            } else {
//                successfullTransferd = true;
            }
            model.addMessage(Message.Type.TO, "-> " + command + (appendLineBreak ? "" : "\r\n"));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void registerReciever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getActivity().registerReceiver(broadcastReceiver, filter);
        setReciverIsRegistered(true);
    }

    @Override
    public void createBroadcastReciver() {
        this.broadcastReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                handleBluetoothState(intent);
                handleAction(intent);
            }

            private void handleBluetoothState(Intent intent) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        showToast(R.string.bluetooth_off);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        showToast(R.string.bluetooth_on);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        showToast(R.string.bluetooth_turning_on);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        showToast(R.string.bluetooth_turning_off);
                        break;
                }
            }

            private void showToast(int stringResId) {
                Toast.makeText(getActivity(), getActivity().getApplicationContext().getString(stringResId), Toast.LENGTH_LONG).show();
            }

            private void handleAction(Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.i(TAG, "Suche gestartet.");
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.i(TAG, "Suche beendet.");
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        DriverWrapper driver = new DriverWrapper(device, DriverWrapper.DriverType.BLUETOOTH);
                        driver.setBonded(false);
                        updateDataAdapter(driver);
                        Log.i(TAG, "Etwas wurde gefunden.");
                        break;
                }
            }
        };
    }

    private void updateDataAdapter(DriverWrapper unpairedDevice) {
        boolean isDuplicate = false;
        if (unpairedDevice != null) {
            for (DriverWrapper wrapper : this.drivers) {
                if (((BluetoothDevice) wrapper.getDriver()).getAddress().equalsIgnoreCase(((BluetoothDevice) unpairedDevice.getDriver()).getAddress())) {
                    isDuplicate = true;
                    Log.d(TAG, "Find duplicate Bluetoothdevice. ---->" + ((BluetoothDevice) unpairedDevice.getDriver()).getName());
                    break;
                }
            }
        }
        if (!isDuplicate && unpairedDevice != null) {
            this.drivers.add(unpairedDevice);
        }
        DriverAdapter driverAdapter = (DriverAdapter) this.getActivity().getDriverSpinner().getAdapter();
        driverAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {
        findBluetoothDevices();
        model.updateDataAdapter();
    }

    @Override
    public boolean settingsValid() {
        return true;
    }

    private List<BluetoothDevice> findBluetoothDevices() {
        this.bluetoothDevices = new ArrayList<>();
        this.bluetoothDevices.addAll(findPairedBluetoothDevices());
        return this.bluetoothDevices;
    }

    private Set<BluetoothDevice> findPairedBluetoothDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    public void findUnPairedBluetoothDevices() {
        registerReciever();
        mBluetoothAdapter.startDiscovery();
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
        model.updateBluetoothAdapter();
    }

    public void postConnect() {
        boolean isConnected = socket != null && socket.isConnected();
        setConnected(isConnected);
        if (isConnected) {
            BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(getActivity().getApplicationContext());
            if (bluetoothConfiguration.isSendGreetingsMessage()) {
                sendCommand(bluetoothConfiguration.getGreetingsMessage());
            }

            this.connectionThread = new ConnectionThread(this.socket);
            this.connectionThread.start();

        } else {
            String deviceName = socket.getRemoteDevice().getName().replaceAll("\n", "");
            getActivity().showConnectionError(deviceName);
        }
    }


}
