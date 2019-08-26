package com.shif.peterson.tizik.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.SimilarAdater;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.ArrayList;
import java.util.List;

import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailAudioBottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailAudioBottomSheet extends BottomSheetDialogFragment implements CommentDialogFragment.OnMusicCommentListener, SimilarAdater.SimiClickListener {


    static String  LIST_SIMILAR_PARAM = "similar_param";
    static String  ID_AUDIO_PARAM = "id_audio_param";
    List<Audio_Artiste> similarList;
    String id_audio;
    SimilarAdater similarAdater;
    private TextView txtgenre;
    private TextView txtdesc;
    private TextView txtavis;
    private TextView txtprix;
    private Button btncomment;
    private Button btnfavori;
    private AppCompatRatingBar ratingBar;
    private RecyclerView recyclerViewsimilar;
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



    public DetailAudioBottomSheet() {
        // Required empty public constructor
    }

    public static DetailAudioBottomSheet newInstance(String id_audio, List<Audio_Artiste> similarList) {
        DetailAudioBottomSheet fragment = new DetailAudioBottomSheet();
        Bundle args = new Bundle();
        args.putString(ID_AUDIO_PARAM, id_audio);
        args.putParcelableArrayList(LIST_SIMILAR_PARAM, (ArrayList<? extends Parcelable>) similarList);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_detail_audio_bottom_sheet, null);
        dialog.setContentView(contentView);

        initUI(contentView);
        initListSimilar();
    }

    private void initUI(View view) {

        txtgenre = view.findViewById(R.id.txtcategorie);
        ratingBar = view.findViewById(R.id.ratingbar);
        txtavis = view.findViewById(R.id.txtavis);
        txtprix = view.findViewById(R.id.txtprix);
        txtdesc = view.findViewById(R.id.expandableTextView);
        recyclerViewsimilar = view.findViewById(R.id.recyclersimilar);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DetailAudioBottomSheet.STYLE_NO_FRAME, R.style.DialogTheme);
        if (getArguments() != null) {
            id_audio = getArguments().getString(ID_AUDIO_PARAM);
            similarList = getArguments().getParcelableArrayList(LIST_SIMILAR_PARAM);
        }
    }

    private void initListSimilar() {

        similarAdater = new SimilarAdater(getContext(), similarList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
       linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewsimilar.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerViewsimilar.addItemDecoration(itemDecor);
        recyclerViewsimilar.setAdapter(similarAdater);
        recyclerViewsimilar.setNestedScrollingEnabled(false);
        recyclerViewsimilar.setVisibility(View.VISIBLE);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        return inflater.inflate(R.layout.fragment_detail_audio_bottom_sheet, container, false);
//    }

    @Override
    public void onCommentDOne(int size) {

        if(size != 0){

            //txtavis.setText(String.valueOf(size));
        }

    }

    @Override
    public void onClick(int position) {

        Intent intent =  new Intent(getContext(), NowPlayingActivity.class);
        intent.putExtra(MUSIC_EXTRA, similarList.get(position));
        startActivity(intent);
    }
}
