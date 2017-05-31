package draegerit.de.arduinoconsole;

import android.graphics.Point;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.data.time.Millisecond;
import org.afree.data.time.Minute;
import org.afree.data.time.Second;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import draegerit.de.arduinoconsole.chart.ChartView;
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

        this.model.addObserver(this);
    }

    @Override
    public void onBackPressed() {
        model.deleteObserver(this);
        super.onBackPressed();
    }

    private void generateChart() {
        this.chartView = new ChartView(getApplicationContext());
        ConstraintLayout chartLayout = (ConstraintLayout) findViewById(R.id.chartLayout);
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
