package draegerit.de.arduinoconsole.connection.task;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

public class CommunicationWriteAsyncTask extends AsyncTask<byte[], Void, Boolean> {

    private static final String TAG = CommunicationWriteAsyncTask.class.getSimpleName();

    private BluetoothSocket socket;

    public CommunicationWriteAsyncTask(BluetoothSocket socket) {
        this.socket = socket;
    }

    @Override
    protected Boolean doInBackground(byte[]... content) {
        boolean sendWithoutException = false;
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(content[0]);
            sendWithoutException = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return sendWithoutException;
    }

}
