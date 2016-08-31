package com.arqamahmad.languageslearnandtalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by B on 8/27/2016.
 */
public class NumbersFragment extends Fragment {

    public NumbersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout, container, false);

        ArrayList<CustomDataStructure> words = new ArrayList<CustomDataStructure>();


        if (MainActivity.numbersResponse == "") {
            ListView listview =(ListView) rootView.findViewById(R.id.list);
            listview.setDivider(null);
            listview.setDividerHeight(0);

        } else {
            //parsing the response into CustomDataStructure
            String response = MainActivity.numbersResponse;
            String[] dataArray = response.split("\\*");
            int i = 0;
            for (i = 0; i < dataArray.length; i++) {
                String[] songArray = dataArray[i].split(",");
                CustomDataStructure temp = new CustomDataStructure(songArray[2], songArray[1]); //To make the language word at top
                words.add(temp);
            }

            //Adapter to populate the ListView
            WordAdapter adapter = new WordAdapter(getActivity(), words);
            final ListView listView = (ListView) rootView.findViewById(R.id.list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (Player.player == null) {
                        new Player();
                    }
                    CustomDataStructure temp = (CustomDataStructure)listView.getItemAtPosition(position);
                    String str = temp.getmDefaultTranslationId();
                    String url = "http://arqamahmad.com/music_app/bensound-cute.mp3";
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    Player.player.playStream(url);
                }
            });

        }

        return rootView;
    }
}
