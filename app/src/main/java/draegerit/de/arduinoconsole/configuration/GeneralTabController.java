package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.widget.Toast;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.PreferencesUtil;
import draegerit.de.arduinoconsole.util.configuration.GeneralConfiguration;

public class GeneralTabController extends AbstractTabController<GeneralTab>{

    public GeneralTabController(GeneralTab tab) {
        super(tab);
    }

    @Override
    public void registerListeners() {

    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        GeneralConfiguration generalConfiguration = PreferencesUtil.getGeneralConfiguration(ctx);
        this.tab.getDeviceActiveWhileConnectedChkBox().setChecked(generalConfiguration.isStayAwakeWhileConnected());
    }

    public void save(Context ctx) {
        GeneralConfiguration generalConfiguration = PreferencesUtil.getGeneralConfiguration(ctx);
        boolean deviceActiveWhileConnected = this.tab.getDeviceActiveWhileConnectedChkBox().isChecked();
        generalConfiguration.setStayAwakeWhileConnected(deviceActiveWhileConnected);
        PreferencesUtil.storeGeneralConfiguration(ctx, generalConfiguration);
        Toast.makeText(ctx, ctx.getString(R.string.save_connection), Toast.LENGTH_LONG).show();
    }
}
