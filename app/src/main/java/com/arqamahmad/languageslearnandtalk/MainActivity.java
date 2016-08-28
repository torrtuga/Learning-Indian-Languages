package com.arqamahmad.languageslearnandtalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] languages = {"Kannada","Tamil","Telugu","Malayalam","Bengali"}; //String array with language name

        ListAdapter arrayAdapter = new ArrayAdapter<String>(this,R.layout.main_activity_array_adapter,
                R.id.textView,languages);

        ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter); //populating listview with the adapter

        //Starting activities for the particular languages on clicking
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch(position){
                    case 0:
                        Intent kannadaIntent = new Intent(MainActivity.this,Kannada.class);
                        startActivity(kannadaIntent);
                        break;
                }
            }
        });
    }
}
