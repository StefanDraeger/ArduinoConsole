package draegerit.de.arduinoconsole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    private TextView consoleTextView;

    private Spinner driverSpinner;

    private Spinner deviceBaudSpinner;

    private Spinner databitSpinner;

    private Spinner stopbitsSpinner;

    private Spinner paritySpinner;

    private Button connectBtn;

    private ImageButton clearBtn;

    private ImageButton refreshDeviceBtn;

    private TextView hyperlinkTextView;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerComponents();
        controller = new Controller(this);

        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setIcon(R.mipmap.logodraeger);
    }

    private void registerComponents() {
        this.consoleTextView = (TextView) findViewById(R.id.consoleTextView);
        this.deviceBaudSpinner = (Spinner) findViewById(R.id.baudSpinner);
        this.connectBtn = (Button) findViewById(R.id.connectBtn);
        this.clearBtn = (ImageButton) findViewById(R.id.clearBtn);
        this.driverSpinner = (Spinner) findViewById(R.id.driverSpinner);
        this.refreshDeviceBtn = (ImageButton) findViewById(R.id.refreshDeviceBtn);
        this.databitSpinner = (Spinner) findViewById(R.id.databitSpinner);
        this.stopbitsSpinner = (Spinner) findViewById(R.id.stopbitsSpinner);
        this.paritySpinner = (Spinner) findViewById(R.id.paritySpinner);
        this.hyperlinkTextView = (TextView) findViewById(R.id.hyperlinkTextView);
        this.hyperlinkTextView.setText(Html.fromHtml("https://www.draeger-it.blog"));

        this.connectBtn.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        controller.unregisterReceiver();
        super.onStop();
    }


    public TextView getConsoleTextView() {
        return consoleTextView;
    }

    public void setConsoleTextView(TextView consoleTextView) {
        this.consoleTextView = consoleTextView;
    }

    public Spinner getDriverSpinner() {
        return driverSpinner;
    }

    public void setDriverSpinner(Spinner driverSpinner) {
        this.driverSpinner = driverSpinner;
    }

    public Spinner getDeviceBaudSpinner() {
        return deviceBaudSpinner;
    }

    public void setDeviceBaudSpinner(Spinner deviceBaudSpinner) {
        this.deviceBaudSpinner = deviceBaudSpinner;
    }

    public Button getConnectBtn() {
        return connectBtn;
    }

    public void setConnectBtn(Button connectBtn) {
        this.connectBtn = connectBtn;
    }

    public ImageButton getClearBtn() {
        return clearBtn;
    }

    public void setClearBtn(ImageButton clearBtn) {
        this.clearBtn = clearBtn;
    }

    public ImageButton getRefreshDeviceBtn() {
        return refreshDeviceBtn;
    }

    public Spinner getDatabitSpinner() {
        return databitSpinner;
    }

    public void setDatabitSpinner(Spinner databitSpinner) {
        this.databitSpinner = databitSpinner;
    }

    public Spinner getStopbitsSpinner() {
        return stopbitsSpinner;
    }

    public void setStopbitsSpinner(Spinner stopbitsSpinner) {
        this.stopbitsSpinner = stopbitsSpinner;
    }

    public Spinner getParitySpinner() {
        return paritySpinner;
    }

    public void setParitySpinner(Spinner paritySpinner) {
        this.paritySpinner = paritySpinner;
    }

    public void setRefreshDeviceBtn(ImageButton refreshDeviceBtn) {
        this.refreshDeviceBtn = refreshDeviceBtn;
    }

    public TextView getHyperlinkTextView() {
        return hyperlinkTextView;
    }

    public void setHyperlinkTextView(TextView hyperlinkTextView) {
        this.hyperlinkTextView = hyperlinkTextView;
    }

    @Override
    public void update(final Observable o, Object arg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Model model = (Model) o;
                getConsoleTextView().setText(model.getMessages());

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
