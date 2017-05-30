package draegerit.de.arduinoconsole;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import draegerit.de.arduinoconsole.util.HtmlUtil;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

/**
 * Klasse für die View der App.
 */
public class MainActivity extends AppCompatActivity implements Observer {

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
     * Controller
     **/
    private Controller controller;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerComponents();
        controller = new Controller(this);


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
        this.consoleTextView = (TextView) findViewById(R.id.consoleTextView);
        this.consoleScrollView = (ScrollView) findViewById(R.id.consoleScrollView);
        this.configureBtn = (ImageButton) findViewById(R.id.configureBtn);
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
    protected void onStop() {
        controller.unregisterReceiver();
        super.onStop();
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

    @Override
    public void update(final Observable o, final Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = (Model) o;
                getConsoleTextView().setText(model.getMessages());

                getConsoleScrollView().post(new Runnable() {

                    @Override
                    public void run() {
                        getConsoleScrollView().fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

                boolean selectionValid = model.getBaudrate() > 0 && model.getDriver() != null;
                getConnectBtn().setEnabled(selectionValid);

                if (model.isConnected()) {
                    getConnectBtn().setText(getResources().getString(R.string.disconnect));
                } else {
                    getConnectBtn().setText(getResources().getString(R.string.connect));
                }
            }
        });
    }
}
