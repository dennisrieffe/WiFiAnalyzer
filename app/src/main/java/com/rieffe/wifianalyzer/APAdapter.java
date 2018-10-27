package com.rieffe.wifianalyzer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class APAdapter extends ArrayAdapter<AP> {

    public APAdapter(Activity context, ArrayList<AP> task) {
        super(context, 0, task);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        AP currentAP = getItem(position);

        if (currentAP != null) {
            ((TextView) listItemView.findViewById(R.id.SSID))
                    .setText("SSID: " + currentAP.getSSID());
            ((TextView) listItemView.findViewById(R.id.RSSI))
                    .setText("RSSI: " + currentAP.getRSSI());
            ((TextView) listItemView.findViewById(R.id.BSSID))
                    .setText("MAC: " + currentAP.getBSSID());
            ((TextView) listItemView.findViewById(R.id.capabilities))
                    .setText("Encryption Method: " + currentAP.getCapabilities());
        }

        return listItemView;
    }
}
