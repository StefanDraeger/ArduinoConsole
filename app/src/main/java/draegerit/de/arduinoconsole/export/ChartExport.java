package draegerit.de.arduinoconsole.export;

import android.content.Context;

public class ChartExport extends AbstractExport {

    @Override
    public void doExport(Context ctx) {
        setExportFilename("chart.png");
        startExportIntent(getChartImage(), ctx);
    }

    @Override
    public String getMimeType() {
        return MIMETYPE_IMAGE_PNG;
    }
}
