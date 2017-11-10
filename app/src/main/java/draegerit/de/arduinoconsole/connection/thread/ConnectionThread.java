package draegerit.de.arduinoconsole.connection.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.util.Message;


public class ConnectionThread extends Thread {

    private static final String TAG = ConnectionThread.class.getSimpleName();

    private final BluetoothSocket connectedBluetoothSocket;
    private InputStream connectedInputStream;

    private Model model = Model.getInstance();

    private boolean runThread = true;

    public ConnectionThread(BluetoothSocket socket) {
        this.connectedBluetoothSocket = socket;

        try {
            setConnectedInputStream(socket.getInputStream());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void run() {
        while (isRunThread() && (this.connectedBluetoothSocket != null && this.connectedBluetoothSocket.isConnected())) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(this.connectedBluetoothSocket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = null;
                while ((line = bufferedReader.readLine())!=null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<- ");
                    sb.append(line);
                    sb.append("\r\n");
                    model.addMessage(Message.Type.FROM, sb.toString());
                }
            } catch (Exception e) {
                setRunThread(false);
                Log.e(TAG, e.getMessage());
            }
        }

        Log.i(TAG, "Thread die!");
    }

    private void setConnectedInputStream(InputStream connectedInputStream) {
        this.connectedInputStream = connectedInputStream;
    }

    public boolean isRunThread() {
        return runThread;
    }

    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }
}
