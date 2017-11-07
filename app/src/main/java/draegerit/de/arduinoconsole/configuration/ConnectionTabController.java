package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.configuration.USBConfiguration;

public class ConnectionTabController extends AbstractTabController<ConnectionTab>{

    public ConnectionTabController(ConnectionTab tab) {
        super(tab);
    }

    public void registerListeners() {
        this.tab.getDeviceBaudSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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

        this.tab.getDatabitSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        this.tab.getStopbitsSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        this.tab.getParitySpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        this.tab.getSendGreetingsChkBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tab.getGreetingsMessageText().setEnabled(isChecked);
            }
        });
    }

    public void save(Context ctx) {
        int baudrate = Integer.parseInt(this.tab.getDeviceBaudSpinner().getSelectedItem().toString());
        int databits = Integer.parseInt(this.tab.getDatabitSpinner().getSelectedItem().toString());
        int stopbits = Integer.parseInt(this.tab.getStopbitsSpinner().getSelectedItem().toString());
        EParity eParity = EParity.getByName(this.tab.getParitySpinner().getSelectedItem().toString());
        int parity = eParity.getValue();
        USBConfiguration usbConfiguration = new USBConfiguration(baudrate, databits, stopbits, parity);
        PreferencesUtil.storeUSBConfiguration(ctx, usbConfiguration);

        boolean showCloseBluetoothConnectionDialog = this.tab.getCloseBluetoothConnectionCheckbox().isChecked();
        boolean showSearchNewDevicesDialog = this.tab.getShowSearchNewDevicesChkBox().isChecked();
        boolean sendGreetingsMessage = this.tab.getSendGreetingsChkBox().isChecked();
        String greetingsMessage = this.tab.getGreetingsMessageText().getText().toString();

        BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration(showCloseBluetoothConnectionDialog, showSearchNewDevicesDialog, sendGreetingsMessage, greetingsMessage);
        PreferencesUtil.storeBluetoothConfiguration(ctx, bluetoothConfiguration);

        Toast.makeText(ctx, ctx.getString(R.string.save_connection), Toast.LENGTH_LONG).show();
    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(ctx);

        this.tab.getDeviceBaudSpinner().setSelection(getPositionForValue(this.tab.getDeviceBaudSpinner(), usbConfiguration.getBaudrate()));
        this.tab.getDatabitSpinner().setSelection(getPositionForValue(this.tab.getDatabitSpinner(), usbConfiguration.getDataBits()));
        this.tab.getStopbitsSpinner().setSelection(getPositionForValue(this.tab.getStopbitsSpinner(), usbConfiguration.getStopbits()));

        ArrayAdapter adapter = (ArrayAdapter) this.tab.getParitySpinner().getAdapter();
        EParity parity = EParity.getByValue(usbConfiguration.getParity());
        int spinnerPosition = adapter.getPosition(parity.name());
        this.tab.getParitySpinner().setSelection(spinnerPosition);

        BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(ctx);
        this.tab.getCloseBluetoothConnectionCheckbox().setChecked(bluetoothConfiguration.isShowCloseConnectionDialog());
        this.tab.getShowSearchNewDevicesChkBox().setChecked(bluetoothConfiguration.isShowSearchNewDevicesDialog());
        this.tab.getSendGreetingsChkBox().setChecked(bluetoothConfiguration.isSendGreetingsMessage());
        this.tab.getGreetingsMessageText().setText(bluetoothConfiguration.getGreetingsMessage());
    }

}
