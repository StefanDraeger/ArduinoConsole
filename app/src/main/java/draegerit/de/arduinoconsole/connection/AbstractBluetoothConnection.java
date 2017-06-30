package draegerit.de.arduinoconsole.connection;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.DriverAdapter;
import draegerit.de.arduinoconsole.util.DriverWrapper;

public abstract class AbstractBluetoothConnection extends AbstractArduinoConnection<BluetoothConfiguration> {

    protected BluetoothAdapter bluetoothAdapter;

    private List<DriverWrapper> drivers;

    private List<BluetoothDevice> bluetoothDevices;

    public AbstractBluetoothConnection(BluetoothConfiguration configuration, MainActivity activity) {
        this.configuration = configuration;
        this.activity = activity;
        init();
        registerDataAdapter();
    }

    protected abstract void init();

    @Override
    public void registerDataAdapter() {
        this.drivers = new ArrayList<>();
        for (BluetoothDevice device : findBluetoothDevices()) {
            this.drivers.add(new DriverWrapper(device, DriverWrapper.DriverType.BLUETOOTH));
        }
        updateDataAdapter(null);
    }

    protected void updateDataAdapter(DriverWrapper unpairedDevice) {
        List<DriverWrapper> driver = new ArrayList<>();
        driver.addAll(this.drivers);
        if (unpairedDevice != null) {
            boolean isDuplicate = false;
            for (DriverWrapper wrapper : driver) {
                if (((BluetoothDevice) wrapper.getDriver()).getAddress().equalsIgnoreCase(((BluetoothDevice) unpairedDevice.getDriver()).getAddress())) {
                    isDuplicate = true;
                    Log.d(TAG, "Find duplicate Bluetoothdevice. ---->" + ((BluetoothDevice) unpairedDevice.getDriver()).getName());
                }
            }
            if (!isDuplicate) {
                driver.add(unpairedDevice);
            }
        }
        DriverAdapter driverAdapter = new DriverAdapter(this.getActivity(), R.layout.devicespinnerlayout, R.id.deviceName, driver);
        this.getActivity().getDriverSpinner().setAdapter(driverAdapter);
    }

    private List<BluetoothDevice> findBluetoothDevices() {
        this.bluetoothDevices = new ArrayList<>();
        this.bluetoothDevices.addAll(findPairedBluetoothDevices());
        return this.bluetoothDevices;
    }

    private Set<BluetoothDevice> findPairedBluetoothDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    public abstract void findUnPairedBluetoothDevices();

    @Override
    public void refresh() {
        findBluetoothDevices();
        model.updateDataAdapter();
    }

    public List<BluetoothDevice> getBluetoothDevices() {
        return bluetoothDevices;
    }


    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

}
