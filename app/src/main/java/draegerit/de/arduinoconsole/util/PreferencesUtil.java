package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import draegerit.de.arduinoconsole.R;

public final class PreferencesUtil {

    private static final String PREFS_NAME = "arduinoConsole";

    private static final String CHART_PREF = "chartPref";

    public static final int ZERO = 0;

    private PreferencesUtil() {

    }

    public static void storeChartPreferences(Context ctx, ChartPreferences chartPreferences) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(chartPreferences);
        editor.putString(CHART_PREF, json);
        editor.commit();
    }

    public static ChartPreferences getChartPreferences(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String chartPrefJson = settings.getString(CHART_PREF, getDefaultchartPreferences(ctx));
        if (!isBlank(chartPrefJson)) {
            Gson gson = new Gson();
            ChartPreferences chartPref = gson.fromJson(chartPrefJson, ChartPreferences.class);
            return chartPref;
        }
        return null;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == ZERO;
    }

    public static String getDefaultchartPreferences(Context ctx) {
        ChartPreferences chartPreferences = new ChartPreferences();
        chartPreferences.setTitle(ctx.getResources().getString(R.string.chartText));
        chartPreferences.setValueAxis(ctx.getResources().getString(R.string.werteText));
        chartPreferences.setDateAxis(ctx.getResources().getString(R.string.datumText));
        chartPreferences.setSubTitle("");
        return new Gson().toJson(chartPreferences);
    }
}
