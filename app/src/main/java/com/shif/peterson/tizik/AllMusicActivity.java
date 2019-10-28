package com.shif.peterson.tizik;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.AllMusiqueAdapter;
import com.shif.peterson.tizik.adapter.AllMusiqueGridAdapter;
import com.shif.peterson.tizik.adapter.ExpandableListAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.utilis.EndlessRecyclerViewScrollListener;
import com.shif.peterson.tizik.utilis.SpannedGridLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shif.peterson.tizik.utilis.Utils.tintMyDrawable;

public class AllMusicActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AllMusiqueGridAdapter.AudioClickHandler,
        AllMusiqueAdapter.AudioClickHandler
{

    final static String EXTRA_CATEGORIE = "extra_categorie";
    final static String EXTRA_CATEGORIE_NAME = "extra_categorie_name";
    final static String EXTRA_MUSIQUE_CRITERIA = "extra_music_criteria";
    final static String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";
    private final String MUSIC_EXTRA = "music_extra";

    String criteria;
    String id_categorie_str;
    String nomcategorie;
    String id_utilisateur;

    AllMusiqueAdapter musicAdapter;
    AllMusiqueGridAdapter feedGridAdapter;
    List<Audio_Artiste> audioArtistes;

    RecyclerView recyclerViewMusique;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration itemDecor;
    ProgressBar progress;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    List<String> head;
    List<String> categorieChild;
    List<String> sortByDateChild;

    HashMap<String, List<String>> hashMap;
    Toolbar toolbar;
    Query query;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private ImageView imglist;
    private ImageView imggrid;
    private ImageView imgfilter;
    private ImageView imgbg;
    private String presentation = "list";
    private MyTextView_Ubuntu_Bold txtMusiqueCount;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference audioTendanceRef = db.collection("AudioTendance");
    private DocumentSnapshot lastResult;
    private ConstraintLayout empty_const;
    private AVLoadingIndicatorView avl;
    private Button btnviewall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_music);

        toolbar = findViewById(R.id.toolbar);


        if(getIntent().hasExtra(EXTRA_CATEGORIE)){

            id_categorie_str =  getIntent().getStringExtra(EXTRA_CATEGORIE);

            if(getIntent().hasExtra(EXTRA_CATEGORIE_NAME)){

                nomcategorie =  getIntent().getStringExtra(EXTRA_CATEGORIE_NAME);

            }


        }else if(getIntent().hasExtra(EXTRA_ID_UTILISATEUR)){

            id_utilisateur = getIntent().getStringExtra(EXTRA_ID_UTILISATEUR);

        }else if(getIntent().hasExtra(EXTRA_MUSIQUE_CRITERIA)){

            criteria = getIntent().getStringExtra(EXTRA_MUSIQUE_CRITERIA);

        }

        FirebaseFirestore.setLoggingEnabled(true);

        initUI();
        if (nomcategorie != null){

            initBackground(nomcategorie);
        }else{

            Glide.with(this)
                    .load(R.drawable.bg3)
                    .into(imgbg);
        }
        audioArtistes = new ArrayList<>();
        if(null != this.id_categorie_str){

            loadMusicsByCategorie(this.id_categorie_str);
        }

        initExpList();

        imgfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

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

                        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
                                new SpannedGridLayoutManager.GridSpanLookup() {
                                    @Override
                                    public SpannedGridLayoutManager.SpanInfo getSpanInfo(int position) {
                                        // Conditions for 2x2 items
                                        if (position % 6 == 0 || position % 6 == 4) {
                                            return new SpannedGridLayoutManager.SpanInfo(2, 2);
                                        } else {
                                            return new SpannedGridLayoutManager.SpanInfo(1, 1);
                                        }
                                    }
                                },
                                2, // number of columns
                                1f // how big is default item
                        );
                        layoutManager = new GridLayoutManager(AllMusicActivity.this, AllMusicActivity.this.getResources().getInteger(R.integer.shr_column_count));
                        recyclerViewMusique.removeItemDecoration(itemDecor);
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
                        layoutManager = new LinearLayoutManager(AllMusicActivity.this, RecyclerView.VERTICAL, false);
                        recyclerViewMusique.setLayoutManager(layoutManager);
                        recyclerViewMusique.addItemDecoration(itemDecor);
                    }

                }
            }


        });


        if(presentation.equals("list")){
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.colorAccent));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.placeholder_bg));

            endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, final RecyclerView view) {

                    if(id_categorie_str != null){
                        loadMusicsByCategorie(id_categorie_str);
                    }

                }
            };


        }else{
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.placeholder_bg));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.colorAccent));

            endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, final RecyclerView view) {

                    if(id_categorie_str != null){
                        loadMusicsByCategorie(id_categorie_str);
                    }
                }
            };
        }
    }

    private void initUI(){

        txtMusiqueCount = findViewById(R.id.txtallmusiques);

        avl = findViewById(R.id.avi);
        empty_const = findViewById(R.id.emptyconst);

        imgfilter  = findViewById(R.id.imgfilter);
        recyclerViewMusique = findViewById(R.id.recyr_all_musique);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        expandableListView = findViewById(R.id.explist_singlechoice);
        imglist = findViewById(R.id.imglist);
        imggrid = findViewById(R.id.imggrid);
        imgbg = findViewById(R.id.imgbg);
        progress = findViewById(R.id.progress_bar);
        txtMusiqueCount.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        btnviewall = findViewById(R.id.btnviewall);

    }

    private void initList() {

        if (query == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        musicAdapter = new AllMusiqueAdapter(AllMusicActivity.this, query, new AllMusiqueAdapter.AudioClickHandler() {
            @Override
            public void OnClick(Audio_Artiste produit) {


                Intent intent2 =  new Intent(AllMusicActivity.this, NowPlayingActivity.class);
                intent2.putExtra(MUSIC_EXTRA, produit);
                startActivity(intent2);
            }
        }){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

                Log.i("TAG", " RecyclerView");

                if (getItemCount() == 0) {
                    Log.i("TAG", "empty RecyclerView");

                    avl.setVisibility(View.GONE);
                    empty_const.setVisibility(View.VISIBLE);
                    recyclerViewMusique.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    btnviewall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            query = audioTendanceRef
                                    .orderBy("date_upload", Query.Direction.DESCENDING)
                                    .limit(100);
                            musicAdapter.setQuery(query);

                        }
                    });

                } else {

                    Log.i("TAG", " RecyclerView");

                    txtMusiqueCount.setText(getItemCount()+" "+getString(R.string.music_found));
                    txtMusiqueCount.setVisibility(View.VISIBLE);
                    empty_const.setVisibility(View.GONE);
                    recyclerViewMusique.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
//                Snackbar.make(view.findViewById(R.id.container),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
                Log.i("TAG", " error RecyclerView \n"+e.getMessage());
            }
        };


        feedGridAdapter = new AllMusiqueGridAdapter(AllMusicActivity.this, query, new AllMusiqueGridAdapter.AudioClickHandler() {
            @Override
            public void OnClick(Audio_Artiste produit) {

                Intent intent2 =  new Intent(AllMusicActivity.this, NowPlayingActivity.class);
                intent2.putExtra(MUSIC_EXTRA, produit);
                startActivity(intent2);
            }
        }){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

                Log.i("TAG", " RecyclerView");

                if (getItemCount() == 0) {
                    Log.i("TAG", "empty RecyclerView");
                    avl.setVisibility(View.GONE);
                    empty_const.setVisibility(View.VISIBLE);
                    recyclerViewMusique.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    btnviewall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            query = audioTendanceRef
                                    .limit(30);
                            musicAdapter.setQuery(query);

                        }
                    });


                } else {

                    Log.i("TAG", " RecyclerView");

                    txtMusiqueCount.setText(getItemCount()+" "+getString(R.string.music_found));
                    txtMusiqueCount.setVisibility(View.VISIBLE);
                    recyclerViewMusique.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
//                Snackbar.make(view.findViewById(R.id.container),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
                Log.i("TAG", " error RecyclerView \n"+e.getMessage());
            }
        };

        layoutManager = new LinearLayoutManager(AllMusicActivity.this, RecyclerView.VERTICAL, false);
        recyclerViewMusique.setLayoutManager(layoutManager);
        itemDecor = new DividerItemDecoration(AllMusicActivity.this, DividerItemDecoration.VERTICAL);
        recyclerViewMusique.addItemDecoration(itemDecor);
        recyclerViewMusique.setAdapter(musicAdapter);
        recyclerViewMusique.setNestedScrollingEnabled(false);

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

                   final List<Categorie> categories = queryDocumentSnapshots.toObjects(Categorie.class);
                    for (Categorie categorie : categories){

                        categorieChild.add(categorie.getNom_categorie());
                    }

                    sortByDateChild = new ArrayList<>();
                    sortByDateChild.add(getString(R.string.croissant));
                    sortByDateChild.add(getString(R.string.decroissant));

                    hashMap = new HashMap<>();
                    hashMap.put(head.get(0), categorieChild);
                    hashMap.put(head.get(1), sortByDateChild);

                    expandableListAdapter = new ExpandableListAdapter(AllMusicActivity.this, head, hashMap);
                    expandableListView.setAdapter(expandableListAdapter);


                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                            if(groupPosition == 0){

                                  query = audioTendanceRef
                                        .whereEqualTo("id_Categorie", categories.get(childPosition).getId_categorie())
                                        .limit(50);
                                  musicAdapter.setQuery(query);
                                  feedGridAdapter.setQuery(query);
                                  initBackground(categories.get(childPosition).getNom_categorie());

                            }else if(groupPosition == 1){


                            }

                            drawerLayout.closeDrawer(GravityCompat.END);

                              return false;
                        }
                    });

                }
            }
        });




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void OnClick(Audio_Artiste produit) {

        Intent intent2 =  new Intent(this, NowPlayingActivity.class);
        intent2.putExtra(MUSIC_EXTRA, produit);
        startActivity(intent2);

    }

    protected void loadMusicsByCategorie(String id_categorie_str){

        progress.setVisibility(View.VISIBLE);

            if (lastResult == null) {

                query = audioTendanceRef
                        .whereEqualTo("id_Categorie", id_categorie_str)
                        .limit(50);

            }else{

                query = audioTendanceRef
                        .whereEqualTo("id_Categorie", id_categorie_str)
                        .startAfter(lastResult)
                        .limit(50);
            }

            initList();

    }

   protected void initBackground(String nomcategorie){

       if(nomcategorie.equals(this.getString(R.string.acoustic))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Faccoustic.jpg?alt=media&token=ed0e11dd-f39b-4e9b-abf3-f58ac5ca8cb0")
                   .into(imgbg);


       }else if(nomcategorie.equals(this.getString(R.string.blue))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fblues.jpg?alt=media&token=c69e50e3-2d43-410d-b685-d7d4d489c377")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.compas))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fkonpa.jpg?alt=media&token=d45129f6-dd75-4c46-86ce-178645142e4d")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.carnaval))){




           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fcarnival-476816_1280.jpg?alt=media&token=7166de46-5c27-480c-a84a-3ee864100bfc")
                   .into(imgbg);


       }
       else if(nomcategorie.equals(this.getString(R.string.kids))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fkids.jpg?alt=media&token=013561ed-ead0-4bf3-baf9-736e9b4773ee")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.danceedm))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fedm.jpg?alt=media&token=8bb64451-864e-44fd-b6b0-c7168b504672")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.classique))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fclassic.jpg?alt=media&token=6c4931ba-d791-488e-9828-6cb392bf0ce7")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.electro))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Felectro.jpg?alt=media&token=ba794171-63b9-4756-bd9b-970ff8dbf57f")
                   .into(imgbg);


       }else if(nomcategorie.equals(this.getString(R.string.musiquer))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fmusic_rel.jpg?alt=media&token=992cc0d3-af28-4fab-abde-a75d6980856c")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.hiphop))
               || nomcategorie.equals(this.getString(R.string.hiphopk))
               || nomcategorie.equals(this.getString(R.string.rap))){



           if(nomcategorie.equals(this.getString(R.string.rap))){

               Glide.with(this)
                       .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffolk.jpg?alt=media&token=ded94486-33bd-4334-b221-eb5659f836a4")
                       .into(imgbg);

           }else{

               Glide.with(this)
                       .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frap.jpg?alt=media&token=209310f1-dace-4098-aaef-e3387b979c23")
                       .into(imgbg);

           }

       }else if(nomcategorie.equals(this.getString(R.string.jazz))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fjazz.jpg?alt=media&token=856a9e55-b34a-4983-a28a-0cc846e056b0")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.salsa))
               || nomcategorie.equals(this.getString(R.string.zouk))
               || nomcategorie.equals(this.getString(R.string.latino))
               || nomcategorie.equals(this.getString(R.string.reggaeton))
       ){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Flatino.jpg?alt=media&token=18cdc2f6-c1eb-4a78-9383-da4c8d6bada6")
                   .into(imgbg);


       }else if(nomcategorie.equals(this.getString(R.string.metal))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frock.jpg?alt=media&token=cb461755-3a18-40a9-9bbc-f076b82aa7c4")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.pop))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fpop.jpg?alt=media&token=ab05055d-4196-4c74-9fbd-89b33520952e")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.rnb))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frnb.jpg?alt=media&token=ce946d4e-c829-4e56-b2e2-e7669632acb7")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.reggae))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frasta-1915004_1280.jpg?alt=media&token=fd6bf66e-83ad-404f-a465-c0a8d32cb0e7")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.rock))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Frock.jpg?alt=media&token=cb461755-3a18-40a9-9bbc-f076b82aa7c4")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.musiquea))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2FAFmusic.jpg?alt=media&token=d06dc09b-c625-4283-828e-18258ec0f323")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.soul))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffunk.jpg?alt=media&token=6585f5ef-4198-4622-a5f3-4522a66f552c")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.folk))
               || nomcategorie.equals(this.getString(R.string.voudou))
       ){



           if(nomcategorie.equals(this.getString(R.string.folk))){

               Glide.with(this)
                       .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Ffolk.jpg?alt=media&token=ded94486-33bd-4334-b221-eb5659f836a4")
                       .into(imgbg);

           }else{

               Glide.with(this)
                       .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fvoodoo-402035_1280.jpg?alt=media&token=7ce4d13c-ab1a-4419-bd2a-342b8efbc02d")
                       .into(imgbg);

           }

       }else if(nomcategorie.equals(this.getString(R.string.musiquelove))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Flove.jpg?alt=media&token=c540076a-fc4e-4ae2-b392-741d03d2faf7")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.mix))){



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fmix.jpg?alt=media&token=2efdc60c-8a89-44f2-8b6c-ff0a1c9213a1")
                   .into(imgbg);



       }else if(nomcategorie.equals(this.getString(R.string.retro))){




           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fretro.jpg?alt=media&token=a62c6367-9609-4364-ade8-b2cdd61040a5")
                   .into(imgbg);


       }else{



           Glide.with(this)
                   .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CategorieImages%2Fworld_music.jpg?alt=media&token=f55dbe63-5002-46cd-ae52-530f1e6dfa36")
                   .into(imgbg);



       }
   }


    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (musicAdapter != null && feedGridAdapter != null) {
            musicAdapter.startListening();
            feedGridAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (musicAdapter != null && feedGridAdapter != null) {
            musicAdapter.stopListening();
            feedGridAdapter.stopListening();
        }
    }
}
