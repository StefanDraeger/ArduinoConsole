package draegerit.de.arduinoconsole;

/**
 * Created by stefa on 30.05.2017.
 */

public final class ArduinoConsoleStatics {

    public static final String HTTP_ADRESS = "https://www.draeger-it.blog";
    public static final String EMAIL_ADRESS = "info@draeger-it.blog";

    public enum ActionCommand {
        UpdateUsbDevice, ChangeConnectionStatus;
    }

    private ArduinoConsoleStatics() {

    }
}
