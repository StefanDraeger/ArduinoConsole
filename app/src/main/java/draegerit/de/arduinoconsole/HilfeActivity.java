package draegerit.de.arduinoconsole;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class HilfeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BASE_URL = "http://draeger-it.blog/android-app-arduinoconsole/";

    private static final String OVERVIEW_URL = "#Uebersicht";
    private static final String SERIAL_CONNECTION_URL = "#Serielle_Verbindung";
    private static final String BLUETOOTH_CONNECTION_URL = "#Bluetooth_Verbindung";

    private static final String GRAPH_CREATE_URL = "#erzeugen";
    private static final String GRAPH_CONFIG_URL = "#konfiguration";
    private static final String GRAPH_EXPORT_URL = "#exportieren";


    private TextView overviewTextView;

    private TextView serialconnectionTextView;
    private TextView bluetoothconnectionTextView;

    private TextView createGraphTextView;
    private TextView configureGraphTextView;
    private TextView exportGraphTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hilfe);
        setTitle(getResources().getString(R.string.hilfe));

        overviewTextView = initLinkTextView(R.id.overviewTextView);

        serialconnectionTextView = initLinkTextView(R.id.serialconnectionTextView);
        bluetoothconnectionTextView = initLinkTextView(R.id.bluetoothconnectionTextView);

        createGraphTextView = initLinkTextView(R.id.createGraphTextView);
        configureGraphTextView = initLinkTextView(R.id.configureGraphTextView);
        exportGraphTextView = initLinkTextView(R.id.exportGraphTextView);
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
        } else if (view.equals(this.createGraphTextView)) {
            openUrl(GRAPH_CREATE_URL);
        } else if (view.equals(this.configureGraphTextView)) {
            openUrl(GRAPH_CONFIG_URL);
        } else if (view.equals(this.exportGraphTextView)) {
            openUrl(GRAPH_EXPORT_URL);
        } else if (view.equals(this.overviewTextView)) {
            openUrl(OVERVIEW_URL);
        }
    }
}
