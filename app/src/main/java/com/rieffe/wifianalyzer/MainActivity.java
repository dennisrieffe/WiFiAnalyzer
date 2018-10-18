package com.rieffe.wifianalyzer;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;


import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillStruct();
    }

    public void fillStruct(){
        final ArrayList<AP> allAP = new ArrayList<>();
        ListView TaskListView = (ListView) findViewById(R.id.list);

        allAP.add(new AP("a","5","c"));
        APAdapter adapter = new APAdapter(this, allAP);
        TaskListView.setAdapter(adapter);
    }

}
