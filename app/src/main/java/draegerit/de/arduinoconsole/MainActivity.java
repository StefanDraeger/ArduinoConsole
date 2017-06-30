package draegerit.de.arduinoconsole;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import draegerit.de.arduinoconsole.connection.AbstractBluetoothConnection;
import draegerit.de.arduinoconsole.connection.BluetoothConnection;
import draegerit.de.arduinoconsole.connection.BluetoothLEConnection;
import draegerit.de.arduinoconsole.util.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.GeneralConfiguration;
import draegerit.de.arduinoconsole.util.HtmlUtil;
import draegerit.de.arduinoconsole.util.Message;
import draegerit.de.arduinoconsole.util.MessageHandler;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.USBConfiguration;

import static android.R.color.holo_green_dark;
import static android.R.color.holo_red_dark;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

/**
 * Klasse für die View der App.
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "ArduinoConsole";

    /**
     *
     */
    protected PowerManager.WakeLock mWakeLock;

    /**
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config1TblRow;

    /**
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config3TblRow;

    /**
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config2TblRow;

    /**
     * Konsole.
     **/
    private TextView consoleTextView;

    /**
     * Scrollview für die Konsole.
     */
    private ScrollView consoleScrollView;

    /**
     * Schaltfläche für das Anzeigen / Verstecken der Verbindungsdaten.
     */
    private ImageButton configureBtn;

    /**
     * Checkbox für das aktivieren / deaktivieren des Autoscroll in der ScrollView der Konsole.
     */
    private CheckBox autoScrollCheckbox;

    /**
     * Auswahlliste für die Auswahl eines angeschlossenes Gerätes.
     **/
    private Spinner driverSpinner;

    /**
     * Schaltfläche für das Verbinden / trennen der Verbindung zu einem Gerät.
     **/
    private Button connectBtn;

    /**
     * Schaltfläche für das leeren der Konsole.
     **/
    private ImageButton clearBtn;

    /**
     * Schaltfläche für das aktualisieren der Auswahlliste für die angeschlossenen Geräte.
     **/
    private ImageButton refreshDeviceBtn;

    /**
     * Der Hyperlink zur Webseite des Entwicklers.
     **/
    private TextView hyperlinkTextView;

    /**
     * Eingabefeld für das absenden eines Befehls an den Arduino.
     */
    private EditText commandTextView;

    /**
     * Schaltfläche für das Absenden des eingegebenen Befehls.
     */
    private Button sendBtn;

    /**
     * Auswahlliste für den Verbindungstyp zum Arduino.
     */
    private Spinner deviceConnectionTypeSpinner;

    /**
     * MainController
     **/
    private MainController controller;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerComponents();
        controller = new MainController(this);

        ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setIcon(R.mipmap.logodraeger);
        }


    }


    /**
     * Erzeugt die Komponenten der View.
     */
    private void registerComponents() {
        this.commandTextView = (EditText) findViewById(R.id.commandTextView);
        this.sendBtn = (Button) findViewById(R.id.sendBtn);
        this.consoleTextView = (TextView) findViewById(R.id.consoleTextView);
        this.consoleScrollView = (ScrollView) findViewById(R.id.consoleScrollView);
        this.autoScrollCheckbox = (CheckBox) findViewById(R.id.autoScrollCheckbox);
        this.configureBtn = (ImageButton) findViewById(R.id.configureBtn);
        this.config1TblRow = (TableRow) findViewById(R.id.config1TblRow);
        this.config2TblRow = (TableRow) findViewById(R.id.config2TblRow);
        this.config3TblRow = (TableRow) findViewById(R.id.config3TblRow);

        this.connectBtn = (Button) findViewById(R.id.connectBtn);
        this.clearBtn = (ImageButton) findViewById(R.id.clearBtn);
        this.driverSpinner = (Spinner) findViewById(R.id.driverSpinner);
        this.refreshDeviceBtn = (ImageButton) findViewById(R.id.refreshDeviceBtn);

        this.hyperlinkTextView = (TextView) findViewById(R.id.hyperlinkTextView);
        this.hyperlinkTextView.setText(HtmlUtil.fromHtml(HTTP_ADRESS));

        this.deviceConnectionTypeSpinner = (Spinner) findViewById(R.id.deviceConnectionTypeSpinner);

        this.connectBtn.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.impressumItem:
                intent = new Intent(this, ImpressumActivity.class);
                break;
            case R.id.graphItem:
                intent = new Intent(this, GraphActivity.class);
                break;
            case R.id.configurationItem:
                intent = new Intent(this, ConfigurationActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Item with ID [" + item.getItemId() + "] not found!");
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        controller.unregisterReceiver();
        super.onStop();
    }

    @Override
    public synchronized void update(final Observable o, final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Model model = (Model) o;

                    long beforeTimestamp = System.currentTimeMillis();
                    if (arg instanceof Message) {
                        Message msg = (Message) arg;
                        getConsoleTextView().append(msg.getValue());
                        if (model.isAutoScroll()) {
                            getConsoleScrollView().fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    } else if (arg instanceof ArduinoConsoleStatics.ActionCommand) {
                        ArduinoConsoleStatics.ActionCommand command = (ArduinoConsoleStatics.ActionCommand) arg;
                        if (controller != null) {
                            switch (command) {
                                case UpdateUsbDevice:
                                    model.getArduinoConnection().registerDataAdapter();
                                    break;
                                case ChangeConnectionStatus:
                                    //updateConnectionStatus(model);
                                    break;
                                case UpdateBluetoothAdapter:
                                    ((BluetoothConnection) model.getArduinoConnection()).postConnect();
                                    break;
                            }
                        }
                    }
                    updateConnectionStatus(model);
                    long duration = System.currentTimeMillis() - beforeTimestamp;
                    Log.i(TAG, "duration: --->" + String.valueOf(duration) + " ms");
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }

    private void updateConnectionStatus(Model model) {
        getCommandTextView().setEnabled(model.getArduinoConnection().isConnected());
        getSendBtn().setEnabled(model.getArduinoConnection().isConnected());

        if (model.getArduinoConnection().isConnected()) {
            getConnectBtn().setText(getResources().getString(R.string.disconnect));
            getConnectBtn().setBackgroundColor(getResources().getColor(holo_red_dark));
        } else {
            USBConfiguration usbConfiguration = PreferencesUtil.getUSBConfiguration(getApplicationContext());
            boolean selectionValid = usbConfiguration.getBaudrate() > 0 && model.getDriver() != null && getDriverSpinner().getSelectedItem() != null;
            if (selectionValid) {
                getConnectBtn().setBackgroundColor(getResources().getColor(holo_green_dark));
            } else {
                getConnectBtn().setBackground(getSendBtn().getBackground());
            }
            getConnectBtn().setEnabled(selectionValid);
            getConnectBtn().setText(getResources().getString(R.string.connect));
        }
    }

    /**
     * Liefert die erste Zeile {@link TableRow} für das Konfigurieren der Verbindungsdaten.
     *
     * @return {@link TableRow}
     */
    public TableRow getConfig1TblRow() {
        return config1TblRow;
    }

    /**
     * Liefert die {@link TextView} für die Konsole.
     *
     * @return {@link TextView}
     */
    public TextView getConsoleTextView() {
        return consoleTextView;
    }

    /**
     * Liefert die Auswahlliste {@link Spinner} für die Auswahl des Gerätes.
     *
     * @return {@link Spinner}
     */
    public Spinner getDriverSpinner() {
        return driverSpinner;
    }


    /**
     * Liefert die Schaltfläche {@link Button} für das erzeugen / trennen einer Verbindung zu einem angeschlossenen Gerät.
     *
     * @return {@link Button}
     */
    public Button getConnectBtn() {
        return connectBtn;
    }

    /**
     * Liefert die Schaltfläche {@link ImageButton} für das leeren der Konsole.
     *
     * @return {@link ImageButton}
     */
    public ImageButton getClearBtn() {
        return clearBtn;
    }

    /**
     * Liefert die Schaltfläche {@link ImageButton} für das aktualisieren der Geräteauswahlliste.
     *
     * @return {@link ImageButton}
     */
    public ImageButton getRefreshDeviceBtn() {
        return refreshDeviceBtn;
    }


    /**
     * Liefert die {@link TextView} für den Hyperlink zur Webseite des Entwicklers.
     *
     * @return {@link TextView}
     */
    public TextView getHyperlinkTextView() {
        return hyperlinkTextView;
    }

    /**
     * Liefert die {@link ScrollView} für die Konsole.
     *
     * @return {@link ScrollView}
     */
    public ScrollView getConsoleScrollView() {
        return consoleScrollView;
    }

    /**
     * Liefert die Schaltfläche {@link ImageButton} für das Anzeigen / verstecken der Verbindungsdaten.
     *
     * @return {@link ImageButton}
     */
    public ImageButton getConfigureBtn() {
        return configureBtn;
    }

    /**
     * Liefert die {@link CheckBox} für das aktivieren / deaktivieren des Autoscrolls in der Konsole.
     *
     * @return {@link CheckBox}
     */
    public CheckBox getAutoScrollCheckbox() {
        return autoScrollCheckbox;
    }

    /**
     * Liefert die Schaltfläche {@link Button} für das Absenden eines Befehls an den Arduino.
     *
     * @return {@link Button}
     */
    public Button getSendBtn() {
        return sendBtn;
    }

    /**
     * Liefert das Eingabefeld {@link EditText} für das eingeben eines Befehls.
     *
     * @return {@link EditText}
     */
    public EditText getCommandTextView() {
        return commandTextView;
    }


    public TableRow getConfig3TblRow() {
        return config3TblRow;
    }

    public void setConfig3TblRow(TableRow config3TblRow) {
        this.config3TblRow = config3TblRow;
    }

    public TableRow getConfig2TblRow() {
        return config2TblRow;
    }

    public void setConfig2TblRow(TableRow config2TblRow) {
        this.config2TblRow = config2TblRow;
    }

    public Spinner getDeviceConnectionTypeSpinner() {
        return deviceConnectionTypeSpinner;
    }

    public void setDeviceConnectionTypeSpinner(Spinner deviceConnectionTypeSpinner) {
        this.deviceConnectionTypeSpinner = deviceConnectionTypeSpinner;
    }

    public void showCloseBluetoothConnectionDialog(final BluetoothAdapter mBluetoothAdapter) {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        dialog.setContentView(R.layout.closebluetoothadapterdialog);
        dialog.setTitle(getResources().getString(R.string.msg_close_bluetooth_connection));

        Button dialogAbortButton = (Button) dialog.findViewById(R.id.dialogButtonAbort);
        dialogAbortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogDisableButton = (Button) dialog.findViewById(R.id.dialogButtonDisable);
        dialogDisableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter.disable();
                dialog.dismiss();
            }
        });

        final CheckBox dontShowDialogAgainCheckbox = (CheckBox) dialog.findViewById(R.id.dontShowDialogAgainCheckbox);
        dontShowDialogAgainCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(getApplicationContext());
                bluetoothConfiguration.setShowCloseConnectionDialog(!dontShowDialogAgainCheckbox.isChecked());
                PreferencesUtil.storeBluetoothConfiguration(getApplicationContext(), bluetoothConfiguration);
            }
        });

        dialog.show();
    }

    public BluetoothSocket getBluetoothSocket(BluetoothDevice device) {
        BluetoothSocket socket = null;
        try {
            ConnectionAsyncTask task = new ConnectionAsyncTask(device);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return socket;
    }

    public void searchNewDevices() {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        dialog.setContentView(R.layout.searchnewdevicesquestiondialog);
        dialog.setTitle(getResources().getString(R.string.msg_search_new_devices));

        Button yesButton = (Button) dialog.findViewById(R.id.yesButton);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model model = Model.getInstance();
                ((AbstractBluetoothConnection) model.getArduinoConnection()).findUnPairedBluetoothDevices();
                dialog.dismiss();
            }
        });

        Button noButton = (Button) dialog.findViewById(R.id.noButton);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final CheckBox dontShowSearchNewDevicesChkBox = (CheckBox) dialog.findViewById(R.id.dontShowSearchNewDevicesChkBox);
        dontShowSearchNewDevicesChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BluetoothConfiguration bluetoothConfiguration = PreferencesUtil.getBluetoothConfiguration(getApplicationContext());
                bluetoothConfiguration.setShowSearchNewDevicesDialog(!dontShowSearchNewDevicesChkBox.isChecked());
                PreferencesUtil.storeBluetoothConfiguration(getApplicationContext(), bluetoothConfiguration);
            }
        });

        dialog.show();
    }

    public void findNonBondedBluetoothLEDevices(final BluetoothLEConnection bluetoothLEConnection) {
        final long SCAN_PERIOD = 10000;

        final BluetoothAdapter.LeScanCallback mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi,
                                         byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bluetoothLEConnection.getBluetoothDevices().add(device);
                            }
                        });
                    }
                };

        Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothLEConnection.getBluetoothAdapter().stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);


        bluetoothLEConnection.getBluetoothAdapter().startLeScan(mLeScanCallback);
    }

    public void showConnectionError(String name) {
        MessageHandler.showErrorMessage(this, getResources().getString(R.string.connection_error, name));
    }

    private class ConnectionAsyncTask extends AsyncTask<Void, Void, BluetoothSocket> {

        //Die eindeutige ID darf nicht geändert werden.
        //TODO: Warum?
        public static final String DEFAULT_UUID = "00001101-0000-1000-8000-00805F9B34FB";

        private ProgressDialog progressDialog;

        private BluetoothSocket bluetoothSocket;
        private final BluetoothDevice bluetoothDevice;

        private Model model = Model.getInstance();


        public ConnectionAsyncTask(BluetoothDevice device) {
            this.bluetoothDevice = device;
        }

        @Override
        protected BluetoothSocket doInBackground(Void... params) {
            try {
                bluetoothSocket = this.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(DEFAULT_UUID));
                if (bluetoothSocket != null) {
                    bluetoothSocket.connect();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bluetoothSocket;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(getString(R.string.msg_title_create_connection));
            progressDialog.setMessage(getString(R.string.msg_msg_create_connection, bluetoothDevice.getName()));
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            super.onPostExecute(socket);
            progressDialog.dismiss();
            ((BluetoothConnection) model.getArduinoConnection()).setSocket(bluetoothSocket);
        }
    }

    public void setWakeLock() {
        GeneralConfiguration generalConfiguration = PreferencesUtil.getGeneralConfiguration(getApplicationContext());
        if (generalConfiguration.isStayAwakeWhileConnected()) {
            final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
            this.mWakeLock.acquire();
        } else {
            releaseWakeLock();
        }
    }

    public void releaseWakeLock() {
        try {
            if (this.mWakeLock != null) {
                this.mWakeLock.release();
            }
        } catch (RuntimeException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        releaseWakeLock();
        super.onDestroy();
    }
}
