package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.EParity;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.configuration.USBConfiguration;

public class ConnectionTabController extends AbstractTabController<ConnectionTab> {

    private boolean serialRowsVisible = true;
    private boolean btRowsVisible = true;
    private boolean httpRowsVisible = true;

    public ConnectionTabController(ConnectionTab tab) {
        super(tab);
    }

    public void registerListeners() {
        this.tab.getSendGreetingsChkBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tab.getGreetingsMessageText().setEnabled(isChecked);
            }
        });

        this.tab.getSerialToggleImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSerialRows();
            }
        });

        this.tab.getSerialTitelText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSerialRows();
            }
        });

        this.tab.getBtToggleImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBtRows();
            }
        });

        this.tab.getBtTitelText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBtRows();
            }
        });

        this.tab.getHttpToggleImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHttpRows();
            }
        });

        this.tab.getHttpTitelText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHttpRows();
            }
        });

        this.tab.getHttpProfileSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                checkHttpProfileSpinner();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        this.tab.getAddProfileBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.tab.getRemoveProfileBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        this.tab.getEditProfileBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void toggleHttpRows() {
        httpRowsVisible = !httpRowsVisible;
        toggleRows(httpRowsVisible, this.tab.getHttpToggleImage(), this.tab.getHttpRows());
        checkHttpProfileSpinner();
    }

    private void checkHttpProfileSpinner() {
        Object o = this.tab.getHttpProfileSpinner().getSelectedItem();
        boolean enabledEditBtns = false;
        if (o != null) {
            String value = o.toString();
            enabledEditBtns = value != null && !value.isEmpty();
        }
        tab.getEditProfileBtn().setEnabled(enabledEditBtns);
        tab.getRemoveProfileBtn().setEnabled(enabledEditBtns);
    }

    private void toggleBtRows() {
        btRowsVisible = !btRowsVisible;
        toggleRows(btRowsVisible, this.tab.getBtToggleImage(), this.tab.getBtRows());
    }

    private void toggleSerialRows() {
        serialRowsVisible = !serialRowsVisible;
        toggleRows(serialRowsVisible, this.tab.getSerialToggleImage(), this.tab.getSerialRows());
    }

    private void toggleRows(boolean state, ImageView toggleImage, LinearLayout[] rows) {
        int visible = View.GONE;
        int drawableResId = R.drawable.plus;
        if (state) {
            visible = View.VISIBLE;
            drawableResId = R.drawable.minus;
        }
        toggleImage.setImageResource(drawableResId);

        List<LinearLayout> rs = Arrays.asList(rows);
        for (LinearLayout row : rs) {
            row.setVisibility(visible);
        }
    }

    public void save(Context ctx) {
        int baudrate = Integer.parseInt(this.tab.getDeviceBaudSpinner().getSelectedItem().toString());
        int databits = Integer.parseInt(this.tab.getDatabitSpinner().getSelectedItem().toString());
        int stopbits = Integer.parseInt(this.tab.getStopbitsSpinner().getSelectedItem().toString());
        EParity eParity = EParity.getByName(this.tab.getParitySpinner().getSelectedItem().toString());
        int parity = eParity.getValue();
        USBConfiguration usbConfiguration = new USBConfiguration(baudrate, databits, stopbits, parity);
        PreferencesUtil.storeUSBConfiguration(ctx, usbConfiguration);

        boolean appendLineBreaks = this.tab.getSendLineBreaksChkBox().isChecked();
        boolean showCloseBluetoothConnectionDialog = this.tab.getCloseBluetoothConnectionCheckbox().isChecked();
        boolean showSearchNewDevicesDialog = this.tab.getShowSearchNewDevicesChkBox().isChecked();
        boolean sendGreetingsMessage = this.tab.getSendGreetingsChkBox().isChecked();
        String greetingsMessage = this.tab.getGreetingsMessageText().getText().toString();

        BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration(showCloseBluetoothConnectionDialog, showSearchNewDevicesDialog, sendGreetingsMessage, appendLineBreaks, greetingsMessage);
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
        this.tab.getSendLineBreaksChkBox().setChecked(bluetoothConfiguration.isAppendLineBreaks());

        toggleSerialRows();
        toggleBtRows();
        toggleHttpRows();
    }

}
