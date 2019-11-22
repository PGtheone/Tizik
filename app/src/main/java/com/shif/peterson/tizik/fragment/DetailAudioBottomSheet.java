package com.shif.peterson.tizik.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.SimilarAdater;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Favori;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailAudioBottomSheet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailAudioBottomSheet extends BottomSheetDialogFragment
        implements
        SimilarAdater.SimiClickListener {


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

    private ImageView imgfav;
    private ImageView imgcomment;

    private boolean isFav = false;
    private String id = null;

    private LinearLayout linearLayout;

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

    public static DetailAudioBottomSheet newInstance(String id_audios, List<Audio_Artiste> similarList) {
        DetailAudioBottomSheet fragment = new DetailAudioBottomSheet();
        Bundle args = new Bundle();
        args.putString(ID_AUDIO_PARAM, id_audios);
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
        linearLayout.setVisibility(View.VISIBLE);
        initListSimilar();
        initFavori();
        initComment();
        initCategorie();

        linearLayout.setVisibility(View.GONE);

    }

    private void initFavori() {

        FirebaseFirestore.getInstance()
                .collection("Favori")
                .whereEqualTo("id_utilisateur", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<Favori> favoriList = queryDocumentSnapshots.toObjects(Favori.class);

                    for (int i = 0; i < favoriList.size(); i++) {

                        Favori fav = favoriList.get(i);
                        if( fav.getId_media().equals(id_audio)){

                            isFav = true;
                            imgfav.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_favorite_black));
                            imgfav.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);
                            DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                            id =  snapshot.getId();

                        }else{

                            isFav = false;
                            imgfav.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_romantic));
                            imgfav.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);

                        }


                    }

                }


            }
        });


        imgfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFav){

                    FirebaseFirestore.getInstance()
                            .collection("Favori")
                            .document(id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    isFav = false;
                                    imgfav.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_romantic));
                                    //imgfav.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);

                                }
                            });

                }else{

                    Date now =  new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                    String date =  formatter.format(now);
                    Favori favori = new Favori(UUID.randomUUID().toString(), id_audio, FirebaseAuth.getInstance().getCurrentUser().getUid(),  FirebaseAuth.getInstance().getCurrentUser().getUid(), date);
                    FirebaseFirestore.getInstance()
                            .collection("Favori").document().set(favori).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            isFav = true;
                            imgfav.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_favorite_black));
                            imgfav.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN);


                        }
                    });

                }


            }
        });
    }

    private void initCategorie() {
    }

    private void initComment() {

        imgcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (id_audio != null && FirebaseAuth.getInstance().getCurrentUser() != null){

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    final CommentDialogFragment fragment = CommentDialogFragment.newInstance(id_audio,  FirebaseAuth.getInstance().getCurrentUser().getUid());
                    fragment.show(fragmentTransaction, "Comment dialog");
                }




            }
        });
    }

    private void initUI(View view) {

        txtgenre = view.findViewById(R.id.txtcategorie);
        ratingBar = view.findViewById(R.id.ratingbar);
        txtavis = view.findViewById(R.id.txtavis);
        txtprix = view.findViewById(R.id.txtprix);
        txtdesc = view.findViewById(R.id.expandableTextView);
        recyclerViewsimilar = view.findViewById(R.id.recyclersimilar);

        imgfav = view.findViewById(R.id.imgfav);
        imgcomment = view.findViewById(R.id.imgcomment);

        linearLayout = view.findViewById(R.id.linearprogress);

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

//    @Override
//    public void onCommentDOne(int size) {
//
//        if(size != 0){
//
//            //txtavis.setText(String.valueOf(size));
//        }
//
//    }

    @Override
    public void onClick(int position) {

        Intent intent =  new Intent(getContext(), NowPlayingActivity.class);
        intent.putExtra(MUSIC_EXTRA, similarList.get(position));
        startActivity(intent);
    }
}
