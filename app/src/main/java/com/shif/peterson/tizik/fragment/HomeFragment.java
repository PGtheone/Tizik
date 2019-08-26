package com.shif.peterson.tizik.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.DetailPlaylistActivity;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.PlaylistAdapter;
import com.shif.peterson.tizik.adapter.RecyclerDataAdapter;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.Playlist;
import com.shif.peterson.tizik.model.SectionDataModel;

import java.util.ArrayList;
import java.util.List;

import static com.shif.peterson.tizik.DetailPlaylistActivity.EXTRA_PLAYLIST;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
       {

    private static final int LIMIT = 10;
    private static final String COLLECTION_NAME_AUDIO = "Audio";
    private final ArrayList<SectionDataModel> allMusic = new ArrayList<SectionDataModel>();
    private final String MUSIC_EXTRA = "music_extra";
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView listProduitRecyclerview;
    private final List<Categorie> allcategories = new ArrayList<>();
    private final List<Playlist> allPlaylist = new ArrayList<>();
    private final int ACTIVITY_REQUEST_CODE = 10000;
    private ArrayList<Object> mainObject = new ArrayList<>();
    private NowPlayingHandler nowPlayingHandler;
    private RecyclerDataAdapter dataAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

       // mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        listProduitRecyclerview = view.findViewById(R.id.listaudiorecycler);

        listProduitRecyclerview.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

     initList();

        return view;

    }


    private void initList() {

        Task taskfree = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        //Playlist
        Task taskPlaylist = FirebaseFirestore
                .getInstance()
                .collection("Playlist")
                .limit(10)
                .get();

        Task taskCategorie =    FirebaseFirestore.getInstance()
                .collection(Categorie.class.getSimpleName())
                .orderBy("nom_categorie", Query.Direction.ASCENDING)
                .limit(6)
                .get();


        Task taskrecommended = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        Task tasknew =   FirebaseFirestore.getInstance()
                .collection("Audio")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();



        final Task<List<QuerySnapshot>> allMainTask = Tasks.whenAllSuccess(taskfree,taskPlaylist,taskCategorie,taskrecommended,tasknew);
        allMainTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

                for(int i = 0; i < querySnapshots.size(); i++){
                    QuerySnapshot queryDocumentSnapshots =  querySnapshots.get(i);

                    if (i == 1){

                        if (!queryDocumentSnapshots.isEmpty()){

                            allPlaylist.addAll(queryDocumentSnapshots.toObjects(Playlist.class));
                            mainObject.add(allPlaylist);


                        }
                    }else if(i == 2){

                        if (!queryDocumentSnapshots.isEmpty()){


                        allcategories.addAll(queryDocumentSnapshots.toObjects(Categorie.class));
                            mainObject.add(allcategories);

                }
                    } else{

                        if(!queryDocumentSnapshots.isEmpty()){

                            SectionDataModel dm = new SectionDataModel();
                            dm.setHeaderTitle(getString(R.string.gratuit));
                            ArrayList<Audio_Artiste> singleItem = new ArrayList<Audio_Artiste>();

                            singleItem.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                            dm.setAllItemsInSection(singleItem);

                            allMusic.add(dm);
                            mainObject.add(allMusic);
                        }


                    }
                }



                dataAdapter = new RecyclerDataAdapter(getContext(), mainObject, new RecyclerDataAdapter.ProductHandler() {
                    @Override
                    public void onClick(Audio_Artiste audio) {
                        Intent intent2 =  new Intent(getContext(), NowPlayingActivity.class);
                        intent2.putExtra(MUSIC_EXTRA, audio);
                        startActivity(intent2);

                    }
                }, new PlaylistAdapter.PlaylistHandler() {
                    @Override
                    public void onPlaylistClick(Playlist position) {

                        Intent intent2 =  new Intent(getContext(), DetailPlaylistActivity.class);
                        intent2.putExtra(EXTRA_PLAYLIST, position);
                        startActivity(intent2);

                    }

                    @Override
                    public void onPlayClick(Playlist position) {

                    }
                });


                listProduitRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL, false));
                listProduitRecyclerview.setAdapter(dataAdapter);
                listProduitRecyclerview.setNestedScrollingEnabled(false);
                listProduitRecyclerview.getAdapter().notifyDataSetChanged();

                mShimmerViewContainer.setVisibility(View.GONE);
                listProduitRecyclerview.setVisibility(View.VISIBLE);

            }
        });


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onMusiChoosen(Audio_Artiste audio_artiste) {
        if (nowPlayingHandler != null) {
            nowPlayingHandler.nowPlayingListener(audio_artiste);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.NowPlayingHandler) {
            nowPlayingHandler = (HomeFragment.NowPlayingHandler) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        nowPlayingHandler = null;
    }



    public interface NowPlayingHandler{

         void nowPlayingListener(Audio_Artiste audio_artiste);
    }




}
