package draegerit.de.arduinoconsole.connection.bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.MemoryHandler;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.util.MessageHandler;


public class ConnectionAsyncTask extends AsyncTask<Void, Void, BluetoothSocket> {

    private static final String TAG = ConnectionAsyncTask.class.getSimpleName();

    //Die eindeutige ID darf nicht geändert werden.
    //TODO: Warum?
    public static final String DEFAULT_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private ProgressDialog progressDialog;

    private MainActivity activity;

    private BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;


    public ConnectionAsyncTask(BluetoothDevice device, MainActivity activity) {
        this.bluetoothDevice = device;
        this.activity = activity;
    }

    @Override
    protected BluetoothSocket doInBackground(Void... params) {
        try {
            bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(DEFAULT_UUID));
            if (bluetoothSocket != null) {
                bluetoothSocket.connect();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return bluetoothSocket;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity, "Verbindung wird aufgebaut...", "Bitte warten");
    }

    @Override
    protected void onPostExecute(BluetoothSocket socket) {
        super.onPostExecute(socket);
        progressDialog.dismiss();
    }
}
