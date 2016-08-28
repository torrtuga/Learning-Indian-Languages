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

/**
 * Created by B on 8/27/2016.
 */
public class ChatFragment extends Fragment {

   public ChatFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout,container,false);

        String[] languages = {"Kannada","Tamil","Telugu","Malayalam","Bengali"}; //String array with language name
        ListAdapter arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,languages);
        ListView listView = (ListView)rootView.findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);
        Toast.makeText(getActivity(),"Inside Chat Fragment",Toast.LENGTH_SHORT).show();

        return rootView;
    }
}
