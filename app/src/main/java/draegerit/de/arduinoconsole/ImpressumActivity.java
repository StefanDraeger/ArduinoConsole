package draegerit.de.arduinoconsole;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ImpressumActivity extends AppCompatActivity {

    private TextView buildNumberTextView;

    private TextView phoneTextView;

    private TextView emailTextView;

    private TextView webTextView;

    private FloatingActionButton contactFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressum);
        setTitle(getResources().getString(R.string.impressum));

        loadComponents();

        ImpressumController controller = new ImpressumController(this);
        controller.registerComponents();
    }

    private void loadComponents() {
        this.buildNumberTextView = (TextView) findViewById(R.id.buildNumberTextView);
        this.phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        this.emailTextView = (TextView) findViewById(R.id.emailTextView);
        this.webTextView = (TextView) findViewById(R.id.webTextView);
        this.contactFloatingActionButton = (FloatingActionButton) findViewById(R.id.contactFloatingActionButton);
    }

    public TextView getBuildNumberTextView() {
        return buildNumberTextView;
    }

    public TextView getPhoneTextView() {
        return phoneTextView;
    }

    public TextView getEmailTextView() {
        return emailTextView;
    }

    public TextView getWebTextView() {
        return webTextView;
    }

    public FloatingActionButton getContactFloatingActionButton() {
        return contactFloatingActionButton;
    }

}
