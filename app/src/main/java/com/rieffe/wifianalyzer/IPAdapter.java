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

public class IPAdapter extends ArrayAdapter<IPInfo> {


    public IPAdapter(Activity context, ArrayList<IPInfo> task) {
        super(context, 0, task);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_current_network, parent, false);
        }
        IPInfo currentIP = getItem(position);
        TextView infoTextView = (TextView) listItemView.findViewById(R.id.network_info);
        infoTextView.setText(currentIP.getInfo());
        TextView dataTextView = (TextView) listItemView.findViewById(R.id.network_data);
        dataTextView.setText(currentIP.getData());
        return listItemView;

    }

}
