package draegerit.de.arduinoconsole.configuration;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import draegerit.de.arduinoconsole.R;


public class GeneralTab extends Fragment {

    private CheckBox deviceActiveWhileConnectedChkBox;

    private View view;

    private GeneralTabController generalTabController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.configuration_tab_general, container, false);
        registerComponents();
        this.generalTabController = new GeneralTabController(this);
        this.generalTabController.setDefaultValues(getContext());
        return this.view;
    }

    public void save() {
        this.generalTabController.save(getContext());
    }

    /**
     * Erzeugt die Komponenten der View.
     */
    private void registerComponents() {
        this.deviceActiveWhileConnectedChkBox = (CheckBox) this.view.findViewById(R.id.deviceActiveWhileConnectedChkBox);
    }

    public CheckBox getDeviceActiveWhileConnectedChkBox() {
        return deviceActiveWhileConnectedChkBox;
    }

    public void setDeviceActiveWhileConnectedChkBox(CheckBox deviceActiveWhileConnectedChkBox) {
        this.deviceActiveWhileConnectedChkBox = deviceActiveWhileConnectedChkBox;
    }
}
