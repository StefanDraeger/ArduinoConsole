package draegerit.de.arduinoconsole.connection;

import java.util.ArrayList;
import java.util.List;

import draegerit.de.arduinoconsole.MainActivity;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.configuration.httpconnection.HttpConnectionProfile;
import draegerit.de.arduinoconsole.util.DriverAdapter;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.configuration.WifiConfiguration;

/**
 * Created by Stefan Draeger on 04.12.2017.
 */

public class WifiConnection extends AbstractArduinoConnection<WifiConfiguration> {

    public WifiConnection(WifiConfiguration configuration, MainActivity activity) {
        super(configuration, activity);
    }

    @Override
    public void registerDataAdapter() {
        List<DriverWrapper> drivers = new ArrayList<>();
        for (HttpConnectionProfile driver : this.configuration.getHttpConnectionProfiles()) {
            drivers.add(new DriverWrapper(driver, DriverWrapper.DriverType.WIFI));
        }

        DriverAdapter driverAdapter = new DriverAdapter(this.getActivity(), R.layout.devicespinnerlayout, R.id.deviceName, drivers);
        this.getActivity().getDriverSpinner().setAdapter(driverAdapter);
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void connect() {

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

    }

    @Override
    public boolean settingsValid() {
        return true;
    }
}
