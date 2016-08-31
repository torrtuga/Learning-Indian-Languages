package com.arqamahmad.languageslearnandtalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by B on 8/27/2016.
 */
public class PronounsFragment extends Fragment {

    public PronounsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout,container,false);



        ArrayList<CustomDataStructure> words = new ArrayList<CustomDataStructure>();

        if(MainActivity.pronounsResponse == ""){
            Toast.makeText(getActivity(),"Internet Problem",Toast.LENGTH_SHORT).show();
            String[] languages = {"Kannada","Tamil","Telugu","Malayalam","Bengali"}; //String array with language name
            ListAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,languages);
            ListView listView = (ListView)rootView.findViewById(R.id.list);
            listView.setAdapter(arrayAdapter);
        }
        else {
            //parsing the response into CustomDataStructure
            String response = MainActivity.pronounsResponse;
            String[] dataArray = response.split("\\*");
            int i = 0;
            for (i = 0; i < dataArray.length; i++) {
                String[] songArray = dataArray[i].split(",");
                CustomDataStructure temp = new CustomDataStructure(songArray[2], songArray[1]); //To make the language word at top
                words.add(temp);
            }

            //Adapter to populate the ListView
            WordAdapter adapter = new WordAdapter(getActivity(), words);
            ListView listView = (ListView) rootView.findViewById(R.id.list);
            listView.setAdapter(adapter);
        }

        return rootView;
    }
}
