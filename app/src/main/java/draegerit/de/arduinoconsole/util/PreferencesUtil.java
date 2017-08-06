package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.configuration.GeneralConfiguration;
import draegerit.de.arduinoconsole.util.configuration.TerminalConfiguration;
import draegerit.de.arduinoconsole.util.configuration.USBConfiguration;

public final class PreferencesUtil {

    private static final String PREFS_NAME = "arduinoConsole";

    private static final String CHART_PREF = "chartPref";
    private static final String USB_CONFIGURATION_PREF = "usbConfigurationPref";
    private static final String BLUETOOTH_CONFIGURATION_PREF = "bluetoothConfigurationPref";
    private static final String GENERAL_CONFIGURATION_PREF = "generalConfigurationPref";
    private static final String TERMINAL_CONFIGURATION_PREF = "terminalConfigurationPref";

    private static final int ZERO = 0;
    private static final String EMPTY = "";

    private PreferencesUtil() {

    }

    public static void storeChartPreferences(Context ctx, ChartPreferences chartPreferences) {
        Gson gson = new Gson();
        String json = gson.toJson(chartPreferences);
        store(ctx, json, CHART_PREF);
    }

    public static ChartPreferences getChartPreferences(Context ctx) {
        String chartPrefJson = get(ctx, CHART_PREF, getDefaultchartPreferences(ctx));
        if (!isBlank(chartPrefJson)) {
            return new Gson().fromJson(chartPrefJson, ChartPreferences.class);
        }
        return null;
    }

    public static void storeUSBConfiguration(Context ctx, USBConfiguration usbConfiguration) {
        String json = new Gson().toJson(usbConfiguration);
        store(ctx, json, USB_CONFIGURATION_PREF);
    }

    public static USBConfiguration getUSBConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String usbConnectionJSON = settings.getString(USB_CONFIGURATION_PREF, getDefaultUSBConnection());
        if (!isBlank(usbConnectionJSON)) {
            return new Gson().fromJson(usbConnectionJSON, USBConfiguration.class);
        }
        return null;
    }

    public static void storeBluetoothConfiguration(Context ctx, BluetoothConfiguration bluetoothConfiguration) {
        String json = new Gson().toJson(bluetoothConfiguration);
        store(ctx, json, BLUETOOTH_CONFIGURATION_PREF);
    }

    public static void storeGeneralConfiguration(Context ctx, GeneralConfiguration generalConfiguration) {
        String json = new Gson().toJson(generalConfiguration);
        store(ctx, json, GENERAL_CONFIGURATION_PREF);
    }

    public static void storeTerminalConfiguration(Context ctx, TerminalConfiguration terminalConfiguration) {
        String json = new Gson().toJson(terminalConfiguration);
        store(ctx, json, TERMINAL_CONFIGURATION_PREF);
    }

    public static BluetoothConfiguration getBluetoothConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String usbConnectionJSON = settings.getString(BLUETOOTH_CONFIGURATION_PREF, getDefaultBluetoothConfiguration());
        if (!isBlank(usbConnectionJSON)) {
            return  new Gson().fromJson(usbConnectionJSON, BluetoothConfiguration.class);
        }
        return null;
    }


    public static GeneralConfiguration getGeneralConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String generalConfigurationJSON = settings.getString(GENERAL_CONFIGURATION_PREF, getDefaultGeneralConfiguration());
        if (!isBlank(generalConfigurationJSON)) {
            return new Gson().fromJson(generalConfigurationJSON, GeneralConfiguration.class);
        }
        return null;
    }

    public static TerminalConfiguration getTerminalConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String terminalConfigurationJSON = settings.getString(TERMINAL_CONFIGURATION_PREF, getDefaultTerminalConfiguration());
        if (!isBlank(terminalConfigurationJSON)) {
            return new Gson().fromJson(terminalConfigurationJSON, TerminalConfiguration.class);
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

    private static String getDefaultchartPreferences(Context ctx) {
        ChartPreferences chartPreferences = new ChartPreferences();
        chartPreferences.setTitle(ctx.getResources().getString(R.string.chartText));
        chartPreferences.setValueAxis(ctx.getResources().getString(R.string.werteText));
        chartPreferences.setDateAxis(ctx.getResources().getString(R.string.datumText));
        chartPreferences.setSubTitle(EMPTY);
        return new Gson().toJson(chartPreferences);
    }

    private static String getDefaultBluetoothConfiguration() {
        BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration(true, true, true, "Hello Arduino!");
        return new Gson().toJson(bluetoothConfiguration);
    }

    private static String getDefaultGeneralConfiguration() {
        GeneralConfiguration generalConfiguration = new GeneralConfiguration(true);
        return new Gson().toJson(generalConfiguration);
    }

    private static String getDefaultTerminalConfiguration() {
        TerminalConfiguration terminalConfiguration = new TerminalConfiguration();
        terminalConfiguration.setShowTimestampsBeforeMessageText(false);
        terminalConfiguration.setMessageDateFormat("yyyy/MM/dd");
        return new Gson().toJson(terminalConfiguration);
    }

    private static boolean store(Context ctx, String json, String key) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, json);
        return editor.commit();
    }

    private static String get(Context ctx, String key, String defaultJson) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        return settings.getString(key, defaultJson);
    }

}
