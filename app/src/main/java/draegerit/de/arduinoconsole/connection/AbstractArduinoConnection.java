package draegerit.de.arduinoconsole.connection;

public abstract class AbstractArduinoConnection<T> {

    private T configuration;

    public AbstractArduinoConnection(T configuration) {
        this.configuration = configuration;
    }
}

