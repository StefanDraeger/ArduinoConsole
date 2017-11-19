package draegerit.de.arduinoconsole.configuration.httpconnection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import draegerit.de.arduinoconsole.R;

/**
 * Created by Stefan Draeger on 19.11.2017.
 */

public class HttpConnectionProfileArrayAdapter extends BaseAdapter {

    private List<HttpConnectionProfile> profiles;
    private Context ctx;

    public HttpConnectionProfileArrayAdapter(Context ctx, List<HttpConnectionProfile> httpConnectionProfiles) {
        this.profiles = httpConnectionProfiles;
        this.ctx = ctx;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Object getItem(int position) {
        return profiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.httpconnection_item, parent, false);

        HttpConnectionProfile profile = profiles.get(position);

        TextView profilename = (TextView) view.findViewById(R.id.profilename);
        profilename.setText(profile.getProfileName());

        TextView serveradress = (TextView) view.findViewById(R.id.serveradress);
        serveradress.setText(profile.getServerAddress());

        return view;
    }
}
