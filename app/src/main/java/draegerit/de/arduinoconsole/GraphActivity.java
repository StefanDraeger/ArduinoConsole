package draegerit.de.arduinoconsole;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import draegerit.de.arduinoconsole.util.ChartPreferences;
import draegerit.de.arduinoconsole.util.Message;
import draegerit.de.arduinoconsole.util.PreferencesUtil;

import static draegerit.de.arduinoconsole.ArduinoConsoleStatics.TIMEZONE;

/**
 * Klasse zum erzeugen der {@link android.app.Activity} für die Darstellung des Diagramms.
 */
public class GraphActivity extends AppCompatActivity implements Observer {

    /**
     * TAG für die Log Messages.
     */
    private static final String TAG = "ArduinoConsole";

    /**
     * Default Wert für die Qualität der Grafik beim exportieren.
     */
    private static final int QUALITY = 100;

    /**
     * Das Model.
     */
    private Model model = Model.getInstance();

    /**
     * {@link GraphController} mit den Actionen für die {@link android.app.Activity}
     */
    private GraphController controller;

    /**
     * {@link ChartView} für das Diagramm.
     */
    private ChartView chartView;

    /**
     * Der Dialog zum exportieren der Daten.
     */
    private Dialog exportDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle(getResources().getString(R.string.graph));

        this.controller = new GraphController(this);
        this.controller.registerComponents();

        generateChart();

        this.model.addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.graphmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.exportItem:
                showExportDialog();
                break;
            case R.id.graphprefItem:
                showChartPreferences();
                break;
            case R.id.graphclearItem:
                clearChartValues();
                break;
            default:
                throw new IllegalArgumentException("Item with ID [" + item.getItemId() + "] not found!");
        }

        if (intent != null) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearChartValues() {
        getChartView().getDataset().getSeries(0).clear();
    }

    void showExportDialog() {
        exportDialog = generateDialog(R.layout.exportdialog, R.string.exportTitle);

        final RadioGroup chartExportRadioGroup = (RadioGroup) exportDialog.findViewById(R.id.chartExportRadioGroup);

        Button dialogOkButton = (Button) exportDialog.findViewById(R.id.dialogButtonOK);
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData(chartExportRadioGroup);
                exportDialog.dismiss();
            }
        });

        exportDialog.show();
    }

    public void showChartPreferences() {
        final Dialog dialog = generateDialog(R.layout.graphconfigurationdialog, R.string.chartPreferences);

        final EditText titleEditText = (EditText) dialog.findViewById(R.id.titleEditText);
        final EditText subTitleEditText = (EditText) dialog.findViewById(R.id.subTitleEditText);
        final EditText valueAxisEditText = (EditText) dialog.findViewById(R.id.valueAxisEditText);
        final EditText dateAxisEditText = (EditText) dialog.findViewById(R.id.dateAxisEditText);

        ChartPreferences chartPreferences = PreferencesUtil.getChartPreferences(getApplicationContext());
        titleEditText.setText(chartPreferences.getTitle());
        subTitleEditText.setText(chartPreferences.getSubTitle());
        valueAxisEditText.setText(chartPreferences.getValueAxis());
        dateAxisEditText.setText(chartPreferences.getDateAxis());

        Button dialogSaveButton = (Button) dialog.findViewById(R.id.dialogButtonSave);
        dialogSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String subTitle = subTitleEditText.getText().toString();
                String valueAxis = valueAxisEditText.getText().toString();
                String dateAxis = dateAxisEditText.getText().toString();
                ChartPreferences chartPreferences = new ChartPreferences(title, subTitle, valueAxis, dateAxis);
                PreferencesUtil.storeChartPreferences(getApplicationContext(), chartPreferences);
                chartView.updateChartPreferences(chartPreferences);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private Dialog generateDialog(final int layout, final int titleResId) {
        final Dialog dialog = new Dialog(new ContextThemeWrapper(GraphActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        dialog.setContentView(layout);
        dialog.setTitle(getResources().getString(titleResId));

        Button dialogAbortButton = (Button) dialog.findViewById(R.id.dialogButtonAbort);
        dialogAbortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private void exportData(final RadioGroup chartExportRadioGroup) {
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

        launchProgressbarWaitDialog(export);
    }


    public void launchProgressbarWaitDialog(final AbstractExport export) {
        GenerateAndShareAsyncTask task = new GenerateAndShareAsyncTask(export);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class GenerateAndShareAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private AbstractExport export;

        public GenerateAndShareAsyncTask(final AbstractExport inExport) {
            this.export = inExport;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            export.doExport(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            export.startExportIntent(new File(export.getExportFilename()), getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String titel = getResources().getString(R.string.graphProgressTitle);
            String message = getResources().getString(R.string.graphProgressMessage);
            progressDialog = new ProgressDialog(GraphActivity.this);
            progressDialog.setTitle(titel);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }
    }

    private File generateChartImage() {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }

        String filename = docsFolder.getAbsolutePath() + File.pathSeparator + String.valueOf(System.currentTimeMillis()) + ".png";

        File file = new File(filename);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            LinearLayout view = (LinearLayout) findViewById(R.id.chartContainerLayout);
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap bmp = view.getDrawingCache();
            bmp.compress(Bitmap.CompressFormat.PNG, QUALITY, out);
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
    public void update(final Observable o, final Object arg) {
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

    /**
     * Formatiert ein Timestamp.
     * @param timestamp
     * @return String
     */
    private Millisecond formatTimestamp(final long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        cal.setTimeInMillis(timestamp);
        return new Millisecond(cal.getTime());
    }

    /**
     * Liefert das Diagramm.
     * @return ChartView
     */
    public ChartView getChartView() {
        return chartView;
    }


}
