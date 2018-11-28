package com.shif.peterson.tizik.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.CarouselAdapter;
import com.shif.peterson.tizik.adapter.RecyclerDataAdapter;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.SectionDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RecyclerDataAdapter.ProductHandler {

    private final String MUSIC_EXTRA = "music_extra";

    private ShimmerFrameLayout mShimmerViewContainer;

    private RecyclerView listProduitRecyclerview;
    ArrayList<SectionDataModel> allMusic;
    RecyclerDataAdapter dataAdapter;

    //caarousel
    private ViewPager carousel;
    private CarouselAdapter carouselAdapter;
    private List<String> carouselImage;
    private int current_page = 0;
    private int NUM_PAGES = 0;

    private List<String> listPubrow;
    private View view;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        listProduitRecyclerview = (RecyclerView) view.findViewById(R.id.listaudiorecycler);
        carousel = (ViewPager) view.findViewById(R.id.carousel);

        allMusic = new ArrayList<SectionDataModel>();

        createDummyData();


        listProduitRecyclerview.setHasFixedSize(true);
        dataAdapter = new RecyclerDataAdapter(getContext(), allMusic,listPubrow, this);

        listProduitRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        listProduitRecyclerview.setAdapter(dataAdapter);
        listProduitRecyclerview.setNestedScrollingEnabled(false);


        carouselAdapter = new CarouselAdapter(getContext(), carouselImage);
        carousel.setAdapter(carouselAdapter);
        NUM_PAGES = carouselImage.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                carousel.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);


        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 7000L);




        listProduitRecyclerview.setVisibility(View.VISIBLE);
        mShimmerViewContainer.setVisibility(View.GONE);


        return view;

    }

    private void createDummyData() {

        carouselImage = new ArrayList<>();
        carouselImage.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel3.jpg?alt=media&token=253a0e79-698b-4e38-80c1-5c0339727c9e");
        carouselImage.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel5.jpg?alt=media&token=22cc988f-706e-45dc-86a3-a56e0cc234ea");
        carouselImage.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel1.jpg?alt=media&token=793b0d9e-f61c-4bb1-9952-a24f361f4cfc");
        carouselImage.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel4.jpg?alt=media&token=85825417-b070-4c6f-8c5b-6ea7a6dbff82");
        carouselImage.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel2.jpg?alt=media&token=29095dc9-e725-48a7-b5fd-f4a4ea1c42b9");


        listPubrow = new ArrayList<>();
        listPubrow.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fpubrow2.jpg?alt=media&token=4472a69a-a420-4e4d-ad09-ddc4ccafe6fb");
        listPubrow.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fpubrow1.jpg?alt=media&token=5e03b3cb-cdd6-4160-9121-b650a13f480c");
        listPubrow.add("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fpubrow1.jpg?alt=media&token=5e03b3cb-cdd6-4160-9121-b650a13f480c");


        for (int i = 1; i <= 10; i++) {

            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Titre Section " + i);

            ArrayList<Audio_Artiste> singleItem = new ArrayList<Audio_Artiste>();
            for (int j = 0; j <= 5; j++) {

                Audio_Artiste produit = new Audio_Artiste();
                produit.setId_musique( "c580dcde-6de1-448a-9328-5c6da9474319");
                produit.setuploaded_by("vlrg2OpgCHZe4rDiG3UKwlssTv03");
                produit.setNom_chanteur("PIC");
                produit.setTitre_musique("Higher");
                produit.setId_album( "Lem Ap Ekri [LAE] Mixtape Vol I");
                produit.setDuree_musique(169770);
                produit.setDate_upload("Wed Nov 14 16:50:29 EST 2018");

                produit.setUrl_musique("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/Audio%2F09-PIC-MAprann.mp3?alt=media&token=cffba160-d342-430b-acb3-5f4a2a4aa2b2");
                produit.setUrl_poster("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/Images%2F09-PIC-MAprann.mp3?alt=media&token=66b2adfc-46a1-48c8-bab3-b652232d3438");
                singleItem.add(produit);

            }

            dm.setAllItemsInSection(singleItem);

            allMusic.add(dm);

        }



    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    public void onClick(Audio_Artiste audio) {

        Intent intent =  new Intent(getContext(), NowPlayingActivity.class);
        intent.putExtra(MUSIC_EXTRA, audio);
        startActivity(intent);
    }
}
