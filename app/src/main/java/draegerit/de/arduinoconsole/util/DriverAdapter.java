package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

import java.util.List;

import draegerit.de.arduinoconsole.R;

public class DriverAdapter extends ArrayAdapter<DriverWrapper> {

    private List<DriverWrapper> devices;

    private Context context;

    private Class<?> deviceClazz;

    public DriverAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<DriverWrapper> devices, Class<?> deviceClazz) {
        super(context, resource, textViewResourceId, devices);
        this.devices = devices;
        this.context = context;
        this.deviceClazz = deviceClazz;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.devicespinnerlayout, parent, false);

        TextView deviceName = (TextView) layout.findViewById(R.id.deviceName);
        DriverWrapper device = devices.get(position);
        String name = "";
        switch (device.getType()) {
            case USB:
                name = ((UsbSerialDriver) device.getDriver()).getDevice().getDeviceName();
                break;
            case BLUETOOTH:
                name = "";
                break;
        }
        deviceName.setText(name);

        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
