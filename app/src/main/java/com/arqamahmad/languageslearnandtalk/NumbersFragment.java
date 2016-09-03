package com.arqamahmad.languageslearnandtalk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by B on 8/27/2016.
 */
public class NumbersFragment extends Fragment {

    public NumbersFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView;
        if (MainActivity.numbersResponse == "") {
            rootView = inflater.inflate(R.layout.fragment_no_internet,container,false);

        } else {
            rootView = inflater.inflate(R.layout.fragment_layout,container,false);
            ArrayList<CustomDataStructure> words = new ArrayList<CustomDataStructure>();
            //parsing the response into CustomDataStructure
            String response = MainActivity.numbersResponse;
            String[] dataArray = response.split("\\*");
            int i;
            for  (i =0; i<dataArray.length; i++){
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
                    String str = Integer.toString(position);
                    String url = "http://arqamahmad.com/language_app/KannadaNumbers/"+str+".mp3";
                    //Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    Player.player.playStream(url);
                }
            });

        }

        return rootView;
    }
}
