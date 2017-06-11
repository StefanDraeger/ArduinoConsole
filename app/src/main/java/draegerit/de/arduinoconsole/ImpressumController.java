package draegerit.de.arduinoconsole;


import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import draegerit.de.arduinoconsole.util.HtmlUtil;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.EMAIL_ADRESS;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.EMPTY;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.HTTP_ADRESS;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.LINK_PHONE;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.PHONENUMBER;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.TEL;
import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.TEXT_PLAIN;

public class ImpressumController {

    private static final String TAG = "ArduinoConsole";

    private ImpressumActivity impressumActivity;

    public ImpressumController(ImpressumActivity impressumActivity) {
        this.impressumActivity = impressumActivity;
    }

    public void registerComponents() {
        String buildnumber = String.valueOf(BuildConfig.BUILD_TIMESTAMP);
        getImpressumActivity().getBuildNumberTextView().setText(getImpressumActivity().getResources().getString(R.string.buildversion) + " " + String.valueOf(buildnumber));


        getImpressumActivity().getPhoneTextView().setText(HtmlUtil.fromHtml(LINK_PHONE));
        /**
         getImpressumActivity().getPhoneTextView().setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(TEL + PHONENUMBER));
        try {
        getImpressumActivity().startActivity(intent);
        } catch (SecurityException ex) {
        Log.e(TAG, ex.getMessage(), ex);
        }
        }
        });
         **/

        getImpressumActivity().getEmailTextView().setText(HtmlUtil.fromHtml(EMAIL_ADRESS));
        getImpressumActivity().getEmailTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactIntent();
            }
        });


        getImpressumActivity().getWebTextView().setText(HtmlUtil.fromHtml(HTTP_ADRESS));
        getImpressumActivity().getWebTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(HTTP_ADRESS));
                getImpressumActivity().startActivity(i);
            }
        });

        getImpressumActivity().getContactFloatingActionButton().setOnClickListener(new View.OnClickListener() {
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
        getImpressumActivity().startActivity(Intent.createChooser(emailintent, "Contact to Draeger-IT.blog"));
    }

    public ImpressumActivity getImpressumActivity() {
        return impressumActivity;
    }
}
