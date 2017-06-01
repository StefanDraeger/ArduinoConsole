package draegerit.de.arduinoconsole.export;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import draegerit.de.arduinoconsole.Model;
import draegerit.de.arduinoconsole.util.Message;

public class CSVExport extends AbstractExport {

    private static final String CSV_SUFFIX = ".csv";

    @Override
    public void doExport(Context ctx) {
        Model model = Model.getInstance();
        List<Message> messageList = new ArrayList<>(model.getMessages());
        StringBuffer csvBuffer = new StringBuffer();
        csvBuffer.append("Index");
        csvBuffer.append(";");
        csvBuffer.append("Timestamp");
        csvBuffer.append(";");
        csvBuffer.append("Value");
        csvBuffer.append("\r");
        int counter = 1;
        for (Message msg : messageList) {
            csvBuffer.append(counter++);
            csvBuffer.append(";");
            csvBuffer.append(msg.getTimestamp());
            csvBuffer.append(";");
            csvBuffer.append(msg.getValue().replaceAll("\r\n", " - "));
            csvBuffer.append("\r");
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        setExportFilename(path.getAbsolutePath() + File.pathSeparator + String.valueOf(System.currentTimeMillis()) + CSV_SUFFIX);

        FileOutputStream fos = null;
        File output = new File(getExportFilename());
        byte[] byteData = csvBuffer.toString().getBytes();
        try {
            fos = new FileOutputStream(output);
            fos.write(byteData);
            fos.flush();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        startExportIntent(output, ctx);
    }

    @Override
    public String getMimeType() {
        return MIMETYPE_TEXT_PLAIN;
    }

}
