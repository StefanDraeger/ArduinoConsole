package draegerit.de.arduinoconsole;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.afree.data.time.Millisecond;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import draegerit.de.arduinoconsole.chart.ChartView;
import draegerit.de.arduinoconsole.export.AbstractExport;
import draegerit.de.arduinoconsole.export.CSVExport;
import draegerit.de.arduinoconsole.export.ChartExport;
import draegerit.de.arduinoconsole.export.PDFExport;
import draegerit.de.arduinoconsole.util.Message;

public class GraphActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "ArduinoConsole";

    private Model model = Model.getInstance();

    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle(getResources().getString(R.string.graph));

        generateChart();

        ImageButton graphClearBtn = (ImageButton) findViewById(R.id.graphClearBtn);
        graphClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartView.getDataset().getSeries(0).clear();
            }
        });

        this.model.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.graphmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.exportItem:
                showExportDialog();
                break;
            default:
                throw new IllegalArgumentException("Item with ID [" + item.getItemId() + "] not found!");
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showExportDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(GraphActivity.this);
        dialog.setContentView(R.layout.exportdialog);
        dialog.setTitle(getResources().getString(R.string.exportTitle));

        final RadioGroup chartExportRadioGroup = (RadioGroup) dialog.findViewById(R.id.chartExportRadioGroup);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData(chartExportRadioGroup);
                dialog.dismiss();
            }
        });

        Button dialogAbortButton = (Button) dialog.findViewById(R.id.dialogButtonAbort);
        dialogAbortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void exportData(RadioGroup chartExportRadioGroup) {
        AbstractExport export = null;
        switch (chartExportRadioGroup.getCheckedRadioButtonId()) {
            case R.id.csvExportItem:
                export = new CSVExport();
                break;
            case R.id.pdfExportItem:
                export = new PDFExport();
                export.setChartImage(generateChartImage());
                break;
            case R.id.chartExportItem:
                export = new ChartExport();
                export.setChartImage(generateChartImage());
                break;
        }

        export.doExport(getApplicationContext());
    }

    private File generateChartImage() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String filename = path.getAbsolutePath() + File.pathSeparator + String.valueOf(System.currentTimeMillis()) + ".png";

        File file = new File(filename);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            LinearLayout view = (LinearLayout) findViewById(R.id.chartContainerLayout);
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        return file;
    }

    @Override
    public void onBackPressed() {
        model.deleteObserver(this);
        super.onBackPressed();
    }

    private void generateChart() {
        this.chartView = new ChartView(getApplicationContext());
        LinearLayout chartLayout = (LinearLayout) findViewById(R.id.chartContainerLayout);
        chartLayout.addView(this.chartView);
    }

    @Override
    public void update(Observable o, final Object arg) {
        if (arg instanceof Message) {
            final Message msg = (Message) arg;
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            chartView.addValue(formatTimestamp(msg.getTimestamp()), msg.getValue());
                        }
                    }
            );
        }
    }

    private Millisecond formatTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        cal.setTimeInMillis(timestamp);
        return new Millisecond(cal.getTime());
    }

}
