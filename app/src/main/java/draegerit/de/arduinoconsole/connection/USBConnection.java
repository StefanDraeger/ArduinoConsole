package draegerit.de.arduinoconsole.connection;

import draegerit.de.arduinoconsole.util.USBConfiguration;

public class USBConnection extends AbstractArduinoConnection<USBConfiguration> {

    public USBConnection(USBConfiguration configuration) {
        super(configuration);
    }
}
