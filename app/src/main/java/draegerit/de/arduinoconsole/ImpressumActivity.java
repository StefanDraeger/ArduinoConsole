package draegerit.de.arduinoconsole;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import draegerit.de.arduinoconsole.util.HtmlUtil;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.EMAIL_ADRESS;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;

public class ImpressumActivity extends AppCompatActivity {

    private static final String TAG = "ArduinoConsole";

    private static final String EMPTY = "";

    private static final String TEXT_PLAIN = "text/plain";

    private static final String LINK_PHONE = "<a href=\"tel:+4953529096669\">05352/ 90 966 69</a>";
    public static final String TEL = "tel:";
    public static final String PHONENUMBER = "+4953529096669";

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

        this.buildNumberTextView = (TextView) findViewById(R.id.buildNumberTextView);
        String buildnumber = String.valueOf(BuildConfig.BUILD_TIMESTAMP);
        this.buildNumberTextView.setText(getResources().getString(R.string.buildversion) +" "+ String.valueOf(buildnumber));
        
        this.phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        this.phoneTextView.setText(HtmlUtil.fromHtml(LINK_PHONE));
        this.phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(TEL + PHONENUMBER));
                try {
                    startActivity(intent);
                } catch (SecurityException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }
        });

        this.emailTextView = (TextView) findViewById(R.id.emailTextView);
        this.emailTextView.setText(HtmlUtil.fromHtml(EMAIL_ADRESS));
        this.emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactIntent();
            }
        });

        this.webTextView = (TextView) findViewById(R.id.webTextView);
        this.webTextView.setText(HtmlUtil.fromHtml(HTTP_ADRESS));
        this.webTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(HTTP_ADRESS));
                startActivity(i);
            }
        });

        this.contactFloatingActionButton = (FloatingActionButton) findViewById(R.id.contactFloatingActionButton);
        this.contactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactIntent();
            }
        });
    }

    private void openContactIntent() {
        Intent emailintent = new Intent(Intent.ACTION_SEND);
        emailintent.setType(TEXT_PLAIN);
        emailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL_ADRESS});
        emailintent.putExtra(Intent.EXTRA_SUBJECT, EMPTY);
        emailintent.putExtra(Intent.EXTRA_TEXT, EMPTY);
        startActivity(Intent.createChooser(emailintent, "Contact to Draeger-IT.blog"));
    }
}
