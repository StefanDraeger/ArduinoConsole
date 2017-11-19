package draegerit.de.arduinoconsole.configuration.httpconnection;

public class HttpConnectionProfile {

    private String profileName;
    private String shema;
    private String serverAddress;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getShema() {
        return shema;
    }

    public void setShema(String shema) {
        this.shema = shema;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
