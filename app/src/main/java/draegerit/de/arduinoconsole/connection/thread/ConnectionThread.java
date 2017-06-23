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

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        if (inputStreamReader == null || bufferedReader == null) {
            inputStreamReader = new InputStreamReader(getConnectedInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        }

        while (isRunThread() && (this.connectedBluetoothSocket != null && this.connectedBluetoothSocket.isConnected())) {
            try {
                final StringBuilder sb = new StringBuilder();
                sb.append(bufferedReader.readLine());
                sb.append("\r\n");
                Log.i(TAG, sb.toString());

                model.addMessage(Message.Type.FROM, sb.toString());
            } catch (Exception e) {
                setRunThread(false);
                Log.e(TAG, e.getMessage());
            }
        }
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

    }

    private BluetoothSocket getConnectedBluetoothSocket() {
        return connectedBluetoothSocket;
    }

    private InputStream getConnectedInputStream() {
        return connectedInputStream;
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
