package draegerit.de.arduinoconsole.util;



public class BluetoothConfiguration {

    private boolean showCloseConnectionDialog;

    private boolean showSearchNewDevicesDialog;

    public BluetoothConfiguration(boolean showCloseConnectionDialog, boolean showSearchNewDevicesDialog) {
        this.showCloseConnectionDialog = showCloseConnectionDialog;
        this.showSearchNewDevicesDialog = showSearchNewDevicesDialog;
    }

    public boolean isShowCloseConnectionDialog() {
        return showCloseConnectionDialog;
    }

    public void setShowCloseConnectionDialog(boolean showCloseConnectionDialog) {
        this.showCloseConnectionDialog = showCloseConnectionDialog;
    }

    public boolean isShowSearchNewDevicesDialog() {
        return showSearchNewDevicesDialog;
    }

    public void setShowSearchNewDevicesDialog(boolean showSearchNewDevicesDialog) {
        this.showSearchNewDevicesDialog = showSearchNewDevicesDialog;
    }
}
