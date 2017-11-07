package draegerit.de.arduinoconsole.controller.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import draegerit.de.arduinoconsole.R;

/**
 * Created by Stefan Draeger on 06.11.2017.
 */

public class CommandsTab extends Fragment {

    private View view;
    private CommandsController controller;

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
        registerComponents();
        this.controller = new CommandsController(this);
        return this.view;
    }

    private void registerComponents() {
        commandButtonUpEditText = (EditText) this.view.findViewById(R.id.commandButtonUpEditText);
        commandButtonLeftEditText = (EditText) this.view.findViewById(R.id.commandButtonLeftEditText);
        commandButtonDownEditText = (EditText) this.view.findViewById(R.id.commandButtonDownEditText);
        commandButtonRightEditText = (EditText) this.view.findViewById(R.id.commandButtonRightEditText);
        commandButtonAEditText = (EditText) this.view.findViewById(R.id.commandButtonAEditText);
        commandButtonBEditText = (EditText) this.view.findViewById(R.id.commandButtonBEditText);
        commandButtonCEditText = (EditText) this.view.findViewById(R.id.commandButtonCEditText);
        commandButtonDEditText = (EditText) this.view.findViewById(R.id.commandButtonDEditText);
    }

    public EditText getCommandButtonUpEditText() {
        return commandButtonUpEditText;
    }

    public EditText getCommandButtonLeftEditText() {
        return commandButtonLeftEditText;
    }

    public EditText getCommandButtonDownEditText() {
        return commandButtonDownEditText;
    }

    public EditText getCommandButtonRightEditText() {
        return commandButtonRightEditText;
    }

    public EditText getCommandButtonAEditText() {
        return commandButtonAEditText;
    }

    public EditText getCommandButtonBEditText() {
        return commandButtonBEditText;
    }

    public EditText getCommandButtonCEditText() {
        return commandButtonCEditText;
    }

    public EditText getCommandButtonDEditText() {
        return commandButtonDEditText;
    }

    public CommandsController getController() {
        return controller;
    }
}
