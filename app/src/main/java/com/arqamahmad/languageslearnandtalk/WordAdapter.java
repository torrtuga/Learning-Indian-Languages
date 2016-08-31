package com.arqamahmad.languageslearnandtalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by B on 8/28/2016.
 * Class created to populate the listView of the Fragments
 */
public class WordAdapter extends ArrayAdapter<CustomDataStructure> {

    public WordAdapter(Context context, ArrayList<CustomDataStructure> word) {
        super(context,0, word);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        CustomDataStructure currentWord = getItem(position);

        TextView translatedTextView = (TextView) listItemView.findViewById(R.id.translated_text_view);
        translatedTextView.setText(currentWord.getmTransledId());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getmDefaultTranslationId());


        return listItemView;
    }
}
