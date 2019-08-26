package com.shif.peterson.tizik.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.shif.peterson.tizik.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbulmPlayingFragment extends Fragment {


    public AbulmPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_abulm_playing, container, false);
    }

}
