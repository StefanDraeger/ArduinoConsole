package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import draegerit.de.arduinoconsole.R;
import draegerit.de.arduinoconsole.configuration.httpconnection.HttpConnectionProfile;
import draegerit.de.arduinoconsole.controller.settings.ControllerSetting;
import draegerit.de.arduinoconsole.util.configuration.BluetoothConfiguration;
import draegerit.de.arduinoconsole.util.configuration.GeneralConfiguration;
import draegerit.de.arduinoconsole.util.configuration.TerminalConfiguration;
import draegerit.de.arduinoconsole.util.configuration.USBConfiguration;
import draegerit.de.arduinoconsole.util.configuration.WifiConfiguration;

public final class PreferencesUtil {

    public static final int ZERO = 0;
    private static final String PREFS_NAME = "arduinoConsole";
    private static final String CHART_PREF = "chartPref";
    private static final String USB_CONFIGURATION_PREF = "usbConfigurationPref";
    private static final String BLUETOOTH_CONFIGURATION_PREF = "bluetoothConfigurationPref";
    private static final String WIFI_CONFIGURATION_PREF = "wifiConfigurationPref";
    private static final String GENERAL_CONFIGURATION_PREF = "generalConfigurationPref";
    private static final String TERMINAL_CONFIGURATION_PREF = "terminalConfigurationPref";
    private static final String CONTROLLER_SETTINGS_PREF = "controllerSettingsPref";
    private static final String HTTP_CONNECTION_PROF_PREF = "httpConnectionProfilesPref";

    private PreferencesUtil() {

    }

    public static ControllerSetting getControllerSettings(Context ctx) {
        String controllerPrefJson = get(ctx, CONTROLLER_SETTINGS_PREF, getDefaultControllerSettings(ctx));
        if (!isBlank(controllerPrefJson)) {
            ControllerSetting pref = new Gson().fromJson(controllerPrefJson, ControllerSetting.class);
            return pref;
        }
        return null;
    }

    public static void storeHttpConnectionProfile(Context ctx, HttpConnectionProfile profile) {
        List<HttpConnectionProfile> profiles = getHttpConnectionProfiles(ctx);
        profiles.add(profile);
        storeHttpConnectionProfiles(ctx, profiles);
    }

    public static List<HttpConnectionProfile> getHttpConnectionProfiles(Context ctx) {
        List<HttpConnectionProfile> result = new ArrayList<>();
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String json = settings.getString(HTTP_CONNECTION_PROF_PREF, "");
        if (!json.isEmpty()) {
            Type token = new TypeToken<List<HttpConnectionProfile>>() {
            }.getType();
            Gson gson = new Gson();
            result = gson.fromJson(json, token);
        }
        return result;
    }

    private static void storeHttpConnectionProfiles(Context ctx, List<HttpConnectionProfile> profiles) {
        String json = new Gson().toJson(profiles);
        store(ctx, json, HTTP_CONNECTION_PROF_PREF);
    }

    public static void storeControllerSettingsPreferences(Context ctx, ControllerSetting controllerSetting) {
        String json = new Gson().toJson(controllerSetting);
        store(ctx, json, CONTROLLER_SETTINGS_PREF);
    }

    public static void storeChartPreferences(Context ctx, ChartPreferences chartPreferences) {
        Gson gson = new Gson();
        String json = gson.toJson(chartPreferences);
        store(ctx, json, CHART_PREF);
    }

    public static ChartPreferences getChartPreferences(Context ctx) {
        String chartPrefJson = get(ctx, CHART_PREF, getDefaultchartPreferences(ctx));
        if (!isBlank(chartPrefJson)) {
            Gson gson = new Gson();
            ChartPreferences chartPref = gson.fromJson(chartPrefJson, ChartPreferences.class);
            return chartPref;
        }
        return null;
    }

    public static void storeUSBConfiguration(Context ctx, USBConfiguration usbConfiguration) {
        Gson gson = new Gson();
        String json = gson.toJson(usbConfiguration);
        store(ctx, json, USB_CONFIGURATION_PREF);
    }

    public static USBConfiguration getUSBConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String usbConnectionJSON = settings.getString(USB_CONFIGURATION_PREF, getDefaultUSBConnection());
        if (!isBlank(usbConnectionJSON)) {
            Gson gson = new Gson();
            USBConfiguration usbConfiguration = gson.fromJson(usbConnectionJSON, USBConfiguration.class);
            return usbConfiguration;
        }
        return null;
    }

    public static WifiConfiguration getWifiConfiguration(Context ctx) {
        List<HttpConnectionProfile> httpConnectionProfiles = getHttpConnectionProfiles(ctx);
        if (!httpConnectionProfiles.isEmpty()) {
            return new WifiConfiguration(httpConnectionProfiles);
        }
        return null;
    }

    public static void storeBluetoothConfiguration(Context ctx, BluetoothConfiguration bluetoothConfiguration) {
        Gson gson = new Gson();
        String json = gson.toJson(bluetoothConfiguration);
        store(ctx, json, BLUETOOTH_CONFIGURATION_PREF);
    }

    public static void storeGeneralConfiguration(Context ctx, GeneralConfiguration generalConfiguration) {
        Gson gson = new Gson();
        String json = gson.toJson(generalConfiguration);
        store(ctx, json, GENERAL_CONFIGURATION_PREF);
    }

    public static void storeTerminalConfiguration(Context ctx, TerminalConfiguration terminalConfiguration) {
        Gson gson = new Gson();
        String json = gson.toJson(terminalConfiguration);
        store(ctx, json, TERMINAL_CONFIGURATION_PREF);
    }

    public static BluetoothConfiguration getBluetoothConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String usbConnectionJSON = settings.getString(BLUETOOTH_CONFIGURATION_PREF, getDefaultBluetoothConfiguration());
        if (!isBlank(usbConnectionJSON)) {
            Gson gson = new Gson();
            BluetoothConfiguration bluetoothConfiguration = gson.fromJson(usbConnectionJSON, BluetoothConfiguration.class);
            return bluetoothConfiguration;
        }
        return null;
    }


    public static GeneralConfiguration getGeneralConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String generalConfigurationJSON = settings.getString(GENERAL_CONFIGURATION_PREF, getDefaultGeneralConfiguration());
        if (!isBlank(generalConfigurationJSON)) {
            Gson gson = new Gson();
            GeneralConfiguration generalConfiguration = gson.fromJson(generalConfigurationJSON, GeneralConfiguration.class);
            return generalConfiguration;
        }
        return null;
    }

    public static TerminalConfiguration getTerminalConfiguration(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, ZERO);
        String terminalConfigurationJSON = settings.getString(TERMINAL_CONFIGURATION_PREF, getDefaultTerminalConfiguration());
        if (!isBlank(terminalConfigurationJSON)) {
            Gson gson = new Gson();
            TerminalConfiguration terminalConfiguration = gson.fromJson(terminalConfigurationJSON, TerminalConfiguration.class);
            return terminalConfiguration;
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

    private static String getDefaultControllerSettings(Context ctx) {
        ControllerSetting setting = new ControllerSetting();
        setting.setCommandButtonA(ctx.getString(R.string.empty));
        setting.setCommandButtonB(ctx.getString(R.string.empty));
        setting.setCommandButtonC(ctx.getString(R.string.empty));
        setting.setCommandButtonD(ctx.getString(R.string.empty));
        setting.setCommandButtonUp(ctx.getString(R.string.empty));
        setting.setCommandButtonDown(ctx.getString(R.string.empty));
        setting.setCommandButtonLeft(ctx.getString(R.string.empty));
        setting.setCommandButtonRight(ctx.getString(R.string.empty));
        return new Gson().toJson(setting);
    }

    private static String getDefaultchartPreferences(Context ctx) {
        ChartPreferences chartPreferences = new ChartPreferences();
        chartPreferences.setTitle(ctx.getResources().getString(R.string.chartText));
        chartPreferences.setValueAxis(ctx.getResources().getString(R.string.werteText));
        chartPreferences.setDateAxis(ctx.getResources().getString(R.string.datumText));
        chartPreferences.setSubTitle("");
        return new Gson().toJson(chartPreferences);
    }

    private static String getDefaultBluetoothConfiguration() {
        BluetoothConfiguration bluetoothConfiguration = new BluetoothConfiguration(true, true, true, false, "Hello from Android!");
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
        terminalConfiguration.setAllowSendEmptyMessages(false);
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
