package draegerit.de.arduinoconsole.controller.settings;

import android.content.Context;

import draegerit.de.arduinoconsole.util.PreferencesUtil;

public class CommandsController extends AbstractTabController<CommandsTab> {


    public CommandsController(CommandsTab tab) {
        super(tab);
    }

    @Override
    public void loadValues() {
        ControllerSetting setting = PreferencesUtil.getControllerSettings(tab.getActivity().getApplicationContext());
        this.tab.getCommandButtonAEditText().setText(setting.getCommandButtonA());
        this.tab.getCommandButtonBEditText().setText(setting.getCommandButtonB());
        this.tab.getCommandButtonCEditText().setText(setting.getCommandButtonC());
        this.tab.getCommandButtonDEditText().setText(setting.getCommandButtonD());

        this.tab.getCommandButtonUpEditText().setText(setting.getCommandButtonUp());
        this.tab.getCommandButtonDownEditText().setText(setting.getCommandButtonDown());
        this.tab.getCommandButtonLeftEditText().setText(setting.getCommandButtonLeft());
        this.tab.getCommandButtonRightEditText().setText(setting.getCommandButtonRight());
    }

    @Override
    public void save() {
        Context ctx = tab.getActivity().getApplicationContext();
        ControllerSetting setting = PreferencesUtil.getControllerSettings(ctx);
        setting.setCommandButtonRight(this.tab.getCommandButtonRightEditText().getText().toString());
        setting.setCommandButtonLeft(this.tab.getCommandButtonLeftEditText().getText().toString());
        setting.setCommandButtonUp(this.tab.getCommandButtonUpEditText().getText().toString());
        setting.setCommandButtonDown(this.tab.getCommandButtonDownEditText().getText().toString());

        setting.setCommandButtonA(this.tab.getCommandButtonAEditText().getText().toString());
        setting.setCommandButtonB(this.tab.getCommandButtonBEditText().getText().toString());
        setting.setCommandButtonC(this.tab.getCommandButtonCEditText().getText().toString());
        setting.setCommandButtonD(this.tab.getCommandButtonDEditText().getText().toString());
        PreferencesUtil.storeControllerSettingsPreferences(ctx, setting);
    }

}
