package com.shif.peterson.tizik.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.AllMusiqueAdapter;
import com.shif.peterson.tizik.adapter.AllMusiqueGridAdapter;
import com.shif.peterson.tizik.adapter.ExpandableListAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shif.peterson.tizik.utilis.Utils.tintMyDrawable;


public class AllMusicFragment extends Fragment implements
        NavigationView.OnNavigationItemSelectedListener,
        AllMusiqueGridAdapter.AudioClickHandler {


    private static final String ARG_PARAM1 = "param1";



    private String mParam1;

    AllMusiqueAdapter musicAdapter;
    AllMusiqueGridAdapter feedGridAdapter;
    List<Audio_Artiste> audioArtistes;

    View view;
    RecyclerView recyclerViewMusique;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration itemDecor;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    List<String> head;
    List<String> categorieChild;
    List<String> sortByDateChild;

    HashMap<String, List<String>> hashMap;


    private ImageView imglist;
    private ImageView imggrid;
    private ImageView imgfilter;
    private String presentation = "list";

    private MyTextView_Ubuntu_Bold txtMusiqueCount;


    public AllMusicFragment() {
        // Required empty public constructor
    }


    public static AllMusicFragment newInstance(String param1) {
        AllMusicFragment fragment = new AllMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_all_music, container, false);


        initUI();
        initList();
        initExpList();

        imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.END);
            }
        });



        if(presentation.equals("list")){
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.colorAccent));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.placeholder_bg));

//            endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, final RecyclerView view) {
//
//                    if(currentpage < totalpages){
//                        progressbar.setVisibility(View.VISIBLE);
//
//                        EndlessTask endlessTask = new EndlessTask();
//                        endlessTask.execute();
//                    }
//
//
//                }
//            };
        }else{
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.placeholder_bg));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.colorAccent));

//            endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, final RecyclerView view) {
//
//                    if(currentpage < totalpages){
//                        progressbar.setVisibility(View.VISIBLE);
//
//                        EndlessTask endlessTask = new EndlessTask();
//                        endlessTask.execute();
//                    }
//
//
//                }
//            };
        }


        imggrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(presentation.equals("list")){

                    presentation = "grid";

                    Drawable drawable = imggrid.getDrawable();
                    imggrid.setImageDrawable(tintMyDrawable(drawable, getResources().getColor(R.color.colorPrimary)));

                    Drawable drawable2 = imglist.getDrawable();
                    imglist.setImageDrawable(tintMyDrawable(drawable2, getResources().getColor(android.R.color.darker_gray)));


                    if(audioArtistes != null){
                        int spacin = 1;

                        int spanncount = getResources().getInteger(R.integer.prod_column_count);

                        recyclerViewMusique.removeAllViewsInLayout();
                        recyclerViewMusique.setAdapter(feedGridAdapter);
                        //layoutManager = new GridLayoutManager(getActivity(), spanncount);
                        layoutManager = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.shr_column_count));
                        recyclerViewMusique.removeItemDecoration(itemDecor);
                        //recyclerViewMusique.addItemDecoration(new GridSpacingItemDecoration(spanncount,  5, true));
                        recyclerViewMusique.setLayoutManager(layoutManager);
                    }


                }
            }


        });

        imglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(presentation.equals("grid")){

                    presentation = "list";

                    Drawable drawable = imglist.getDrawable();
                    Drawable drawable2 = imggrid.getDrawable();

                    imggrid.setImageDrawable(tintMyDrawable(drawable2, getResources().getColor(android.R.color.darker_gray)));
                    imglist.setImageDrawable(tintMyDrawable(drawable, getResources().getColor(R.color.colorPrimary)));

                    if(audioArtistes != null){

                        recyclerViewMusique.removeAllViewsInLayout();
                        recyclerViewMusique.setAdapter(musicAdapter);
                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerViewMusique.setLayoutManager(layoutManager);
                        recyclerViewMusique.addItemDecoration(itemDecor);
                    }

                }
            }


        });

        return view;
    }

    private void initUI(){

        txtMusiqueCount = view.findViewById(R.id.txtallmusiques);

        imgfilter  = view.findViewById(R.id.imgfilter);
        recyclerViewMusique = view.findViewById(R.id.recyr_all_musique);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);

        expandableListView = view.findViewById(R.id.explist_singlechoice);
        imglist = view.findViewById(R.id.imglist);
        imggrid = view.findViewById(R.id.imggrid);

    }

    private void initList() {

        audioArtistes = new ArrayList<>();

        for (int j = 0; j <= 10; j++) {

            Audio_Artiste produit = new Audio_Artiste();
            produit.setId_musique( "c580dcde-6de1-448a-9328-5c6da9474319");
            produit.setuploaded_by("vlrg2OpgCHZe4rDiG3UKwlssTv03");
            produit.setNom_chanteur("PIC");
            produit.setTitre_musique("T Button Ou Pa Welcome ft jndfdf");
            produit.setId_album( "Lem Ap Ekri [LAE] Mixtape Vol I");
            produit.setDuree_musique(169770);
            produit.setDate_upload(Timestamp.now().toDate());

            produit.setUrl_musique("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/Audio%2FOu%20Pa%20Welcome%20T-Bouton%20(Video).mp3?alt=media&token=7b04e338-c5c4-4ba7-a4c9-6465f3d742df");
            produit.setUrl_poster("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/Images%2FOu%20Pa%20Welcome%20T-Bouton%20(Video).mp3?alt=media&token=a49e785d-c03b-4d88-a017-30db93de09f5");

            audioArtistes.add( produit);

        }

//        musicAdapter = new AllMusiqueAdapter(view.getContext(), audioArtistes);
//        feedGridAdapter = new AllMusiqueGridAdapter(view.getContext(), audioArtistes, this);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        linearLayoutManager.setAutoMeasureEnabled(true);
//        recyclerViewMusique.setLayoutManager(linearLayoutManager);
//        itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerViewMusique.addItemDecoration(itemDecor);
//        recyclerViewMusique.setAdapter(musicAdapter);
//        recyclerViewMusique.setNestedScrollingEnabled(false);
//
//        txtMusiqueCount.setText(audioArtistes.size()+getString(R.string.music_found));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    private void initExpList() {


        head = new ArrayList<>();
        head.add(getString(R.string.categorie));
        head.add(getString(R.string.sortby));

        categorieChild = new ArrayList<>();

        FirebaseFirestore.getInstance().collection(Categorie.class.getSimpleName()).orderBy("nom_categorie", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){

                    List<Categorie> categories = queryDocumentSnapshots.toObjects(Categorie.class);
                    for (Categorie categorie : categories){

                        categorieChild.add(categorie.getNom_categorie());
                    }

                }
            }
        });

        sortByDateChild = new ArrayList<>();
        sortByDateChild.add(getString(R.string.croissant));
        sortByDateChild.add(getString(R.string.decroissant));


        hashMap = new HashMap<>();
        hashMap.put(head.get(0), categorieChild);
        hashMap.put(head.get(1), sortByDateChild);

        expandableListAdapter = new ExpandableListAdapter(getContext(), head, hashMap);
        expandableListView.setAdapter(expandableListAdapter);


    }

    @Override
    public void OnClick(Audio_Artiste produit) {

    }
}
