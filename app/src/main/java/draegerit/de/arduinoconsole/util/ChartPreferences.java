package draegerit.de.arduinoconsole.util;


public class ChartPreferences {

    private String title;

    private String subTitle;

    private String valueAxis;

    private String dateAxis;

    public ChartPreferences() {

    }

    public ChartPreferences(String title,String subTitle, String valueAxis, String dateAxis) {
        this.title = title;
        this.valueAxis = valueAxis;
        this.dateAxis = dateAxis;
        this.subTitle = subTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValueAxis() {
        return valueAxis;
    }

    public void setValueAxis(String valueAxis) {
        this.valueAxis = valueAxis;
    }

    public String getDateAxis() {
        return dateAxis;
    }

    public void setDateAxis(String dateAxis) {
        this.dateAxis = dateAxis;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
