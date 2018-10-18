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

public class APAdapter extends ArrayAdapter<AP>{

    public APAdapter(Activity context, ArrayList<AP> task) {
        super(context, 0, task);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        AP currentAP = getItem(position);

        TextView SSIDTextView = (TextView) listItemView.findViewById(R.id.SSID);
        SSIDTextView.setText(currentAP.getSSID());
        TextView RSSITextView = (TextView) listItemView.findViewById(R.id.RSSI);
        RSSITextView.setText(currentAP.getRSSI());
        TextView BSSIDTextView = (TextView) listItemView.findViewById(R.id.BSSID);
        BSSIDTextView.setText(currentAP.getBSSID());
        return listItemView;

    }



}
