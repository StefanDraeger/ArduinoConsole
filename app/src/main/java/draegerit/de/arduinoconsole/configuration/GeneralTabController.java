package draegerit.de.arduinoconsole.configuration;


import android.content.Context;
import android.widget.Toast;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.GeneralConfiguration;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

public class GeneralTabController {

    private GeneralTab generalTab;

    public GeneralTabController(GeneralTab generalTab) {
        this.generalTab = generalTab;
    }

    /**
     * Setzt die Default Werte.
     */
    public void setDefaultValues(Context ctx) {
        GeneralConfiguration generalConfiguration = PreferencesUtil.getGeneralConfiguration(ctx);
        this.generalTab.getDeviceActiveWhileConnectedChkBox().setChecked(generalConfiguration.isStayAwakeWhileConnected());
    }

    public void save(Context ctx) {
        GeneralConfiguration generalConfiguration = PreferencesUtil.getGeneralConfiguration(ctx);
        boolean deviceActiveWhileConnected = this.generalTab.getDeviceActiveWhileConnectedChkBox().isChecked();
        generalConfiguration.setStayAwakeWhileConnected(deviceActiveWhileConnected);
        PreferencesUtil.storeGeneralConfiguration(ctx, generalConfiguration);
        Toast.makeText(ctx, ctx.getString(R.string.save_connection), Toast.LENGTH_LONG).show();
    }
}
