package draegerit.de.arduinoconsole.configuration;


import android.widget.ArrayAdapter;
import android.widget.Spinner;

abstract class AbstractTabController {

    int getPositionForValue(Spinner spinner, int value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        String valueStr = String.valueOf(value);
        return adapter.getPosition(valueStr);
    }

    int getPositionForValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        return adapter.getPosition(value);
    }
}
