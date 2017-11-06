package draegerit.de.arduinoconsole.controller.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.configuration.GeneralTabController;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

/**
 * Created by Stefan Draeger on 06.11.2017.
 */

public class CommandsTab extends Fragment {

    private View view;

    private EditText commandButtonUpEditText;
    private EditText commandButtonLeftEditText;
    private EditText commandButtonDownEditText;
    private EditText commandButtonRightEditText;
    private EditText commandButtonAEditText;
    private EditText commandButtonBEditText;
    private EditText commandButtonCEditText;
    private EditText commandButtonDEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.controller_tab_commands, container, false);

        commandButtonUpEditText = (EditText) this.view.findViewById(R.id.commandButtonUpEditText);
        commandButtonLeftEditText = (EditText) this.view.findViewById(R.id.commandButtonLeftEditText);
        commandButtonDownEditText = (EditText) this.view.findViewById(R.id.commandButtonDownEditText);
        commandButtonRightEditText = (EditText) this.view.findViewById(R.id.commandButtonRightEditText);
        commandButtonAEditText = (EditText) this.view.findViewById(R.id.commandButtonAEditText);
        commandButtonBEditText = (EditText) this.view.findViewById(R.id.commandButtonBEditText);
        commandButtonCEditText = (EditText) this.view.findViewById(R.id.commandButtonCEditText);
        commandButtonDEditText = (EditText) this.view.findViewById(R.id.commandButtonDEditText);

        return this.view;
    }

    public void save() {
        Context ctx = getActivity().getApplicationContext();
        ControllerSetting setting = PreferencesUtil.getControllerSettings(ctx);
        setting.setCommandButtonRight(this.commandButtonRightEditText.getText().toString());
        setting.setCommandButtonLeft(this.commandButtonLeftEditText.getText().toString());
        setting.setCommandButtonUp(this.commandButtonUpEditText.getText().toString());
        setting.setCommandButtonDown(this.commandButtonDownEditText.getText().toString());
        setting.setCommandButtonA(this.commandButtonAEditText.getText().toString());
        setting.setCommandButtonB(this.commandButtonBEditText.getText().toString());
        setting.setCommandButtonC(this.commandButtonCEditText.getText().toString());
        setting.setCommandButtonD(this.commandButtonDEditText.getText().toString());
        PreferencesUtil.storeControllerSettingsPreferences(ctx, setting);
    }
}
