package draegerit.de.arduinoconsole;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ControllerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView alertBtnUp;
    private ImageView alertBtnDown;
    private ImageView alertBtnLeft;
    private ImageView alertBtnRight;

    private ImageView alertBtnA;
    private ImageView alertBtnB;
    private ImageView alertBtnC;
    private ImageView alertBtnD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        setTitle(getResources().getString(R.string.controller));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(getResources().getDrawable(R.drawable.logodraeger));

        alertBtnUp = initAlertButtonAction(R.id.alertBtnUp);
        alertBtnDown = initAlertButtonAction(R.id.alertBtnDown);
        alertBtnLeft = initAlertButtonAction(R.id.alertBtnLeft);
        alertBtnRight = initAlertButtonAction(R.id.alertBtnRight);

        alertBtnA = initAlertButtonAction(R.id.alertBtnA);
        alertBtnB = initAlertButtonAction(R.id.alertBtnB);
        alertBtnC = initAlertButtonAction(R.id.alertBtnC);
        alertBtnD = initAlertButtonAction(R.id.alertBtnD);
    }

    private ImageView initAlertButtonAction(int id) {
        ImageView imgView = (ImageView) findViewById(id);
        imgView.setOnClickListener(this);
        return imgView;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_edit:
                intent = new Intent(this, ControllerSettingsActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        showAlertMessage(view);
    }

    private void showAlertMessage(View view) {
        int resId = -1;
        if (view.equals(this.alertBtnA)) {
            resId = R.string.buttona;
        } else if (view.equals(this.alertBtnB)) {
            resId = R.string.buttonb;
        } else if (view.equals(this.alertBtnC)) {
            resId = R.string.buttonc;
        } else if (view.equals(this.alertBtnD)) {
            resId = R.string.buttond;
        } else if (view.equals(this.alertBtnUp)) {
            resId = R.string.buttonUp;
        } else if (view.equals(this.alertBtnDown)) {
            resId = R.string.buttonDown;
        } else if (view.equals(this.alertBtnLeft)) {
            resId = R.string.buttonLeft;
        } else if (view.equals(this.alertBtnRight)) {
            resId = R.string.buttonRight;
        }

        if (resId != -1) {
            String alertMessage = getResources().getString(R.string.no_function_for_command, getResources().getString(resId));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    ControllerActivity.this);

            alertDialogBuilder.setTitle(getResources().getString(R.string.alert));

            alertDialogBuilder
                    .setMessage(alertMessage)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.abort), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
