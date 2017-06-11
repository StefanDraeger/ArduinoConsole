package draegerit.de.arduinoconsole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import draegerit.de.arduinoconsole.util.HtmlUtil;
import draegerit.de.arduinoconsole.util.Message;

import static android.R.color.holo_green_dark;
import static android.R.color.holo_red_dark;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

/**
 * Klasse für die View der App.
 */
public class MainActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "ArduinoConsole";

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
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config1TblRow;

    /**
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config2TblRow;

    /**
     * TableRow für die Konfiguration der Verbindung.
     */
    private TableRow config3TblRow;

    /**
     * Auswahlliste für die Auswahl eines angeschlossenes Gerätes.
     **/
    private Spinner driverSpinner;

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
        this.deviceBaudSpinner = (Spinner) findViewById(R.id.baudSpinner);
        this.connectBtn = (Button) findViewById(R.id.connectBtn);
        this.clearBtn = (ImageButton) findViewById(R.id.clearBtn);
        this.driverSpinner = (Spinner) findViewById(R.id.driverSpinner);
        this.refreshDeviceBtn = (ImageButton) findViewById(R.id.refreshDeviceBtn);
        this.databitSpinner = (Spinner) findViewById(R.id.databitSpinner);
        this.stopbitsSpinner = (Spinner) findViewById(R.id.stopbitsSpinner);
        this.paritySpinner = (Spinner) findViewById(R.id.paritySpinner);
        this.hyperlinkTextView = (TextView) findViewById(R.id.hyperlinkTextView);
        this.hyperlinkTextView.setText(HtmlUtil.fromHtml(HTTP_ADRESS));

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
                                    controller.registerDataAdapter();
                                    break;
                                case ChangeConnectionStatus:
                                    //updateConnectionStatus(model);
                                    break;
                            }
                        }
                    }
                    updateConnectionStatus(model);
                    long duration = System.currentTimeMillis() - beforeTimestamp;
                    Log.i(TAG, "duration: --->" + String.valueOf(duration) + " ms");
                } catch (Exception e)

                {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        });
    }

    private void updateConnectionStatus(Model model) {
        getCommandTextView().setEnabled(model.isConnected());
        getSendBtn().setEnabled(model.isConnected());

        if (model.isConnected()) {
            getConnectBtn().setText(getResources().getString(R.string.disconnect));
            getConnectBtn().setBackgroundColor(getResources().getColor(holo_red_dark));
        } else {
            boolean selectionValid = model.getBaudrate() > 0 && model.getDriver() != null && getDriverSpinner().getSelectedItem() != null;
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
     * Liefert die Auswahlliste {@link Spinner} für die Auswahl der Übertragungsgeschwindigkeit.
     *
     * @return {@link Spinner}
     */
    public Spinner getDeviceBaudSpinner() {
        return deviceBaudSpinner;
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
     * Liefert die erste Zeile {@link TableRow} für das Konfigurieren der Verbindungsdaten.
     *
     * @return {@link TableRow}
     */
    public TableRow getConfig1TblRow() {
        return config1TblRow;
    }

    /**
     * Liefert die zweite Zeile {@link TableRow} für das Konfigurieren der Verbindungsdaten.
     *
     * @return {@link TableRow}
     */
    public TableRow getConfig2TblRow() {
        return config2TblRow;
    }

    /**
     * Liefert die dritte Zeile {@link TableRow} für das Konfigurieren der Verbindungsdaten.
     *
     * @return {@link TableRow}
     */
    public TableRow getConfig3TblRow() {
        return config3TblRow;
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


}
