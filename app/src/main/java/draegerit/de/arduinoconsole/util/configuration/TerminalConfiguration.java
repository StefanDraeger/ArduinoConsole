package draegerit.de.arduinoconsole.util.configuration;


public class TerminalConfiguration{

    private boolean showTimestampsBeforeMessageText;

    private String messageDateFormat;

    public boolean isShowTimestampsBeforeMessageText() {
        return showTimestampsBeforeMessageText;
    }

    public void setShowTimestampsBeforeMessageText(boolean showTimestampsBeforeMessageText) {
        this.showTimestampsBeforeMessageText = showTimestampsBeforeMessageText;
    }

    public String getMessageDateFormat() {
        return messageDateFormat;
    }

    public void setMessageDateFormat(String messageDateFormat) {
        this.messageDateFormat = messageDateFormat;
    }
}
