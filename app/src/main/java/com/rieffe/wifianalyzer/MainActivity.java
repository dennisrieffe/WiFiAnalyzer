package com.rieffe.wifianalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setApp();
    }

    public void setApp() {
        findViewById(R.id.btn_to_list)
                .setOnClickListener(v -> startActivity(
                        new Intent(MainActivity.this, ListAP.class)));

        findViewById(R.id.btn_to_device_information)
                .setOnClickListener(v -> startActivity(
                        new Intent(MainActivity.this, NetworkInformation.class)));
    }


}
