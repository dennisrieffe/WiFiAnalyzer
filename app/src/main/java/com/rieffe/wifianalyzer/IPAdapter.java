package com.rieffe.wifianalyzer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Optional;

public class IPAdapter extends ArrayAdapter<IPInfo> {

    public IPAdapter(Activity context, ArrayList<IPInfo> task) {
        super(context, 0, task);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = Optional.ofNullable(convertView)
                .orElse(LayoutInflater.from(getContext()).inflate(R.layout.list_item_current_network, parent, false));

        Optional.ofNullable(getItem(position))
                .ifPresent(currentIP -> {
                    ((ImageView) listItemView.findViewById(R.id.list_picture))
                            .setImageResource(currentIP.getPicture());
                    ((TextView) listItemView.findViewById(R.id.network_info))
                            .setText(currentIP.getInfo());
                    ((TextView) listItemView.findViewById(R.id.network_data))
                            .setText(currentIP.getData());
                });

        return listItemView;
    }
}
