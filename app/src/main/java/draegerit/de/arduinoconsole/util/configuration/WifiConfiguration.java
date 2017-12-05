package draegerit.de.arduinoconsole.util.configuration;

import java.util.List;

import draegerit.de.arduinoconsole.configuration.httpconnection.HttpConnectionProfile;

/**
 * Created by Stefan Draeger on 04.12.2017.
 */

public class WifiConfiguration {

    private List<HttpConnectionProfile> httpConnectionProfiles;

    public WifiConfiguration(List<HttpConnectionProfile> httpConnectionProfiles) {
        this.httpConnectionProfiles = httpConnectionProfiles;
    }

    public List<HttpConnectionProfile> getHttpConnectionProfiles() {
        return httpConnectionProfiles;
    }

    public void setHttpConnectionProfiles(List<HttpConnectionProfile> httpConnectionProfiles) {
        this.httpConnectionProfiles = httpConnectionProfiles;
    }
}
