package draegerit.de.arduinoconsole.export;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

import static draegerit.de.arduinoconsole.R.string.filenotfoundmessage;

public abstract class AbstractExport {

    protected final static String MIMETYPE_TEXT_PLAIN = "text/plain";
    protected final static String MIMETYPE_IMAGE_PNG = "image/png";
    protected final static String MIMETYPE_EXCEL = "application/vnd.ms-excel";
    protected final static String MIMETYPE_PDF = "application/pdf";

    protected static final String TAG = "ArduinoConsole";

    private File chartImage;

    private String exportFilename;

    public abstract void doExport(Context ctx);

    public abstract String getMimeType();

    public void startExportIntent(File file, Context context) {
        if (!file.exists()) {
            String msg = String.format(context.getResources().getString(filenotfoundmessage), file.getAbsolutePath());
            Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType(getMimeType());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            context.startActivity(intent);
        }
    }

    public void setExportFilename(String exportFilename) {
        this.exportFilename = exportFilename;
    }

    public String getExportFilename() {
        return exportFilename;
    }

    public File getChartImage() {
        return chartImage;
    }

    public void setChartImage(File chartImage) {
        this.chartImage = chartImage;
    }
}
