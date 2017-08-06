package draegerit.de.arduinoconsole.util;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.media.Image;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

import java.util.List;

import draegerit.de.arduinoconsole.R;

public class DriverAdapter extends ArrayAdapter<DriverWrapper> {

    private List<DriverWrapper> devices;

    private Context context;

    public DriverAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull List<DriverWrapper> devices) {
        super(context, resource, textViewResourceId, devices);
        this.devices = devices;
        this.context = context;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.devicespinnerlayout, parent, false);

        TextView deviceName = (TextView) layout.findViewById(R.id.deviceName);

        ImageView deviceTypeImage = (ImageView) layout.findViewById(R.id.deviceTypeImage);

        TextView adressTextView = (TextView) layout.findViewById(R.id.adressTextView);

        DriverWrapper device = devices.get(position);
        String name = "";
        switch (device.getType()) {
            case USB:
                name = ((UsbSerialDriver) device.getDriver()).getDevice().getDeviceName();
                adressTextView.setVisibility(View.GONE);
                break;
            case BLUETOOTH:
                BluetoothDevice bluetoothDevice = (BluetoothDevice) device.getDriver();
                name = bluetoothDevice.getName();
                adressTextView.setText(bluetoothDevice.getAddress());
                break;
        }

        setDeviceTypeImage(device, deviceTypeImage);

        if (!device.isBonded()) {
            name = name.concat(context.getString(R.string.not_bounded));
            deviceName.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        name = clearName(name);

        deviceName.setText(name);


        return layout;
    }

    private String clearName(String name) {
        return name.replaceAll("\n", "");
    }

    private void setDeviceTypeImage(DriverWrapper driverWrapper, ImageView imageView) {
        switch (driverWrapper.getType()) {
            case USB:
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.usb));
                break;
            case BLUETOOTH:
                BluetoothDevice bluetoothDevice = (BluetoothDevice) driverWrapper.getDriver();
                if (bluetoothDevice != null && bluetoothDevice.getBluetoothClass() != null) {
                    int deviceClass = bluetoothDevice.getBluetoothClass().getDeviceClass();
                    if (deviceClass < BluetoothClass.Device.Major.PHONE) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.computer));
                    } else if (deviceClass > BluetoothClass.Device.Major.PHONE && deviceClass < BluetoothClass.Device.Major.NETWORKING) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.phone));
                    } else if (deviceClass > BluetoothClass.Device.Major.NETWORKING && deviceClass < BluetoothClass.Device.Major.UNCATEGORIZED) {
                        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.chip));
                    }
                }
                break;
        }
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
