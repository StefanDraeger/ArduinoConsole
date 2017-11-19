package draegerit.de.arduinoconsole.configuration.httpconnection;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Stefan Draeger on 19.11.2017.
 */

public final class HttpConnectionUtil {

    private HttpConnectionUtil() {
        //Empty
    }

    public static boolean isHttpConnectionAvailable(String address) {
        boolean result = false;
        try {
            result = InetAddress.getByName(address).isReachable(1500);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private class TestConnectionAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
