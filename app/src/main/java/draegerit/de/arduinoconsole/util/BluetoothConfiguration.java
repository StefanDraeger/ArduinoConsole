package draegerit.de.arduinoconsole.util;



public class BluetoothConfiguration {

    private boolean showCloseConnectionDialog;

    public BluetoothConfiguration(boolean showCloseConnectionDialog) {
        this.showCloseConnectionDialog = showCloseConnectionDialog;
    }

    public boolean isShowCloseConnectionDialog() {
        return showCloseConnectionDialog;
    }

    public void setShowCloseConnectionDialog(boolean showCloseConnectionDialog) {
        this.showCloseConnectionDialog = showCloseConnectionDialog;
    }
}
