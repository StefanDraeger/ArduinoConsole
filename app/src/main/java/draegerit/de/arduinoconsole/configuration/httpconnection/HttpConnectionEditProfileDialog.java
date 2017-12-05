package draegerit.de.arduinoconsole.configuration.httpconnection;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

/**
 * Created by Stefan Draeger on 19.11.2017.
 */

public class HttpConnectionEditProfileDialog extends Dialog implements View.OnClickListener {

    private HttpConnectionProfile profile;

    private EditText profileNameEditText;
    private RadioButton httpRadioButton;
    private RadioButton httpsRadioButton;
    private EditText serveradressEditText;

    private TextView testConnectionMsgTextView;
    private String shemaText;

    public HttpConnectionEditProfileDialog(@NonNull Context context, HttpConnectionProfile profile) {
        super(context);
        this.profile = profile;
        init();
    }

    private void init() {
        this.setContentView(R.layout.edit_http_profile_dialog);

        initBtnAction(R.id.testHttpConnectionBtn);
        initBtnAction(R.id.saveHttpConnectionBtn);
        initBtnAction(R.id.abortHttpConnectionBtn);

        this.profileNameEditText = (EditText) findViewById(R.id.profileNameEditText);
        this.httpRadioButton = (RadioButton) findViewById(R.id.httpRadioButton);
        this.httpsRadioButton = (RadioButton) findViewById(R.id.httpsRadioButton);
        this.serveradressEditText = (EditText) findViewById(R.id.serveradressEditText);
        this.testConnectionMsgTextView = (TextView) findViewById(R.id.testConnectionMsgTextView);

        String titel = getContext().getResources().getString(R.string.msg_addHttpProfile);
        if (this.profile != null) {
            titel = getContext().getResources().getString(R.string.msg_editHttpProfile);
            loadContent();
        }
        this.setTitle(titel);
    }

    private void loadContent() {
        this.profileNameEditText.setText(this.profile.getProfileName());
        this.httpRadioButton.setSelected(this.profile.getShema().equalsIgnoreCase(this.getContext().getResources().getString(R.string.http_txt)));
        this.httpsRadioButton.setSelected(this.profile.getShema().equalsIgnoreCase(this.getContext().getResources().getString(R.string.https_txt)));
        this.serveradressEditText.setText(this.profile.getServerAddress());
    }


    private void initBtnAction(int resId) {
        Button btn = (Button) findViewById(resId);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.testHttpConnectionBtn:
                testHttpConnection();
                break;
            case R.id.saveHttpConnectionBtn:
                saveProfile();
                break;
            case R.id.abortHttpConnectionBtn:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void saveProfile() {
        HttpConnectionProfile profile = new HttpConnectionProfile();
        profile.setProfileName(this.profileNameEditText.getText().toString());
        profile.setServerAddress(this.serveradressEditText.getText().toString());
        profile.setShema(getShemaText());
        PreferencesUtil.storeHttpConnectionProfile(getContext(), profile);
        dismiss();
        Toast.makeText(getContext(),getContext().getResources().getString(R.string.save_succesfull),Toast.LENGTH_LONG).show();
    }

    private void testHttpConnection() {
        TestConnectionAsyncTask task = new TestConnectionAsyncTask(serveradressEditText.getText().toString());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getShemaText() {
        String shemaText = getContext().getResources().getString(R.string.http_txt).toLowerCase();
        if (this.httpsRadioButton.isChecked()) {
            shemaText = getContext().getResources().getString(R.string.https_txt).toLowerCase();
        }
        return shemaText;
    }

    private void showTestConnectionMessage(boolean serverIsReachable, String serverAdress) {
        String statusMsg = getContext().getResources().getString(R.string.not_txt);
        int colorResId = R.color.textRed;
        if (serverIsReachable) {
            statusMsg = getContext().getResources().getString(R.string.empty);
            colorResId = R.color.textGreen;
        }
        this.testConnectionMsgTextView.setTextColor(getContext().getResources().getColor(colorResId));
        this.testConnectionMsgTextView.setText(getContext().getResources().getString(R.string.testconnection_msg_txt, serverAdress, statusMsg));
    }

    private class TestConnectionAsyncTask extends AsyncTask<Void, Void, Void> {

        private String serverAdress;

        private boolean serverIsReachable;

        public TestConnectionAsyncTask(String address) {
            this.serverAdress = address;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                serverIsReachable = InetAddress.getByName(this.serverAdress) != null;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            showTestConnectionMessage(serverIsReachable, serverAdress);
        }
    }
}
