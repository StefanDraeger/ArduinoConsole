package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import draegerit.de.arduinoconsole.R;

public final class PreferencesUtil {

    private static final String PREFS_NAME = "arduinoConsole";

    private static final String CHART_PREF = "chartPref";
    private static final String USB_CONNECTION_PREF = "usbConnectionPref";

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

    public static void storeUSBConnection(Context ctx, USBConfiguration usbConfiguration) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        Gson gson = new Gson();
        String json = gson.toJson(usbConfiguration);
        editor.putString(USB_CONNECTION_PREF, json);
        editor.commit();
    }

    public static USBConfiguration getUSBConnection(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String usbConnectionJSON = settings.getString(USB_CONNECTION_PREF, getDefaultUSBConnection());
        if (!isBlank(usbConnectionJSON)) {
            Gson gson = new Gson();
            USBConfiguration usbConfiguration = gson.fromJson(usbConnectionJSON, USBConfiguration.class);
            return usbConfiguration;
        }
        return null;
    }

    private static String getDefaultUSBConnection() {
        USBConfiguration usbConfiguration = new USBConfiguration();
        usbConfiguration.setBaudrate(9600);
        usbConfiguration.setDataBits(8);
        usbConfiguration.setStopbits(1);
        usbConfiguration.setParity(EParity.NONE.getValue());
        return new Gson().toJson(usbConfiguration);
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
