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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ListAP extends AppCompatActivity {

    final ArrayList<AP> allAP = new ArrayList<>();
    private WifiManager wifiManager;
    private APAdapter APAdapter;

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<AP> temp = new ArrayList<>();
            unregisterReceiver(this);

            //TODO find the correct name for this one
            wifiManager.getScanResults().forEach(scanResult -> {
                String SSID = scanResult.SSID.contains("Find the correct name")
                        ? "Hidden SSID"
                        : scanResult.SSID;

                temp.add(new AP(scanResult.BSSID, -scanResult.level, SSID, scanResult.capabilities));
                APAdapter.notifyDataSetChanged();
            });

            temp.sort((ap, t1) -> ap.getRSSI() - t1.getRSSI());
            allAP.clear();
            allAP.addAll(temp);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
        }

        setContentView(R.layout.activity_list_ap);
        findViewById(R.id.scan_btn).setOnClickListener(view -> startScan());
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && !wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled, enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        APAdapter = new APAdapter(this, allAP);
        ((ListView) findViewById(R.id.list)).setAdapter(APAdapter);
    }

    private void startScan() {
        allAP.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Performing scan", Toast.LENGTH_SHORT).show();
    }

}
