package com.cristhianbonilla.moviereview.Fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cristhianbonilla.moviereview.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Comentar extends DialogFragment {


    public Comentar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comentar, container, false);
        return v;
    }

}
