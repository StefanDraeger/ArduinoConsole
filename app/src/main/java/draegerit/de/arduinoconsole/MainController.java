package draegerit.de.arduinoconsole;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import draegerit.de.arduinoconsole.connection.BluetoothConnection;
import draegerit.de.arduinoconsole.connection.USBConnection;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.ChartPreferences;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.USBConfiguration;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

class MainController extends AbstractController {

    private static final String TAG = "ArduinoConsole";

    private Model model = Model.getInstance();

    private MainActivity mainActivity;


    MainController(final MainActivity inMainActivity) {
        super(inMainActivity);
        this.mainActivity = inMainActivity;
        model.addObserver(this.mainActivity);
        init();
        registerListeners();
    }

    private void init() {
        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(mainActivity.getApplicationContext());
        model.setArduinoConnection(new USBConnection(usbConfiguration, mainActivity));
    }

    @Override
    public void registerComponents() {

    }

    /**
     * Entfernt den {@link BroadcastReceiver}
     */
    void unregisterReceiver() {
        try {
            mainActivity.unregisterReceiver(model.getArduinoConnection().getBroadcastReceiver());
        } catch (IllegalArgumentException ex) {
            Log.e(TAG, ex.getMessage());
        }

    }

    private void registerListeners() {
        mainActivity.getDriverSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                DriverWrapper<?> driver = (DriverWrapper<?>) mainActivity.getDriverSpinner().getSelectedItem();
                model.setDriver(driver);
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });

        mainActivity.getConnectBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!model.getArduinoConnection().isConnected()) {
                    connect();
                } else {
                    disconnect();
                }
            }
        });

        this.mainActivity.getClearBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.getMessages().clear();
                mainActivity.getConsoleTextView().setText("");
            }
        });

        this.mainActivity.getRefreshDeviceBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.getArduinoConnection().refresh();
            }
        });

        this.mainActivity.getHyperlinkTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(HTTP_ADRESS));
                mainActivity.startActivity(i);
            }
        });

        this.mainActivity.getConfigureBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int configurePaneVisibility = View.VISIBLE;
                switch (model.getConfigurePaneVisibility()) {
                    case View.GONE:
                        configurePaneVisibility = View.VISIBLE;
                        break;
                    case View.VISIBLE:
                        configurePaneVisibility = View.GONE;
                        break;
                }
                model.setConfigurePaneVisibility(configurePaneVisibility);
                mainActivity.getConfig1TblRow().setVisibility(configurePaneVisibility);
                mainActivity.getConfig2TblRow().setVisibility(configurePaneVisibility);
                mainActivity.getConfig3TblRow().setVisibility(configurePaneVisibility);
            }
        });

        this.mainActivity.getSendBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String command = mainActivity.getCommandTextView().getText().toString();
                if (command.trim().length() > 0) {
                    model.getArduinoConnection().sendCommand(command);
                } else {
                    MessageHandler.showErrorMessage(mainActivity, mainActivity.getResources().getString(R.string.msg_emptycommands));
                }
            }
        });

        this.mainActivity.getAutoScrollCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setAutoScroll(isChecked);
            }
        });

        this.mainActivity.getDeviceConnectionTypeSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (view != null) {
                    String value = ((TextView) view).getText().toString();
                    if (value.equalsIgnoreCase("USB Serial Connection")) {
                        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(mainActivity.getApplicationContext());
                        model.setArduinoConnection(new USBConnection(usbConfiguration, mainActivity));
                        checkBluetoothConnection();
                    } else if (value.equalsIgnoreCase("Bluetooth Connection")) {
                        BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(mainActivity.getApplicationContext());
                        model.setArduinoConnection(new BluetoothConnection(bluetoothConfiguration, mainActivity));
                    }
                    model.updateDataAdapter();
                }
            }

            private void checkBluetoothConnection() {
                BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(getActivity().getApplicationContext());
                if (bluetoothConfiguration.isShowCloseConnectionDialog()) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled()) {
                        mainActivity.showCloseBluetoothConnectionDialog(mBluetoothAdapter);
                    }
                }
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
    }

    private void disconnect() {
        if (model.getArduinoConnection().isConnected()) {
            model.getArduinoConnection().disconnect();
        }
    }

    private void connect() {
        model.getArduinoConnection().connect();
    }


}
