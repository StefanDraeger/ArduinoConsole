package draegerit.de.arduinoconsole;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import draegerit.de.arduinoconsole.connection.BluetoothConnection;
import draegerit.de.arduinoconsole.connection.USBConnection;
import draegerit.de.arduinoconsole.util.DriverWrapper;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.configuration.TerminalConfiguration;
import draegerit.de.arduinoconsole.util.configuration.USBConfiguration;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

class MainController extends AbstractController {

    public static final String EMPTY = "";
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
                mainActivity.getConsoleTextView().setText(EMPTY);
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
                if (model.getArduinoConnection() != null && model.getArduinoConnection().isConnected()) {
                    TerminalConfiguration terminalConfiguration = PreferencesUtil.getTerminalConfiguration(getActivity().getApplicationContext());
                    if (!terminalConfiguration.isAllowSendEmptyMessages() && command.trim().length() == 0) {
                        MessageHandler.showErrorMessage(mainActivity, mainActivity.getResources().getString(R.string.msg_emptycommands));
                    } else {
                        model.getArduinoConnection().sendCommand(command);
                    }
                } else {
                    MessageHandler.showErrorMessage(mainActivity, mainActivity.getResources().getString(R.string.msg_no_connection));
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
                    BroadcastReceiver receiver = model.getArduinoConnection().getBroadcastReceiver();
                    if (receiver != null && model.getArduinoConnection().isReciverIsRegistered()) {
                        try {
                            mainActivity.unregisterReceiver(receiver);
                        } catch (IllegalArgumentException ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }

                    if (value.equalsIgnoreCase(mainActivity.getResources().getString(R.string.usb_serial_connection))) {
                        USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(mainActivity.getApplicationContext());
                        model.setArduinoConnection(new USBConnection(usbConfiguration, mainActivity));
                        checkBluetoothConnection();
                    } else if (value.equalsIgnoreCase(mainActivity.getResources().getString(R.string.bluetooth_connection))) {
                        BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(mainActivity.getApplicationContext());
                        model.setArduinoConnection(new BluetoothConnection(bluetoothConfiguration, mainActivity));

                        if (bluetoothConfiguration.isShowSearchNewDevicesDialog()) {
                            mainActivity.searchNewDevices();
                        }
                    } else if (value.equalsIgnoreCase(mainActivity.getResources().getString(R.string.http_connection))) {
                        MessageHandler.showCommingSoonMsg(mainActivity);
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
            mainActivity.releaseWakeLock();
        }
    }

    private void connect() {
        model.getArduinoConnection().connect();
        mainActivity.setWakeLock();
    }


}
