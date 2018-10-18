package com.rieffe.wifianalyzer;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setApp();
    }

    public void setApp() {
        Button setList = (Button) findViewById(R.id.btn_to_list);
        setList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListAP.class);
                startActivity(intent);
            }
        });

        Button deviceInformation = (Button) findViewById(R.id.btn_to_device_information);
        deviceInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkInformation.class);
                startActivity(intent);
            }
        });
    }


}
