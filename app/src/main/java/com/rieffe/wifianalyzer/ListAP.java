package com.rieffe.wifianalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

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

        allAP.add(new AP("65:48:fe", "70", "Eduroam"));
        allAP.add(new AP("48:79:2e", "65", "UNOWifi"));
        APAdapter adapter = new APAdapter(this, allAP);
        TaskListView.setAdapter(adapter);
    }
}