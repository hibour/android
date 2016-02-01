package com.dsquare.hibour.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsquare.hibour.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Suggestions extends Fragment {


    public Suggestions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggestions, container, false);
    }


}
