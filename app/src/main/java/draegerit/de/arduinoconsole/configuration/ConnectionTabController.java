package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.USBConfiguration;

public class ConnectionTabController {

    private ConnectionTab connectionTab;

    public ConnectionTabController(ConnectionTab connectionTab){
        this.connectionTab = connectionTab;
    }

    public void registerListeners() {
      this.connectionTab.getDeviceBaudSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int baudrate = Integer.parseInt(value);
                    //model.setBaudrate(baudrate);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        this.connectionTab.getDatabitSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int databits = Integer.parseInt(value);
                    //model.setDatabits(databits);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        this.connectionTab.getStopbitsSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    int stopbits = Integer.parseInt(value);
                    //model.setStopbits(stopbits);
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        this.connectionTab.getParitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    //model.setParity(EParity.getByName(value));
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    public void save(Context ctx) {
        int baudrate = Integer.parseInt(this.connectionTab.getDeviceBaudSpinner().getSelectedItem().toString());
        int databits = Integer.parseInt(this.connectionTab.getDatabitSpinner().getSelectedItem().toString());
        int stopbits = Integer.parseInt(this.connectionTab.getStopbitsSpinner().getSelectedItem().toString());
        EParity eParity = EParity.getByName(this.connectionTab.getParitySpinner().getSelectedItem().toString());
        int parity = eParity.getValue();
        USBConfiguration usbConfiguration = new USBConfiguration(baudrate, databits, stopbits, parity);
        PreferencesUtil.storeUSBConnection(ctx, usbConfiguration);

        Toast.makeText(ctx,ctx.getString(R.string.save_connection), Toast.LENGTH_LONG).show();
    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConnection(ctx);

        this.connectionTab.getDeviceBaudSpinner().setSelection(getPositionForValue(this.connectionTab.getDeviceBaudSpinner(), usbConfiguration.getBaudrate()));
        this.connectionTab.getDatabitSpinner().setSelection(getPositionForValue(this.connectionTab.getDatabitSpinner(), usbConfiguration.getDataBits()));
        this.connectionTab.getStopbitsSpinner().setSelection(getPositionForValue(this.connectionTab.getStopbitsSpinner(), usbConfiguration.getStopbits()));

        ArrayAdapter adapter = (ArrayAdapter) this.connectionTab.getParitySpinner().getAdapter();
        EParity parity = EParity.getByValue(usbConfiguration.getParity());
        int spinnerPosition = adapter.getPosition(parity.name());
        this.connectionTab.getParitySpinner().setSelection(spinnerPosition);
    }

    private int getPositionForValue(Spinner spinner, int value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        String valueStr = String.valueOf(value);
        int spinnerPosition = adapter.getPosition(valueStr);
        return spinnerPosition;
    }
}
