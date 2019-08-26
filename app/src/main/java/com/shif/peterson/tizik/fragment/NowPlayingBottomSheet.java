package com.shif.peterson.tizik.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;


public class NowPlayingBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_PARAM_MUSIC = "music_param";

    private Audio_Artiste audio;
    //private OnFragmentInteractionListener mListener;

    private BottomSheetBehavior.BottomSheetCallback
            mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, R.style.DialogTheme);
        View contentView = View.inflate(getContext(), R.layout.fragment_now_playing_bottom_sheet, null);
        dialog.setContentView(contentView);
    }


    public NowPlayingBottomSheet() {
        // Required empty public constructor
    }

    public static NowPlayingBottomSheet newInstance(Audio_Artiste audio) {
        NowPlayingBottomSheet fragment = new NowPlayingBottomSheet();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_MUSIC, audio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            audio = getArguments().getParcelable(ARG_PARAM_MUSIC);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing_bottom_sheet, container, false);
    }

//
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


//    public interface OnFragmentInteractionListener {
//
//        void onFragmentInteraction(Uri uri);
//    }
}
