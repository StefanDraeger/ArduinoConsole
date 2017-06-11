package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.USBConfiguration;


public class ConnectionTab extends Fragment {

    private Model model = Model.getInstance();

    /**
     * Auswahlliste für die Auswahl der Übertragungsgeschwindigkeit.
     **/
    private Spinner deviceBaudSpinner;

    /**
     * Auswahlliste für die Auswahl der Databits.
     **/
    private Spinner databitSpinner;

    /**
     * Auswahlliste für die Auswahl der Stopbits.
     **/
    private Spinner stopbitsSpinner;

    /**
     * Auswahlliste für die Auswahl der Parity.
     **/
    private Spinner paritySpinner;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.configuration_tab_connection, container, false);
        registerComponents();
        setDefaultValues();
        registerListeners();
        return this.view;
    }

    /**
     * Erzeugt die Komponenten der View.
     */
    private void registerComponents() {
        this.deviceBaudSpinner = (Spinner) this.view.findViewById(R.id.baudSpinner);
        this.databitSpinner = (Spinner) this.view.findViewById(R.id.databitSpinner);
        this.stopbitsSpinner = (Spinner) this.view.findViewById(R.id.stopbitsSpinner);
        this.paritySpinner = (Spinner) this.view.findViewById(R.id.paritySpinner);
    }

    /**
     * Setzt die Default Werte.
     */
    private void setDefaultValues() {
        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConnection(getContext());

        getDeviceBaudSpinner().setSelection(getPositionForValue(getDeviceBaudSpinner(), usbConfiguration.getBaudrate()));
        getDatabitSpinner().setSelection(getPositionForValue(getDatabitSpinner(), usbConfiguration.getDataBits()));
        getStopbitsSpinner().setSelection(getPositionForValue(getStopbitsSpinner(), usbConfiguration.getStopbits()));

        ArrayAdapter adapter = (ArrayAdapter) getParitySpinner().getAdapter();
        EParity parity = EParity.getByValue(usbConfiguration.getParity());
        int spinnerPosition = adapter.getPosition(parity.name());
        getParitySpinner().setSelection(spinnerPosition);
    }

    private int getPositionForValue(Spinner spinner, int value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        String valueStr = String.valueOf(value);
        int spinnerPosition = adapter.getPosition(valueStr);
        return spinnerPosition;
    }

    private void registerListeners() {
        getDeviceBaudSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int baudrate = Integer.parseInt(value);
                    model.setBaudrate(baudrate);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        getDatabitSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int databits = Integer.parseInt(value);
                    model.setDatabits(databits);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        getStopbitsSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int stopbits = Integer.parseInt(value);
                    model.setStopbits(stopbits);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        getParitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    model.setParity(EParity.getByName(value));
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    public void save() {
        int baudrate = Integer.parseInt(getDeviceBaudSpinner().getSelectedItem().toString());
        int databits = Integer.parseInt(getDatabitSpinner().getSelectedItem().toString());
        int stopbits = Integer.parseInt(getStopbitsSpinner().getSelectedItem().toString());
        EParity eParity = EParity.getByName(getParitySpinner().getSelectedItem().toString());
        int parity = eParity.getValue();
        USBConfiguration usbConfiguration = new USBConfiguration(baudrate, databits, stopbits, parity);
        PreferencesUtil.storeUSBConnection(getContext(), usbConfiguration);

        Toast.makeText(getContext(),getString(R.string.save_connection),Toast.LENGTH_LONG).show();
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Auswahl der Übertragungsgeschwindigkeit.
     *
     * @return {@link Spinner}
     */
    public Spinner getDeviceBaudSpinner() {
        return deviceBaudSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Databits.
     *
     * @return {@link Spinner}
     */
    public Spinner getDatabitSpinner() {
        return databitSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Stopbits.
     *
     * @return {@link Spinner}
     */
    public Spinner getStopbitsSpinner() {
        return stopbitsSpinner;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Parity.
     *
     * @return {@link Spinner}
     */
    public Spinner getParitySpinner() {
        return paritySpinner;
    }

}
