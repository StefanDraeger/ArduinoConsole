package draegerit.de.arduinoconsole.configuration;


import android.widget.ArrayAdapter;
import android.widget.Spinner;

public abstract class AbstractTabController {

    protected int getPositionForValue(Spinner spinner, int value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        String valueStr = String.valueOf(value);
        int spinnerPosition = adapter.getPosition(valueStr);
        return spinnerPosition;
    }

    protected int getPositionForValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = adapter.getPosition(value);
        return spinnerPosition;
    }
}
