package draegerit.de.arduinoconsole.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.connection.bluetooth.ConnectionAsyncTask;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.DriverAdapter;
import draegerit.de.arduinoconsole.util.DriverWrapper;


public class BluetoothConnection extends AbstractArduinoConnection<BluetoothConfiguration> {

    private static final String TAG = "ArduinoConsole";

    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> bluetoothDevices;

    private BluetoothSocket socket;

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
    }

    @Override
    public void registerDataAdapter() {
        List<DriverWrapper> drivers = new ArrayList<>();
        for (BluetoothDevice device : findBluetoothDevices()) {
            drivers.add(new DriverWrapper(device, DriverWrapper.DriverType.BLUETOOTH));
        }

        DriverAdapter driverAdapter = new DriverAdapter(this.getActivity(), R.layout.devicespinnerlayout, R.id.deviceName, drivers, BluetoothDevice.class);
        this.getActivity().getDriverSpinner().setAdapter(driverAdapter);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    BluetoothDevice device = (BluetoothDevice) model.getDriver().getDriver();
                    socket = new ConnectionAsyncTask(device, getActivity()).execute(null, null, null).get();
                    if (socket != null && socket.isConnected()) {
                        //setStatus(ConnectionStatus.Connected);
                        //connectionThread = new ConnectionThread(this, this.socket);
                        //connectionThread.start();
                    } else {
                        //setStatus(ConnectionStatus.ConnectionFailed);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void sendCommand(String command) {

    }

    @Override
    public void registerReciever() {

    }

    @Override
    public void createBroadcastReciver() {

    }

    @Override
    public void refresh() {
        findBluetoothDevices();
        model.updateDataAdapter();
    }

    private List<BluetoothDevice> findBluetoothDevices() {
        this.bluetoothDevices = new ArrayList<>();
        this.bluetoothDevices.addAll(findPairedBluetoothDevices());
        return this.bluetoothDevices;
    }

    private Set<BluetoothDevice> findPairedBluetoothDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }
}
