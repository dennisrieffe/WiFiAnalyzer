package com.rieffe.wifianalyzer;

/*
This class extends the ArrayAdapter class and overrides multiple methods to set lay out of different views for the available wifi networks.
 */

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
import java.util.Optional;


//This method sets the network information of one specific AP.
public class APAdapter extends ArrayAdapter<AP> {

    public APAdapter(Activity context, ArrayList<AP> AP) {
        super(context, 0, AP);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = Optional.ofNullable(convertView)
                .orElse(LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false));

        Optional.ofNullable(getItem(position))
                .ifPresent(currentAP -> {
                    ((TextView) listItemView.findViewById(R.id.SSID))
                            .setText("SSID: " + currentAP.getSSID());
                    ((TextView) listItemView.findViewById(R.id.BSSID))
                            .setText("MAC: " + currentAP.getBSSID());
                    ((TextView) listItemView.findViewById(R.id.capabilities))
                            .setText("EM: " + currentAP.getCapabilities());
                    TextView RSSITextView = listItemView.findViewById(R.id.RSSI);
                    ((GradientDrawable) RSSITextView.getBackground()).
                            setColor(getRSSIColor(currentAP.getRSSI()));
                    RSSITextView.setText("" + currentAP.getRSSI());
                });

        return listItemView;
    }

    //A method to determine the color of the circle based on the RSSI
    private int getRSSIColor(int RSSI) {
        int RSSIID;

        if (RSSI > 1 && RSSI <= 40) {
            RSSIID = R.color.rssi1;
        } else if (RSSI > 40 && RSSI <= 45) {
            RSSIID = R.color.rssi2;
        } else if (RSSI > 45 && RSSI <= 50) {
            RSSIID = R.color.rssi3;
        } else if (RSSI > 50 && RSSI <= 55) {
            RSSIID = R.color.rssi4;
        } else if (RSSI > 55 && RSSI <= 60) {
            RSSIID = R.color.rssi5;
        } else if (RSSI > 60 && RSSI <= 65) {
            RSSIID = R.color.rssi6;
        } else if (RSSI > 65 && RSSI <= 70) {
            RSSIID = R.color.rssi7;
        } else if (RSSI > 70 && RSSI <= 75) {
            RSSIID = R.color.rssi8;
        } else if (RSSI > 75 && RSSI <= 80) {
            RSSIID = R.color.rssi9;
        } else {
            RSSIID = R.color.rssi10;
        }
        
        return ContextCompat.getColor(getContext(), RSSIID);
    }

}
