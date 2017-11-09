package draegerit.de.arduinoconsole;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class HilfeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BASE_URL = "http://draeger-it.blog/android-app-arduinoconsole/";

    private static final String OVERVIEW_URL = "#Uebersicht";
    private static final String SERIAL_CONNECTION_URL = "#Serielle_Verbindung";
    private static final String BLUETOOTH_CONNECTION_URL = "#Bluetooth_Verbindung";

    private static final String GRAPH_CONFIG_URL = "#konfiguration";
    private static final String GRAPH_EXPORT_URL = "#exportieren";

    private static final String CONTROLLER_OVERVIEW_URL = "#Uebersicht-2";
    private static final String CONTROLLER_CONFIGURATION_URL ="#Konfiguration-2";

    private static final String CONFIG_TAB_GENERAL_URL = "#Tab_8211_General";
    private static final String CONFIG_TAB_CONNECTION_URL = "#Tab_8211_Connection";
    private static final String CONFIG_TAB_TERMINAL_URL = "#Tab_8211_Terminal";

    private TextView overviewTextView;

    private TextView serialconnectionTextView;
    private TextView bluetoothconnectionTextView;

    private TextView hilfeControllerOverviewTextView;
    private TextView hilfeControllerConfigTextView;

    private TextView configureGraphTextView;
    private TextView exportGraphTextView;

    private TextView hilfeConfigGeneralTextView;
    private TextView hilfeConfigConnectionTextView;
    private TextView hilfeConfigTerminalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);
        setTitle(getResources().getString(R.string.hilfe));

        overviewTextView = initLinkTextView(R.id.overviewTextView);

        serialconnectionTextView = initLinkTextView(R.id.serialconnectionTextView);
        bluetoothconnectionTextView = initLinkTextView(R.id.bluetoothconnectionTextView);

        hilfeControllerOverviewTextView = initLinkTextView(R.id.hilfeControllerOverviewTextView);
        hilfeControllerConfigTextView = initLinkTextView(R.id.hilfeControllerConfigTextView);

        configureGraphTextView = initLinkTextView(R.id.configureGraphTextView);
        exportGraphTextView = initLinkTextView(R.id.exportGraphTextView);

        hilfeConfigGeneralTextView = initLinkTextView(R.id.hilfeConfigGeneralTextView);
        hilfeConfigConnectionTextView = initLinkTextView(R.id.hilfeConfigConnectionTextView);
        hilfeConfigTerminalTextView = initLinkTextView(R.id.hilfeConfigTerminalTextView);

    }

    private TextView initLinkTextView(int id) {
        TextView textView = (TextView) findViewById(id);
        textView.setOnClickListener(this);
        return textView;
    }

    private void openUrl(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(BASE_URL + url));
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(this.serialconnectionTextView)) {
            openUrl(SERIAL_CONNECTION_URL);
        } else if (view.equals(this.bluetoothconnectionTextView)) {
            openUrl(BLUETOOTH_CONNECTION_URL);
        } else if (view.equals(this.configureGraphTextView)) {
            openUrl(GRAPH_CONFIG_URL);
        } else if (view.equals(this.exportGraphTextView)) {
            openUrl(GRAPH_EXPORT_URL);
        } else if (view.equals(this.overviewTextView)) {
            openUrl(OVERVIEW_URL);
        }else if (view.equals(this.hilfeConfigGeneralTextView)) {
            openUrl(CONFIG_TAB_GENERAL_URL);
        }else if (view.equals(this.hilfeConfigConnectionTextView)) {
            openUrl(CONFIG_TAB_CONNECTION_URL);
        }else if (view.equals(this.hilfeConfigTerminalTextView)) {
            openUrl(CONFIG_TAB_TERMINAL_URL);
        }else if (view.equals(this.hilfeControllerOverviewTextView)) {
            openUrl(CONTROLLER_OVERVIEW_URL);
        }else if (view.equals(this.hilfeControllerConfigTextView)) {
            openUrl(CONTROLLER_CONFIGURATION_URL);
        }
    }
}
