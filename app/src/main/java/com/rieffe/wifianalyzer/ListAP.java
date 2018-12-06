package com.rieffe.wifianalyzer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contains a broadcast receiver which is used to scan for the Wi-Fi results. Scanning results are
 * added to the APAdapter.
 */
public class ListAP extends AppCompatActivity {

    final ArrayList<AP> allAP = new ArrayList<>();
    private @Nullable
    WifiManager wifiManager;
    private APAdapter APAdapter;

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<AP> temp = new ArrayList<>();
            unregisterReceiver(this);

            wifiManager.getScanResults().forEach(scanResult -> {
                String SSID = scanResult.SSID.contains("")
                        ? "Hidden SSID"
                        : scanResult.SSID;

                temp.add(new AP(scanResult.BSSID, -scanResult.level, SSID, scanResult.capabilities));
                APAdapter.notifyDataSetChanged();
            });

            temp.sort(Comparator.comparingInt(AP::getRSSI));
            allAP.clear();
            allAP.addAll(temp);
        }
    };

    /**
     * Wi-Fi Manager is used to collect the current available networks. These networks are then
     * sent to the APAdapter.
     *
     * @param savedInstanceState - The saved instance state; contains relevant Android setting
     *                           values.
     */
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

    /**
     * Initiates a new scan.
     */
    private void startScan() {
        allAP.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        if (wifiManager != null) {
            wifiManager.startScan();
        }

        Toast.makeText(this, "Performing scan", Toast.LENGTH_SHORT).show();
    }

}
