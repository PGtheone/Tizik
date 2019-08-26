package com.shif.peterson.tizik.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.shif.peterson.tizik.R;


public class PlanDialogFragment extends DialogFragment {

    private static final String ARG_PLAN = "plan";

    private String plan;
    private Button btnok;
    private TextView txthead;
    private TextView txtdesc;
    View view;



    public PlanDialogFragment() {
        // Required empty public constructor
    }


    public static PlanDialogFragment newInstance(String plan) {
        PlanDialogFragment fragment = new PlanDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAN, plan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plan = getArguments().getString(ARG_PLAN);

        }
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_plan_dialog, container, false);

        txthead = view.findViewById(R.id.txthead);
        txtdesc = view.findViewById(R.id.desctimerest);
        btnok = view.findViewById(R.id.btnok);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDialog().dismiss();
            }
        });

        return view;
    }

}
