package draegerit.de.arduinoconsole.controller.settings;

import android.support.v4.app.Fragment;

/**
 * Created by Stefan Draeger on 07.11.2017.
 */

public abstract class AbstractTabController<T extends Fragment> {

    T tab;

    public AbstractTabController(T tab){
        this.tab = tab;
        init();
    }

    private void init() {
        loadValues();
    }

    public abstract void loadValues();

    public abstract void save();
}
