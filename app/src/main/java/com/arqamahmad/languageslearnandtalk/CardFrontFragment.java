package com.arqamahmad.languageslearnandtalk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by B on 9/2/2016.
 */
public class CardFrontFragment extends android.app.Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card_front,container,false);
        return v;
    }
}
