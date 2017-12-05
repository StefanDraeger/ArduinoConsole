package draegerit.de.arduinoconsole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ControllerActivity extends AppCompatActivity {

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

    private ControllerController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        setTitle(getResources().getString(R.string.controller));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(getResources().getDrawable(R.drawable.logodraeger));

        this.controller = new ControllerController(this);

        buttonUp = initImageButtonAction(R.id.buttonUp);
        buttonDown = initImageButtonAction(R.id.buttonDown);
        buttonLeft = initImageButtonAction(R.id.buttonLeft);
        buttonRight = initImageButtonAction(R.id.buttonRight);

        buttonA = initButtonAction(R.id.buttonA);
        buttonB = initButtonAction(R.id.buttonB);
        buttonC = initButtonAction(R.id.buttonC);
        buttonD = initButtonAction(R.id.buttonD);

        buttonStart = initButtonAction(R.id.buttonStart);
        buttonSelect = initButtonAction(R.id.buttonSelect);

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

        this.controller.setDefaultValues();
    }

    private ImageView initAlertButtonAction(int id) {
        ImageView imgView = (ImageView) findViewById(id);
        imgView.setOnClickListener(this.controller);
        return imgView;
    }

    private TextView initButtonAction(int id) {
        TextView view = (TextView) findViewById(id);
        view.setOnClickListener(this.controller);
        return view;
    }

    private ImageView initImageButtonAction(int id) {
        ImageView view = (ImageView) findViewById(id);
        view.setOnClickListener(this.controller);
        return view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.controller.setDefaultValues();
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

    public ImageView getAlertBtnUp() {
        return alertBtnUp;
    }

    public ImageView getAlertBtnDown() {
        return alertBtnDown;
    }

    public ImageView getAlertBtnLeft() {
        return alertBtnLeft;
    }

    public ImageView getAlertBtnRight() {
        return alertBtnRight;
    }


    public ImageView getAlertBtnA() {
        return alertBtnA;
    }


    public ImageView getAlertBtnB() {
        return alertBtnB;
    }

    public ImageView getAlertBtnC() {
        return alertBtnC;
    }

    public ImageView getAlertBtnD() {
        return alertBtnD;
    }

    public ImageView getAlertBtnStart() {
        return alertBtnStart;
    }

    public ImageView getAlertBtnSelect() {
        return alertBtnSelect;
    }

    public ImageView getButtonUp() {
        return buttonUp;
    }

    public ImageView getButtonDown() {
        return buttonDown;
    }

    public ImageView getButtonLeft() {
        return buttonLeft;
    }

    public ImageView getButtonRight() {
        return buttonRight;
    }

    public TextView getButtonA() {
        return buttonA;
    }

    public TextView getButtonB() {
        return buttonB;
    }


    public TextView getButtonC() {
        return buttonC;
    }


    public TextView getButtonD() {
        return buttonD;
    }

    public TextView getButtonStart() {
        return buttonStart;
    }

    public TextView getButtonSelect() {
        return buttonSelect;
    }

}
