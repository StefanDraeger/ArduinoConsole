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
import java.util.List;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.connection.thread.ConnectionThread;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.DriverWrapper;


public class BluetoothConnection extends AbstractBluetoothConnection {

    private BluetoothSocket socket;

    private ConnectionThread connectionThread;

    public BluetoothConnection(BluetoothConfiguration configuration, MainActivity activity) {
        super(configuration, activity);
        createBroadcastReciver();
    }

    protected void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.msg_no_bluetooth_support), Toast.LENGTH_LONG);
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getActivity().startActivityForResult(enableBtIntent, 0);
        }

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

    public void findUnPairedBluetoothDevices() {
        registerReciever();
        bluetoothAdapter.startDiscovery();
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
        model.updateBluetoothAdapter();
    }

    public void postConnect() {
        boolean isConnected = socket != null && socket.isConnected();
        setConnected(isConnected);
        if (isConnected) {
            this.connectionThread = new ConnectionThread(this.socket);
            this.connectionThread.start();
        } else {
getActivity().showConnectionError(socket.getRemoteDevice().getName());
        }
    }
}
