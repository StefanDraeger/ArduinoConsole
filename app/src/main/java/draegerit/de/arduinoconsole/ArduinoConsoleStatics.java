package draegerit.de.arduinoconsole;

/**
 * Created by stefa on 30.05.2017.
 */

public final class ArduinoConsoleStatics {

    protected static final String HTTP_ADRESS = "https://www.draeger-it.blog";
    protected static final String EMAIL_ADRESS = "info@draeger-it.blog";

    public static final String EMPTY = "";

    protected static final String TEXT_PLAIN = "text/plain";

    protected static final String LINK_PHONE = "<a href=\"tel:+4953529096669\">05352/ 90 966 69</a>";
    protected static final String TEL = "tel:";
    protected static final String PHONENUMBER = "+4953529096669";

    protected static final String TIMEZONE = "Europe/Berlin";

    public static final String LONG_TIMESTAMP_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public enum ActionCommand {
        UpdateUsbDevice, ChangeConnectionStatus;
    }

    private ArduinoConsoleStatics() {

    }
}
