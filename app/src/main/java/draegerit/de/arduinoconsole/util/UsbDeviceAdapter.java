package draegerit.de.arduinoconsole.util;

import android.content.Context;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import draegerit.de.arduinoconsole.R;

public class UsbDeviceAdapter extends ArrayAdapter<UsbDevice> {

    private List<UsbDevice> usbDevices;

    private Context context;

    public UsbDeviceAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<UsbDevice> usbDevices) {
        super(context, resource, textViewResourceId, usbDevices);
        this.usbDevices = usbDevices;
        this.context = context;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.devicespinnerlayout, parent, false);

        TextView deviceName = (TextView) layout.findViewById(R.id.deviceName);
        deviceName.setText(usbDevices.get(position).getDeviceName());

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
