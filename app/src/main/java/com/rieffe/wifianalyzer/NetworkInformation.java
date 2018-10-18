package com.rieffe.wifianalyzer;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.TextView;

public class NetworkInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_information);

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        int frequency = wm.getConnectionInfo().getFrequency();
        StringBuilder sb = new StringBuilder();
        sb.append(frequency);
        String frequencyString = sb.toString();
        TextView ipText = (TextView) findViewById(R.id.device_ip);
        ipText.setText(ip);
        TextView ipFrequency = (TextView) findViewById(R.id.device_frequency);
        ipFrequency.setText(frequencyString);

    }
}
