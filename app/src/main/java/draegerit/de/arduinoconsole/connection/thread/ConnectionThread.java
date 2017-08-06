package draegerit.de.arduinoconsole.connection.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.util.Message;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.EMPTY;


public class ConnectionThread extends Thread {

    private static final String TAG = ConnectionThread.class.getSimpleName();
    private static final String LINE_BREAK = "\r\n";

    private final BluetoothSocket connectedBluetoothSocket;

    private Model model = Model.getInstance();

    private boolean runThread = true;

    public ConnectionThread(BluetoothSocket socket) {
        this.connectedBluetoothSocket = socket;
    }

    @Override
    public void run() {
        while (isRunThread() && (this.connectedBluetoothSocket != null && this.connectedBluetoothSocket.isConnected())) {
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(this.connectedBluetoothSocket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                final StringBuilder sb = new StringBuilder();
                String line;
                while (!(line = bufferedReader.readLine()).equalsIgnoreCase(EMPTY)) {
                    sb.append(line);
                    sb.append(LINE_BREAK);
                    model.addMessage(Message.Type.FROM, sb.toString());
                }
            } catch (Exception e) {
                setRunThread(false);
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private boolean isRunThread() {
        return runThread;
    }

    public void setRunThread(boolean runThread) {
        this.runThread = runThread;
    }
}
