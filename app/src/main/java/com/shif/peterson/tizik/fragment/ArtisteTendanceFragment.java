package com.shif.peterson.tizik.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;


public class ArtisteTendanceFragment extends Fragment {

    private static final int LIMIT = 20;
    private static final String COLLECTION_NAME_USER = "Utilisateur";
    ChipGroup chipGroup;
    View view;
    FirebaseFirestore mFirestore;
    List<Utilisateur> utilisateurList;
    List<Utilisateur> selectedArtist;
     private OnArtistChoosenListener mListener;

    public static CollectionReference getUtilisateurCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME_USER);
    }

    public ArtisteTendanceFragment() {
        // Required empty public constructor
    }

    public static ArtisteTendanceFragment newInstance() {
        ArtisteTendanceFragment fragment = new ArtisteTendanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_artiste_tendance, container, false);

        chipGroup = view.findViewById(R.id.chipgroup);


        initFirestore();

    return view;
    }

    private void initFirestore() {

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();


        getUtilisateurCollectionReference().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                if (queryDocumentSnapshots.isEmpty()){

                    //todo skip this step
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();

                }else{

                    utilisateurList = queryDocumentSnapshots.toObjects(Utilisateur.class);
                    initChipGroup(utilisateurList);


                }
            }
        });

    }



    public void initChipGroup(List<Utilisateur> utilisateurs){

        selectedArtist = new ArrayList<>();
        for (final Utilisateur utilisateur : utilisateurs){


            final Chip chip = new Chip(getActivity());
            chip.setText(utilisateur.getNom_complet());
            chip.setCheckable(true);
            chip.setTag(utilisateur);
            if(utilisateur.getUrl_photo() != null){

                RequestOptions glideCircle = new RequestOptions()
                        .centerCrop()
                        .transform( new CircleCrop());

                Glide.with(getContext())
                        .load(utilisateur.getUrl_photo())
                        .apply(glideCircle)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                                chip.setChipIcon(resource);
                            }
                        });

            }

            chip.setChipBackgroundColorResource(R.color.placeholder_bg);
            chip.setTextAppearanceResource(android.R.style.TextAppearance_Small);
            chip.setGravity(View.TEXT_ALIGNMENT_CENTER);

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   if(isChecked){

                        Utilisateur utilisateur = (Utilisateur) buttonView.getTag();
                        selectedArtist.add(utilisateur);
                        onButtonPressed(utilisateur, true);

                   }else{

                       Utilisateur utilisateur = (Utilisateur) buttonView.getTag();
                       selectedArtist.remove(utilisateur);
                       onButtonPressed(utilisateur, false);
                   }
                }
            });

            chipGroup.addView(chip);
        }
    }


    public void onButtonPressed(Utilisateur uri, boolean choosen) {
        if (mListener != null) {
            mListener.onArtisteChoosen(uri, choosen);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArtistChoosenListener) {
            mListener = (OnArtistChoosenListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnArtistChoosenListener {

        void onArtisteChoosen(Utilisateur utilisateur, boolean choosen);
    }



}
