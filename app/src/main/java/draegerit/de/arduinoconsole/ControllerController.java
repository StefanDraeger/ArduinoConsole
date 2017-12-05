package draegerit.de.arduinoconsole;

import android.view.View;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import draegerit.de.arduinoconsole.controller.settings.ControllerSetting;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

/**
 * Created by Stefan Draeger on 05.12.2017.
 */

public class ControllerController implements View.OnClickListener {

    public static final String UNDEFINED = "-undefined-";
    private Model model = Model.getInstance();
    private ControllerActivity activity;

    public ControllerController(ControllerActivity controllerActivity) {
        this.activity = controllerActivity;
    }

    public void setDefaultValues() {
        ControllerSetting setting = PreferencesUtil.getControllerSettings(this.activity.getApplicationContext());
        int alertBtnUpVisible = StringUtils.isBlank(setting.getCommandButtonUp()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnDownVisible = StringUtils.isBlank(setting.getCommandButtonDown()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnLeftVisible = StringUtils.isBlank(setting.getCommandButtonLeft()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnRightVisible = StringUtils.isBlank(setting.getCommandButtonRight()) ? View.VISIBLE : View.INVISIBLE;

        int alertBtnAVisible = StringUtils.isBlank(setting.getCommandButtonA()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnBVisible = StringUtils.isBlank(setting.getCommandButtonB()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnCVisible = StringUtils.isBlank(setting.getCommandButtonC()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnDVisible = StringUtils.isBlank(setting.getCommandButtonD()) ? View.VISIBLE : View.INVISIBLE;

        int alertBtnStartVisible = StringUtils.isBlank(setting.getCommandButtonStart()) ? View.VISIBLE : View.INVISIBLE;
        int alertBtnSelectVisible = StringUtils.isBlank(setting.getCommandButtonSelect()) ? View.VISIBLE : View.INVISIBLE;

        this.activity.getAlertBtnUp().setVisibility(alertBtnUpVisible);
        this.activity.getAlertBtnDown().setVisibility(alertBtnDownVisible);
        this.activity.getAlertBtnLeft().setVisibility(alertBtnLeftVisible);
        this.activity.getAlertBtnRight().setVisibility(alertBtnRightVisible);

        this.activity.getAlertBtnA().setVisibility(alertBtnAVisible);
        this.activity.getAlertBtnB().setVisibility(alertBtnBVisible);
        this.activity.getAlertBtnC().setVisibility(alertBtnCVisible);
        this.activity.getAlertBtnD().setVisibility(alertBtnDVisible);

        this.activity.getAlertBtnStart().setVisibility(alertBtnStartVisible);
        this.activity.getAlertBtnSelect().setVisibility(alertBtnSelectVisible);

        this.activity.getButtonUp().setEnabled(!StringUtils.isBlank(setting.getCommandButtonUp()));
        this.activity.getButtonDown().setEnabled(!StringUtils.isBlank(setting.getCommandButtonDown()));
        this.activity.getButtonLeft().setEnabled(!StringUtils.isBlank(setting.getCommandButtonLeft()));
        this.activity.getButtonRight().setEnabled(!StringUtils.isBlank(setting.getCommandButtonRight()));

        this.activity.getButtonA().setEnabled(!StringUtils.isBlank(setting.getCommandButtonA()));
        this.activity.getButtonB().setEnabled(!StringUtils.isBlank(setting.getCommandButtonB()));
        this.activity.getButtonC().setEnabled(!StringUtils.isBlank(setting.getCommandButtonC()));
        this.activity.getButtonD().setEnabled(!StringUtils.isBlank(setting.getCommandButtonD()));

        this.activity.getButtonStart().setEnabled(!StringUtils.isBlank(setting.getCommandButtonStart()));
        this.activity.getButtonSelect().setEnabled(!StringUtils.isBlank(setting.getCommandButtonSelect()));
    }

    @Override
    public void onClick(View view) {
        showAlertMessage(view);
        executeButtonAction(view);
    }

    private void executeButtonAction(View view) {
        ControllerSetting controllerSettings = PreferencesUtil.getControllerSettings(this.activity.getApplicationContext());

        String command = UNDEFINED;
        if (view.equals(this.activity.getButtonA())) {
            command = controllerSettings.getCommandButtonA();
        } else if (view.equals(this.activity.getButtonB())) {
            command = controllerSettings.getCommandButtonB();
        } else if (view.equals(this.activity.getButtonC())) {
            command = controllerSettings.getCommandButtonC();
        } else if (view.equals(this.activity.getButtonD())) {
            command = controllerSettings.getCommandButtonD();
        } else if (view.equals(this.activity.getButtonUp())) {
            command = controllerSettings.getCommandButtonUp();
        } else if (view.equals(this.activity.getButtonDown())) {
            command = controllerSettings.getCommandButtonDown();
        } else if (view.equals(this.activity.getButtonLeft())) {
            command = controllerSettings.getCommandButtonLeft();
        } else if (view.equals(this.activity.getButtonRight())) {
            command = controllerSettings.getCommandButtonRight();
        } else if (view.equals(this.activity.getButtonStart())) {
            command = controllerSettings.getCommandButtonStart();
        } else if (view.equals(this.activity.getButtonSelect())) {
            command = controllerSettings.getCommandButtonSelect();
        }

        if (!command.equalsIgnoreCase(UNDEFINED) && !model.getArduinoConnection().isConnected()) {
            Toast.makeText(this.activity.getApplicationContext(), this.activity.getResources().getString(R.string.msg_no_connection), Toast.LENGTH_LONG).show();
        } else if (!command.equalsIgnoreCase(UNDEFINED) && model.getArduinoConnection().isConnected()) {

        }
    }

    private void showAlertMessage(View view) {
        int resId = -1;
        if (view.equals(this.activity.getAlertBtnA())) {
            resId = R.string.buttona;
        } else if (view.equals(this.activity.getAlertBtnB())) {
            resId = R.string.buttonb;
        } else if (view.equals(this.activity.getAlertBtnC())) {
            resId = R.string.buttonc;
        } else if (view.equals(this.activity.getAlertBtnD())) {
            resId = R.string.buttond;
        } else if (view.equals(this.activity.getAlertBtnUp())) {
            resId = R.string.buttonUp;
        } else if (view.equals(this.activity.getAlertBtnDown())) {
            resId = R.string.buttonDown;
        } else if (view.equals(this.activity.getAlertBtnLeft())) {
            resId = R.string.buttonLeft;
        } else if (view.equals(this.activity.getAlertBtnRight())) {
            resId = R.string.buttonRight;
        } else if (view.equals(this.activity.getAlertBtnStart())) {
            resId = R.string.txt_button_start;
        } else if (view.equals(this.activity.getAlertBtnSelect())) {
            resId = R.string.txt_button_select;
        }

        if (resId != -1) {
            String alertMessage = activity.getResources().getString(R.string.no_function_for_command, activity.getResources().getString(resId));
            Toast.makeText(this.activity.getApplicationContext(), alertMessage, Toast.LENGTH_LONG).show();
        }
    }
}

