package com.rieffe.wifianalyzer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAP extends AppCompatActivity {


    final ArrayList<AP> allAP = new ArrayList<>();
    private final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private WifiManager wifiManager;
    private ListView TaskListView;
    private Button buttonScan;
    private List<ScanResult> results;
    private APAdapter APAdapter;
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            List<AP> temp = new ArrayList<>();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                temp.add(new AP(scanResult.SSID, scanResult.level, scanResult.BSSID));
                APAdapter.notifyDataSetChanged();
            }
            Collections.sort(temp, new Comparator<AP>() {
                @Override
                public int compare(AP ap, AP t1) {
                    return t1.getRSSI() - ap.getRSSI();
                }
            });
            allAP.clear();
            allAP.addAll(temp);

//            for (int i = 0; i < allAP.size(); i++) {
//                if (allAP.get(i).getSSID().isEmpty() || allAP.get(i).getSSID().equals("")) {
//                    allAP.get(i).setSSID("Hidden SSID");
//                }
//            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }

        setContentView(R.layout.activity_list_ap);
        buttonScan = findViewById(R.id.scan_btn);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
        TaskListView = (ListView) findViewById(R.id.list);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled, enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        APAdapter = new APAdapter(this, allAP);
        TaskListView.setAdapter(APAdapter);
    }

    private void startScan() {
        allAP.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Performing scan", Toast.LENGTH_SHORT).show();
    }

}
