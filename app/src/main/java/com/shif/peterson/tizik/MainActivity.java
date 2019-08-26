package com.shif.peterson.tizik;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.MenuAdapter;
import com.shif.peterson.tizik.adapter.PlaylistAdapter;
import com.shif.peterson.tizik.adapter.RecyclerDataAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.fragment.HomeFragment;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.Playlist;
import com.shif.peterson.tizik.model.SectionDataModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;

import static com.shif.peterson.tizik.DetailPlaylistActivity.EXTRA_PLAYLIST;
import static com.shif.peterson.tizik.ProfileActivity.ID_USER_EXTRA;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private static final String MUSIC_EXTRA = "music_extra";
    private final ArrayList<SectionDataModel> allMusic = new ArrayList<SectionDataModel>();


    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";

    private final String music_IS_PLAYING = "isPlaying";
    private String MUSIC_STATE_POSITION_KEY = "music_position";
    private String MUSIC_KEY = "music_key";
    private String MUSIC_SIMILAR_KEY = "music_similar";
    private String MUSIC_PLAYLIST_POSI_KEY = "music_playlist_posi";
    private String MUSIC_PLAYING_LINK = "music_playing_link";


    int ACTIVITY_REQUEST_CODE = 75536;

    SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageView imgmusic;
    private TextView txtnommusic;
    private TextView txtnommusicfull;
    private  TextView txtnomartiste;
    private TextView txtnomartistefulll;
    private ImageView imgblur;
    private MaterialIconView imgplay;
    private MaterialIconView imgprevious;
    private MaterialIconView imgnext;
    private MaterialIconView imgplayfull;
    private SeekBar seekbar;
    ConstraintLayout constraintLayout;

    Audio_Artiste audio_artiste;
    int mCurrentPosition;

    DataSource.Factory dateSourceFactory;
    ExtractorsFactory extractorsFactory;
    ExoPlayer exoPlayer;
    BandwidthMeter bandwidthMeter;

    private Handler mHandler;
    private Runnable mRunnable;
    boolean isPlaying;
    private final List<Categorie> allcategories = new ArrayList<>();
    private final List<Abonnement> allAbonnements = new ArrayList<>();
    private final List<Playlist> allPlaylist = new ArrayList<>();
    private final List<Audio_Artiste> allTop = new ArrayList<>();
    ImageView imgprofile;
    MyTextView_Ubuntu_Bold txtNom;
    MyTextView_Ubuntu_Regular txtusername;
    View headerView;
    DuoDrawerLayout drawer;
    DuoMenuView menuView;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private ArrayList<String> mTitles = new ArrayList<>();
    private androidx.appcompat.widget.Toolbar toolbbar;
    private MenuAdapter mMenuAdapter;
    private ArrayList<Object> mainObject = new ArrayList<>();
    private HomeFragment.NowPlayingHandler nowPlayingHandler;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView listProduitRecyclerview;
    private RecyclerDataAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbbar);

        FirebaseApp.initializeApp(this);

        initUI();
        initList();

        mAuth = FirebaseAuth.getInstance();
        mHandler = new Handler();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser == null){

                    Intent intent = new Intent(MainActivity.this, MainSignInActivity.class);
                    startActivity(intent);


                }else{


                }
            }
        };


//        imgprofile = (ImageView) headerView.findViewById(R.id.imageView);
//        txtNom = (MyTextView_Ubuntu_Bold)  headerView.findViewById(R.id.txtnom);
//        txtusername = (MyTextView_Ubuntu_Regular)  headerView.findViewById(R.id.txtusername);



//        handleMenu();
//        handleDrawer();
//
//
//        HomeFragment detailFragment = HomeFragment.newInstance();
//        goToFragment(detailFragment, false);
//        mMenuAdapter.setViewSelected(0, true);
//        setTitle(mTitles.get(0));


    }

    private void initUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scrollview)
                    .setBackgroundResource(R.drawable.bg_top_radius);
        }

        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.main_nav)));
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        listProduitRecyclerview = findViewById(R.id.listaudiorecycler);

        listProduitRecyclerview.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        //getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        //toolbbar.setTitle("title");

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        toolbbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initList() {

        Task taskfree = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        //task top
        Task taskTop = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .limit(9)
                .get();

       //

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

//        Task taskabonnement  =  FirebaseFirestore.getInstance()
//                .collection("Abonnement")
//                .whereEqualTo("id_fan", firebaseUser.getUid())
//                .limit(5)
//                .get();


        Task taskabonnement  =  FirebaseFirestore.getInstance()
                .collection("Abonnement")
                .whereEqualTo("id_fan", "pAOCKyv8dBgmryBX5qC7C25H2Zx1")
                .limit(5)
                .get();

       // , taskCategorie, taskfree,taskrecommended,tasknew

        final Task<List<QuerySnapshot>> allMainTask = Tasks.whenAllSuccess(taskPlaylist,taskTop, taskabonnement, tasknew, taskCategorie);
        allMainTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

                for(int i = 0; i < querySnapshots.size(); i++){
                    QuerySnapshot queryDocumentSnapshots =  querySnapshots.get(i);

                    if (i == 0){

                        if (!queryDocumentSnapshots.isEmpty()){

                            allPlaylist.addAll(queryDocumentSnapshots.toObjects(Playlist.class));
                            mainObject.add(allPlaylist);


                        }
                    }else if (i == 1){

                        if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null){

                            allTop.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                            mainObject.add(allTop);


                        }
                    } else if(i == 2){

                        if (!queryDocumentSnapshots.isEmpty()){


                            allAbonnements.addAll(queryDocumentSnapshots.toObjects(Abonnement.class));
                            mainObject.add(allAbonnements);

                        }
                    }else if(i == 4){

                        if (!queryDocumentSnapshots.isEmpty()){


                            allcategories.addAll(queryDocumentSnapshots.toObjects(Categorie.class));
                            mainObject.add(allcategories);

                        }
                    }

                    else{

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



                dataAdapter = new RecyclerDataAdapter(MainActivity.this, mainObject, new RecyclerDataAdapter.ProductHandler() {
                    @Override
                    public void onClick(Audio_Artiste audio) {
                        Intent intent2 =  new Intent(MainActivity.this, NowPlayingActivity.class);
                        intent2.putExtra(MUSIC_EXTRA, audio);
                        startActivity(intent2);

                    }
                }, new PlaylistAdapter.PlaylistHandler() {
                    @Override
                    public void onPlaylistClick(Playlist position) {

                        Intent intent2 =  new Intent(MainActivity.this, DetailPlaylistActivity.class);
                        intent2.putExtra(EXTRA_PLAYLIST, position);
                        startActivity(intent2);

                    }

                    @Override
                    public void onPlayClick(Playlist position) {

                    }
                });


                listProduitRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL, false));
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



//    private void handleDrawer() {
//        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
//                drawer,
//                toolbbar,
//                R.string.navigation_drawer_open,
//                R.string.navigation_drawer_close);
//
//        drawer.setDrawerListener(duoDrawerToggle);
//        duoDrawerToggle.syncState();
//
//        headerView = ((DuoMenuView) drawer.getMenuView()).getHeaderView();
//
//        imgprofile = (ImageView) headerView.findViewById(R.id.imageView);
//        txtNom = (MyTextView_Ubuntu_Bold)  headerView.findViewById(R.id.txtnom);
//        txtusername = (MyTextView_Ubuntu_Regular)  headerView.findViewById(R.id.txtusername);
//
//                firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser != null){
//
//            if(firebaseUser.getPhotoUrl() != null){
//
//                RequestOptions glideCircle = new RequestOptions()
//                        .centerCrop()
//                        .error(R.drawable.headset)
//                        .transform( new CircleCrop());
//
//                Glide.with(this)
//                        .load(firebaseUser.getPhotoUrl())
//                        .apply(glideCircle)
//                        .into(imgprofile);
//            }
//
//
//
//            txtNom.setText(firebaseUser.getDisplayName());
//            if(firebaseUser.getEmail() != null){
//
//                txtusername.setText(firebaseUser.getEmail());
//
//            }
//        }
//
//    }
//
//    private void handleMenu() {
//
//        mMenuAdapter = new MenuAdapter(mTitles);
//        menuView.setOnMenuClickListener(this);
//        menuView.setAdapter(mMenuAdapter);
//    }
//
//    private void goToFragment(Fragment fragment, boolean addToBackStack) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        if (addToBackStack) {
//            transaction.addToBackStack(null);
//        }
//
//        transaction.replace(R.id.container, fragment).commit();
//    }
//
//    @Override
//    public void onBackPressed() {
//         drawer = (DuoDrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }



//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//
//
//        } else if (id == R.id.nav_profile) {
//
//
//
//        } else if (id == R.id.nav_genre) {
//
//        } else if (id == R.id.nav_favori) {
//
//            if (firebaseUser != null){
//
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                //startActivity(intent);
//
//            }else{
//
//                newFragment.show(ft, "SignIn");
//            }
//
//        } else if (id == R.id.nav_playlist) {
//
//            if (firebaseUser != null){
//
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
//                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                //startActivity(intent);
//
//            }else{
//
//
//                newFragment.show(ft, "SignIn");
//            }
//
//        }else if (id == R.id.nav_upload) {
//
//
//            if (firebaseUser != null){
//
//                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
//                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
//                startActivity(intent);
//
//            }else{
//
//
//              //  newFragment.onActivityResult(0,0, null);
//                newFragment.show(ft, "SignIn");
//            }
//
//        }else if (id == R.id.nav_notification) {
//
//
//
//            if (firebaseUser != null){
//
//                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                //startActivity(intent);
//
//            }else{
//
//                newFragment.show(ft, "SignIn");
//            }
//
//        }else if (id == R.id.nav_reglages) {
//
//        }else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }else if (id == R.id.nav_logout){
//
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        newFragment.onActivityResult(requestCode, resultCode, data);


//        if(requestCode == ACTIVITY_REQUEST_CODE){
//            if(resultCode == RESULT_OK){
//
//                Log.d("TAG", " code "+requestCode);
//                Log.d("TAG", " code "+requestCode);
//                Log.d("TAG", "data "+data.toString());
////                if(data.hasExtra(MUSIC_EXTRA)){
////
////
////
////
//////                    } &&
//////                            (slidingUpPanelLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
//////                        mLayout.setPanelState(PanelState.COLLAPSED);
//////                    } else {
//////                        super.onBackPressed();
//////                    }
//////
////
////
////                    audio_artiste = data.getParcelableExtra(MUSIC_EXTRA);
////                    mCurrentPosition = getIntent().getIntExtra(MUSIC_STATE_POSITION_KEY, 0);
////
////
////                    txtnommusicfull.setText(audio_artiste.getTitre_musique());
////                    txtnommusic.setText(audio_artiste.getTitre_musique());
////                    txtnomartiste.setText(audio_artiste.getNom_chanteur());
////                    txtnomartistefulll.setText(audio_artiste.getNom_chanteur());
////
////                    RequestOptions glideOptions = new RequestOptions()
////                            .centerCrop()
////                            .transform( new BlurTransformation(50))
////                            .error(R.color.cardview_dark_background)
////                            .placeholder(R.color.cardview_dark_background);
////
////                    RequestOptions glideOptions2 = new RequestOptions()
////                            .centerCrop()
////                            .error(R.color.cardview_dark_background)
////                            .placeholder(R.color.cardview_dark_background);
////
////                    Glide.with(this)
////                            .load(audio_artiste.getUrl_poster())
////                            .apply(glideOptions)
////                            .transition(withCrossFade())
////                            .into(imgblur);
////
////
////                    Glide.with(this)
////                            .load(audio_artiste.getUrl_poster())
////                            .apply(glideOptions2)
////                            .transition(withCrossFade())
////                            .into(imgmusic);
////
////                    if (slidingUpPanelLayout != null) {
////
////                        slidingUpPanelLayout.setPanelHeight(100);
////                        if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
////
////                            imgplay.setVisibility(View.GONE);
////                            txtnommusic.setVisibility(View.GONE);
////                            txtnomartiste.setVisibility(View.GONE);
////
////                        }else{
////
////                            imgplay.setVisibility(View.VISIBLE);
////                            txtnommusic.setVisibility(View.VISIBLE);
////                            txtnomartiste.setVisibility(View.VISIBLE);
////                        }
////
////
////                        initExoPlayer(audio_artiste.getUrl_musique(), mCurrentPosition);
////                        // exoPlayer.seekTo(mCurrentPosition);
////                    }
////
////
////
////
////                }
//
//            }
//
//
//        }

    }

//    private void initExoPlayer(String musicLink,  int position) {
//
//        if(exoPlayer == null){
//
//            bandwidthMeter = new DefaultBandwidthMeter();
//            extractorsFactory = new DefaultExtractorsFactory();
//            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
//            dateSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()), (TransferListener) bandwidthMeter);
//            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio_artiste.getUrl_musique()), dateSourceFactory, extractorsFactory, new Handler(), null);
//
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.seekTo(mCurrentPosition);
//            exoPlayer.setPlayWhenReady(true);
//
//            exoPlayer.addListener(new ExoPlayer.EventListener() {
//
//                @Override
//                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//                }
//
//                @Override
//                public void onLoadingChanged(boolean isLoading) {
//
//                }
//
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//                    if (playbackState == ExoPlayer.STATE_BUFFERING) {
//
//
//                    } else {
//
//                        //fab.playAnimation();
//                        //animator.start();
//                    }
//
//                    if (playbackState == ExoPlayer.STATE_READY) {
//
//                        imgplay.setImageResource(R.drawable.pause_icon);
//                        isPlaying = true;
//                        //exoplayerIsPlaying = true;
//                        //fab.playAnimation();
//
//                        //linearLayout.setVisibility(View.GONE);
//                        seekbar.setMax((int) (exoPlayer.getDuration() / 1000));
//
//                    } else {
//
//                       // exoplayerIsPlaying = false;
//                       // fab.playAnimation();
//
//                    }
//
//                    if (playbackState == ExoPlayer.STATE_ENDED) {
//
//                       // exoplayerIsPlaying = false;
//                        if (mHandler != null) {
//                            mHandler.removeCallbacks(mRunnable);
//                        }
//                        //fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
//                        //fab.playAnimation();
//
////                        if(listSimilarAudio.size() > playlistposi){
////
////                            currentplayinglink = listSimilarAudio.get(playlistposi).getUrl_musique();
////
////                            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentplayinglink), dateSourceFactory, extractorsFactory, new Handler(), null);
////                            exoPlayer.prepare(mediaSource);
////                            exoPlayer.setPlayWhenReady(true);
////
////                            seekBar.setProgress(0);
////                            toolbarProgress.setProgress(0);
////                            playlistposi++;
////
////
////                        }
//
//
//                    }
//
//                }
//
//                @Override
//                public void onPlayerError(ExoPlaybackException error) {
//                    //Toast.makeText(NowPlayingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
////                    Snackbar.make(R.id.container, error.getMessage(), Snackbar.LENGTH_LONG)
////                            .setAction("Retry", null).show();
//
//                }
//
//
//            });
//
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (exoPlayer != null) {
//                      int  position = (int) exoPlayer.getCurrentPosition() / 1000; // In milliseconds
//                        seekbar.setProgress(position);
//
//                    }
//                    mHandler.postDelayed(mRunnable, 1000);
//                }
//            };
//            mHandler.postDelayed(mRunnable, 1000);
//            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                @Override
//                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                    if (fromUser && exoPlayer != null) {
////
//                        exoPlayer.seekTo((long) progress * 1000);
//
//
//                    }
////
//                }
//
//                @Override
//                public void onStartTrackingTouch(SeekBar seekBar) {
//
//                }
//
//                @Override
//                public void onStopTrackingTouch(SeekBar seekBar) {
//
//                }
//            });
//
//        }
//
//    }
//
//    @Override
//    public void nowPlayingListener(Audio_Artiste audio_artiste) {
//
//        Intent intent = new Intent(this, AudioPlayerService.class);
//        intent.putExtra(MUSIC_EXTRA, audio_artiste);
//        Util.startForegroundService(this, intent);
//
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            if (firebaseUser != null){
//
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(ID_USER_EXTRA, firebaseUser.getUid());
                    startActivity(intent);
//
                }else{
//
                 startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
               }


        } else if (id == R.id.nav_genre) {

            if (firebaseUser != null){
////
                    Intent intent = new Intent(MainActivity.this, GenresActivity.class);
                    startActivity(intent);
//
                }else{

                    startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
                }

        } else if (id == R.id.nav_upload) {

            if (firebaseUser != null){

                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                startActivity(intent);

            }else{

              startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
            }

        }else if (id == R.id.nav_favori) {

            if (firebaseUser != null){
////
                    Intent intent = new Intent(MainActivity.this, FavoriActivity.class);
                    intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                    startActivity(intent);

                }else{

                startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
                }
//

        } else if (id == R.id.nav_playlist) {

        }else if (id == R.id.nav_notification) {

        }else if (id == R.id.nav_reglages) {

        }else if (id == R.id.nav_logout) {

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


//    @Override
//    public void onFooterClicked() {
//        if(mAuth != null) {
//
//            mAuth.signOut();
//            Intent intent = new Intent(MainActivity.this, MainSignInActivity.class);
//            startActivity(intent);
//            finish();
//
//        }
//
//    }
//
//    @Override
//    public void onHeaderClicked() {
//
//        if (firebaseUser != null){
//
//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                intent.putExtra("ID_USER_EXTRA", firebaseUser.getUid());
//                startActivity(intent);
//
//            }else{
//
//            Intent intent = new Intent(MainActivity.this, MainSignInActivity.class);
//            startActivity(intent);
//            }
//    }
//
//    @Override
//    public void onOptionClicked(int position, Object objectClicked) {
//        // Set the toolbar title
//        setTitle(mTitles.get(position));
//
//        // Set the right options selected
//        mMenuAdapter.setViewSelected(position, true);
//
//
//        switch (position) {
//
//            case 0:
//
//                goToFragment(new HomeFragment(), false);
//
//                break;
//            case 1:
//
//                if (firebaseUser != null){
//
//                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                    intent.putExtra(ID_USER_EXTRA, firebaseUser.getUid());
//                    startActivity(intent);
//
//                }else{
//
//                 //   newFragment.show(ft, "SignIn");
//                }
//                break;
//            case 2:
////Genres
//                if (firebaseUser != null){
////
//                    Intent intent = new Intent(MainActivity.this, GenresActivity.class);
//                    startActivity(intent);
//
//                }else{
//
////                    newFragment.show(ft, "SignIn");
//                }
//
//                break;
//            case 3:
////Favori
//                if (firebaseUser != null){
////
//                    Intent intent = new Intent(MainActivity.this, FavoriActivity.class);
//                    intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
//                    startActivity(intent);
//
//                }else{
//
//                    //newFragment.show(ft, "SignIn");
//                }
//
//                break;
//            case 4:
////Playlist
//                goToFragment(new HomeFragment(), false);
//
//                break;
//            case 5:
//
//                if (firebaseUser != null){
////
//                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
//                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
//                startActivity(intent);
//
//            }else{
//
//              //  newFragment.show(ft, "SignIn");
//            }
//
//                break;
//            default:
//                goToFragment(new HomeFragment(), false);
//                break;
//        }
//
//        // Close the drawer
//        drawer.closeDrawer();
//    }
}
