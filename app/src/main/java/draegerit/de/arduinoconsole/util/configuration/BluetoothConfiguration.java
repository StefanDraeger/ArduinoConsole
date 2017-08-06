package draegerit.de.arduinoconsole.util.configuration;



public class BluetoothConfiguration {

    private boolean showCloseConnectionDialog;

    private boolean showSearchNewDevicesDialog;

    private boolean sendGreetingsMessage;

    private String greetingsMessage;

    public BluetoothConfiguration(boolean showCloseConnectionDialog, boolean showSearchNewDevicesDialog,boolean sendGreetingsMessage,String greetingsMessage) {
        this.showCloseConnectionDialog = showCloseConnectionDialog;
        this.showSearchNewDevicesDialog = showSearchNewDevicesDialog;
        this.sendGreetingsMessage = sendGreetingsMessage;
        this.greetingsMessage = greetingsMessage;
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

    public boolean isSendGreetingsMessage() {
        return sendGreetingsMessage;
    }

    public void setSendGreetingsMessage(boolean sendGreetingsMessage) {
        this.sendGreetingsMessage = sendGreetingsMessage;
    }

    public String getGreetingsMessage() {
        return greetingsMessage;
    }

    public void setGreetingsMessage(String greetingsMessage) {
        this.greetingsMessage = greetingsMessage;
    }
}
