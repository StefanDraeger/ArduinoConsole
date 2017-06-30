package draegerit.de.arduinoconsole.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;

public class BluetoothLEConnection extends AbstractBluetoothConnection {

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {

            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mBluetoothGatt.getServices();
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                intent.getStringExtra(EXTRA_DATA);
            }
        }
    };

    private void broadcastUpdate(final String action) {
        System.out.println(action);
        final Intent intent = new Intent(action);
        getActivity().sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        System.out.println(action);
        System.out.println(characteristic);
    }


    private BluetoothGatt mBluetoothGatt;

    public BluetoothLEConnection(BluetoothConfiguration configuration, MainActivity activity) {
        super(configuration, activity);
    }

    protected void init(){
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getActivity(), "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
        }
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void findUnPairedBluetoothDevices() {
        activity.findNonBondedBluetoothLEDevices(this);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() {
        BluetoothDevice device = (BluetoothDevice) model.getDriver().getDriver();
        System.out.println(device);
        mBluetoothGatt = device.connectGatt(getActivity().getApplicationContext(), false, mGattCallback);
        System.out.println(mBluetoothGatt);
    }


    @Override
    public void sendCommand(String command) {

    }

    @Override
    public void registerReciever() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        activity.registerReceiver(mGattUpdateReceiver,intentFilter);
    }

    @Override
    public void createBroadcastReciver() {

    }

}
