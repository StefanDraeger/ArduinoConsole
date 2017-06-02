package draegerit.de.arduinoconsole;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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

public class GraphActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "ArduinoConsole";
    private static final int QUALITY = 100;


    private ImageButton graphClearBtn;
    private ImageButton prefBtn;

    private Model model = Model.getInstance();

    private GraphController controller;

    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        setTitle(getResources().getString(R.string.graph));

        createComponents();

        this.controller = new GraphController(this);
        this.controller.registerComponents();

        generateChart();

        this.model.addObserver(this);
    }

    private void createComponents() {
        this.graphClearBtn = (ImageButton) findViewById(R.id.graphClearBtn);
        this.prefBtn = (ImageButton) findViewById(R.id.prefBtn);
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

    void showExportDialog() {
        final Dialog dialog = generateDialog(R.layout.exportdialog, R.string.exportTitle);

        final RadioGroup chartExportRadioGroup = (RadioGroup) dialog.findViewById(R.id.chartExportRadioGroup);

        Button dialogOkButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData(chartExportRadioGroup);
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private Dialog generateDialog(int layout, int titleResId) {
        final Dialog dialog = new Dialog(GraphActivity.this);
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

        launchProgressbarWaitDialog(export);
    }


    public void launchProgressbarWaitDialog(AbstractExport export) {
        String titel = getResources().getString(R.string.graphProgressTitle);
        String message = getResources().getString(R.string.graphProgressMessage);
        final ProgressDialog spinnerProgressDialog = ProgressDialog.show(GraphActivity.this, titel, message, true);
        spinnerProgressDialog.setCancelable(false);
        spinnerProgressDialog.show();
        GenerateAndShareAsyncTask task = new GenerateAndShareAsyncTask(spinnerProgressDialog, export);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    private class GenerateAndShareAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private AbstractExport export;

        public GenerateAndShareAsyncTask(ProgressDialog progressDialog, AbstractExport export) {
            this.progressDialog = progressDialog;
            this.export = export;
        }

        @Override
        protected Void doInBackground(Void... params) {
            export.doExport(getApplicationContext());
            progressDialog.dismiss();
            return null;
        }
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
        cal.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        cal.setTimeInMillis(timestamp);
        return new Millisecond(cal.getTime());
    }

    public ChartView getChartView() {
        return chartView;
    }

    public ImageButton getGraphClearBtn() {
        return graphClearBtn;
    }

    public ImageButton getPrefBtn() {
        return prefBtn;
    }
}
