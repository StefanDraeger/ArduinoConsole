package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import draegerit.de.arduinoconsole.R;


public class ConnectionTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.configuration_tab_connection, container, false);
    }
}