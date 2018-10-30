package com.rieffe.wifianalyzer;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
            ((TextView) listItemView.findViewById(R.id.BSSID))
                    .setText("MAC: " + currentAP.getBSSID());
            ((TextView) listItemView.findViewById(R.id.capabilities))
                    .setText("EM: " + currentAP.getCapabilities());
            TextView RSSITextView = (TextView) listItemView.findViewById(R.id.RSSI);
            ((GradientDrawable) RSSITextView.getBackground()).
                    setColor(getRSSIColor(currentAP.getRSSI()));
            RSSITextView.setText("" + currentAP.getRSSI());
        }
        return listItemView;
    }

    private int getRSSIColor(int RSSI) {
        int magnitudeResourceID;
        if (RSSI > 1 && RSSI <= 40) {
            magnitudeResourceID = R.color.rssi1;
        } else if (RSSI > 40 && RSSI <= 45) {
            magnitudeResourceID = R.color.rssi2;
        } else if (RSSI > 45 && RSSI <= 50) {
            magnitudeResourceID = R.color.rssi3;
        } else if (RSSI > 50 && RSSI <= 55) {
            magnitudeResourceID = R.color.rssi4;
        } else if (RSSI > 55 && RSSI <= 60) {
            magnitudeResourceID = R.color.rssi5;
        } else if (RSSI > 60 && RSSI <= 65) {
            magnitudeResourceID = R.color.rssi6;
        } else if (RSSI > 65 && RSSI <= 70) {
            magnitudeResourceID = R.color.rssi7;
        } else if (RSSI > 70 && RSSI <= 75) {
            magnitudeResourceID = R.color.rssi8;
        } else if (RSSI > 75 && RSSI <= 80) {
            magnitudeResourceID = R.color.rssi9;
        } else {
            magnitudeResourceID = R.color.rssi10;
        }
        return ContextCompat.getColor(getContext(), magnitudeResourceID);
    }

}
