package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import draegerit.de.arduinoconsole.R;


public class TerminalTab extends Fragment {

    private View view;

    private TerminalTabController controller;

    private CheckBox displayTimestampCheckBox;

    private Spinner timestampSpinner;

    private TextView timestampExampleTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.configuration_tab_terminal, container, false);
        registerComponents();
        this.controller = new TerminalTabController(this);
        this.controller.setDefaultValues(getContext());
        return this.view;
    }

    public void save() {
        this.controller.save(getContext());
    }

    /**
     * Erzeugt die Komponenten der View.
     */
    private void registerComponents() {
        this.displayTimestampCheckBox = (CheckBox) this.view.findViewById(R.id.displayTimestampCheckBox);
        this.timestampSpinner = (Spinner) this.view.findViewById(R.id.timestampSpinner);
        this.timestampExampleTextView = (TextView) this.view.findViewById(R.id.timestampExampleTextView);
    }

    public CheckBox getDisplayTimestampCheckBox() {
        return displayTimestampCheckBox;
    }

    public void setDisplayTimestampCheckBox(CheckBox displayTimestampCheckBox) {
        this.displayTimestampCheckBox = displayTimestampCheckBox;
    }

    public Spinner getTimestampSpinner() {
        return timestampSpinner;
    }

    public void setTimestampSpinner(Spinner timestampSpinner) {
        this.timestampSpinner = timestampSpinner;
    }

    public TextView getTimestampExampleTextView() {
        return timestampExampleTextView;
    }

    public void setTimestampExampleTextView(TextView timestampExampleTextView) {
        this.timestampExampleTextView = timestampExampleTextView;
    }
}
