package draegerit.de.arduinoconsole;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import draegerit.de.arduinoconsole.controller.settings.ControllerSetting;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

public class ControllerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView alertBtnUp;
    private ImageView alertBtnDown;
    private ImageView alertBtnLeft;
    private ImageView alertBtnRight;

    private ImageView alertBtnA;
    private ImageView alertBtnB;
    private ImageView alertBtnC;
    private ImageView alertBtnD;

    private ImageView alertBtnStart;
    private ImageView alertBtnSelect;

    private ImageView buttonUp;
    private ImageView buttonDown;
    private ImageView buttonLeft;
    private ImageView buttonRight;

    private TextView buttonA;
    private TextView buttonB;
    private TextView buttonC;
    private TextView buttonD;

    private TextView buttonStart;
    private TextView buttonSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        setTitle(getResources().getString(R.string.controller));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(getResources().getDrawable(R.drawable.logodraeger));

        buttonUp = (ImageView) findViewById(R.id.buttonUp);
        buttonDown = (ImageView) findViewById(R.id.buttonDown);
        buttonLeft = (ImageView) findViewById(R.id.buttonLeft);
        buttonRight = (ImageView) findViewById(R.id.buttonRight);

        buttonA = (TextView) findViewById(R.id.buttonA);
        buttonB = (TextView) findViewById(R.id.buttonB);
        buttonC = (TextView) findViewById(R.id.buttonC);
        buttonD = (TextView) findViewById(R.id.buttonD);

        buttonStart = (TextView) findViewById(R.id.buttonStart);
        buttonSelect = (TextView) findViewById(R.id.buttonSelect);

        alertBtnUp = initAlertButtonAction(R.id.alertBtnUp);
        alertBtnDown = initAlertButtonAction(R.id.alertBtnDown);
        alertBtnLeft = initAlertButtonAction(R.id.alertBtnLeft);
        alertBtnRight = initAlertButtonAction(R.id.alertBtnRight);

        alertBtnA = initAlertButtonAction(R.id.alertBtnA);
        alertBtnB = initAlertButtonAction(R.id.alertBtnB);
        alertBtnC = initAlertButtonAction(R.id.alertBtnC);
        alertBtnD = initAlertButtonAction(R.id.alertBtnD);

        alertBtnStart = initAlertButtonAction(R.id.alertBtnStart);
        alertBtnSelect = initAlertButtonAction(R.id.alertBtnSelect);

        setValues();
    }

    private void setValues() {
        ControllerSetting setting = PreferencesUtil.getControllerSettings(getApplicationContext());
        int alertBtnUpVisible = StringUtils.isBlank(setting.getCommandButtonUp())?View.VISIBLE:View.INVISIBLE;
        int alertBtnDownVisible = StringUtils.isBlank(setting.getCommandButtonDown())?View.VISIBLE:View.INVISIBLE;
        int alertBtnLeftVisible = StringUtils.isBlank(setting.getCommandButtonLeft())?View.VISIBLE:View.INVISIBLE;
        int alertBtnRightVisible = StringUtils.isBlank(setting.getCommandButtonRight())?View.VISIBLE:View.INVISIBLE;

        int alertBtnAVisible = StringUtils.isBlank(setting.getCommandButtonA())?View.VISIBLE:View.INVISIBLE;
        int alertBtnBVisible = StringUtils.isBlank(setting.getCommandButtonB())?View.VISIBLE:View.INVISIBLE;
        int alertBtnCVisible = StringUtils.isBlank(setting.getCommandButtonC())?View.VISIBLE:View.INVISIBLE;
        int alertBtnDVisible = StringUtils.isBlank(setting.getCommandButtonD())?View.VISIBLE:View.INVISIBLE;

        int alertBtnStartVisible = StringUtils.isBlank(setting.getCommandButtonStart())?View.VISIBLE:View.INVISIBLE;
        int alertBtnSelectVisible = StringUtils.isBlank(setting.getCommandButtonSelect())?View.VISIBLE:View.INVISIBLE;

        alertBtnUp.setVisibility(alertBtnUpVisible);
        alertBtnDown.setVisibility(alertBtnDownVisible);
        alertBtnLeft.setVisibility(alertBtnLeftVisible);
        alertBtnRight.setVisibility(alertBtnRightVisible);

        alertBtnA.setVisibility(alertBtnAVisible);
        alertBtnB.setVisibility(alertBtnBVisible);
        alertBtnC.setVisibility(alertBtnCVisible);
        alertBtnD.setVisibility(alertBtnDVisible);

        alertBtnStart.setVisibility(alertBtnStartVisible);
        alertBtnSelect.setVisibility(alertBtnSelectVisible);

        buttonUp.setEnabled(!StringUtils.isBlank(setting.getCommandButtonUp()));
        buttonDown.setEnabled(!StringUtils.isBlank(setting.getCommandButtonDown()));
        buttonLeft.setEnabled(!StringUtils.isBlank(setting.getCommandButtonLeft()));
        buttonRight.setEnabled(!StringUtils.isBlank(setting.getCommandButtonRight()));

        buttonA.setEnabled(!StringUtils.isBlank(setting.getCommandButtonA()));
        buttonB.setEnabled(!StringUtils.isBlank(setting.getCommandButtonB()));
        buttonC.setEnabled(!StringUtils.isBlank(setting.getCommandButtonC()));
        buttonD.setEnabled(!StringUtils.isBlank(setting.getCommandButtonD()));

        buttonStart.setEnabled(!StringUtils.isBlank(setting.getCommandButtonStart()));
        buttonSelect.setEnabled(!StringUtils.isBlank(setting.getCommandButtonSelect()));
    }

    private ImageView initAlertButtonAction(int id) {
        ImageView imgView = (ImageView) findViewById(id);
        imgView.setOnClickListener(this);
        return imgView;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
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
            case R.id.hilfeItem:
                intent = new Intent(this, HilfeActivity.class);
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
        } else if (view.equals(this.alertBtnStart)) {
            resId = R.string.txt_button_start;
        }else if (view.equals(this.alertBtnSelect)) {
            resId = R.string.txt_button_select;
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
