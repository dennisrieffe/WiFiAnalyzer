package com.rieffe.wifianalyzer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAP extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ap);

        fillStruct();
    }

    public void fillStruct() {
        final ArrayList<AP> allAP = new ArrayList<>();
        ListView TaskListView = (ListView) findViewById(R.id.list);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);


        allAP.add(new AP("65:48:fe", 70, "Eduroam"));
        allAP.add(new AP("48:79:2e", 60, "UNOWifi"));
        APAdapter adapter = new APAdapter(this, allAP);
        TaskListView.setAdapter(adapter);
    }


}
