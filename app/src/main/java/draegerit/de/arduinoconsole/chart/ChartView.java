package draegerit.de.arduinoconsole.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.afree.data.time.Millisecond;
import org.afree.data.time.RegularTimePeriod;
import org.afree.data.time.Second;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.XYDataset;
import org.afree.graphics.SolidColor;
import org.afree.ui.RectangleInsets;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.Message;

public class ChartView extends DemoView {

    private static final String TAG = "ArduinoConsole";

    private Context ctx;

    private TimeSeries myTimeSerie;

    private RegularTimePeriod lastMillisecond;

    public ChartView(Context context) {
        super(context);
        this.ctx = context;

        XYDataset dataset = getDataset();
        AFreeChart chart = createChart(dataset);
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaintType(new SolidColor(Color.LTGRAY));
        plot.setDomainGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setRangeGridlinePaintType(new SolidColor(Color.WHITE));
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);

        XYItemRenderer r = plot.getRenderer();

        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
//            renderer.setBaseShapesVisible(true);
//            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss"));

        setChart(chart);
    }

    private XYDataset getDataset() {
        String text = getResources().getString(R.string.seriesTxt);
        this.myTimeSerie = new TimeSeries(text);
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(myTimeSerie);
        return dataset;
    }

    public void addValue(Millisecond millisecond, String value) {
        if (value == null || value.trim().length() == 0) {
            return;
        }
        lastMillisecond = millisecond;
        try {
            Double dblValue = Double.parseDouble(value);
            myTimeSerie.add(lastMillisecond.next(), dblValue);
        } catch (NumberFormatException ex) {
            Log.e(TAG, ex.getMessage());
            try {
                String[] str = value.split("\r\n");
                for (String v : str) {
                    if (v != null && v.trim().length() > 0) {
                        lastMillisecond = lastMillisecond.next();
                        Double dblValue = Double.parseDouble(v);
                        myTimeSerie.add(lastMillisecond.next(), dblValue);
                    }
                }
            } catch (NumberFormatException ex1) {
                Log.e(TAG, ex1.getMessage());
            }
        }
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset the dataset.
     * @return The chart.
     */
    private AFreeChart createChart(XYDataset dataset) {
        AFreeChart chart = ChartFactory.createTimeSeriesChart(
                ctx.getResources().getString(R.string.chartText),
                ctx.getResources().getString(R.string.datumText),
                ctx.getResources().getString(R.string.werteText),
                dataset,
                false,
                false,
                false);
        return chart;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return true;
    }
}
