package com.shif.peterson.tizik;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.shif.peterson.tizik.fragment.HomeFragment;
import com.shif.peterson.tizik.fragment.MainSignInDialogFragment;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.tankery.lib.circularseekbar.CircularSeekBar;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";

    private final String music_IS_PLAYING = "isPlaying";
    private String MUSIC_STATE_POSITION_KEY = "music_position";
    private String MUSIC_KEY = "music_key";
    private String MUSIC_SIMILAR_KEY = "music_similar";
    private String MUSIC_PLAYLIST_POSI_KEY = "music_playlist_posi";
    private String MUSIC_PLAYING_LINK = "music_playing_link";


    int ACTIVITY_REQUEST_CODE = 75536;
    private final String MUSIC_EXTRA = "music_extra";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment detailFragment = HomeFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, detailFragment, "home container")
                .commit();

        mAuth = FirebaseAuth.getInstance();
        mHandler = new Handler();

        constraintLayout = (ConstraintLayout) findViewById(R.id.bottomControl);
        slidingUpPanelLayout =(SlidingUpPanelLayout) findViewById(R.id.slidingpanel);
        imgmusic = (ImageView) findViewById(R.id.imgmusic);
        imgblur = (ImageView) findViewById(R.id.imgblur);
        txtnomartiste = (TextView) findViewById(R.id.txtnomartiste2);
        txtnommusic = (TextView) findViewById(R.id.txttitremusic);
        txtnomartistefulll = (TextView) findViewById(R.id.txtnomartiste);
        txtnommusicfull = (TextView) findViewById(R.id.txtnommizik);

        imgprevious = (MaterialIconView) findViewById(R.id.imgprevious);
        imgplayfull = (MaterialIconView) findViewById(R.id.play);
        imgnext = (MaterialIconView) findViewById(R.id.imgnext);
        imgplay = (MaterialIconView) findViewById(R.id.imgplay);

        seekbar = (SeekBar) findViewById(R.id.seek_bar);


        if(getIntent().hasExtra( MUSIC_EXTRA)){

            audio_artiste = getIntent().getParcelableExtra(MUSIC_EXTRA);
            mCurrentPosition = getIntent().getIntExtra(MUSIC_STATE_POSITION_KEY, 0);

            txtnommusicfull.setText(audio_artiste.getTitre_musique());
            txtnommusic.setText(audio_artiste.getTitre_musique());
            txtnomartiste.setText(audio_artiste.getNom_chanteur());
            txtnomartistefulll.setText(audio_artiste.getNom_chanteur());

            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .transform( new BlurTransformation(50))
                    .error(R.color.cardview_dark_background)
                    .placeholder(R.color.cardview_dark_background);

            RequestOptions glideOptions2 = new RequestOptions()
                    .centerCrop()
                    .error(R.color.cardview_dark_background)
                    .placeholder(R.color.cardview_dark_background);

            Glide.with(this)
                    .load(audio_artiste.getUrl_poster())
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(imgblur);


            Glide.with(this)
                    .load(audio_artiste.getUrl_poster())
                    .apply(glideOptions2)
                    .transition(withCrossFade())
                    .into(imgmusic);

            slidingUpPanelLayout.setPanelHeight(100);

            if (slidingUpPanelLayout != null) {

                slidingUpPanelLayout.setPanelHeight(100);
                if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){

                    imgplay.setVisibility(View.GONE);
                    txtnommusic.setVisibility(View.GONE);
                    txtnomartiste.setVisibility(View.GONE);

                }else{

                    imgplay.setVisibility(View.VISIBLE);
                    txtnommusic.setVisibility(View.VISIBLE);
                    txtnomartiste.setVisibility(View.VISIBLE);
                }

                initExoPlayer(audio_artiste.getUrl_musique(), mCurrentPosition);
                exoPlayer.seekTo(mCurrentPosition);

            }


        }

        if(exoPlayer != null){

            imgplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( isPlaying ){

                        imgplay.setImageResource(R.drawable.play_icon);
                        imgplay.setColorResource(R.color.ms_black);
                    }else{


                        imgplay.setImageResource(R.drawable.pause_icon);
                        imgplay.setColorResource(R.color.ms_black);
                    }
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        } else if (id == R.id.nav_genre) {

        } else if (id == R.id.nav_favori) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        } else if (id == R.id.nav_playlist) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_upload) {




            if (mAuth.getInstance().getCurrentUser() != null){

                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_notification) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_reglages) {

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG", " code "+requestCode);

        if(requestCode == ACTIVITY_REQUEST_CODE){

            Log.d("TAG", " code "+requestCode);
            Log.d("TAG", "data "+data.toString());
            if(data.hasExtra(MUSIC_EXTRA)){

                if(resultCode == RESULT_OK){

                    Log.d("TAG", " code "+requestCode);
                }



//                    } &&
//                            (slidingUpPanelLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
//                        mLayout.setPanelState(PanelState.COLLAPSED);
//                    } else {
//                        super.onBackPressed();
//                    }
//


                 audio_artiste = data.getParcelableExtra(MUSIC_EXTRA);
                mCurrentPosition = getIntent().getIntExtra(MUSIC_STATE_POSITION_KEY, 0);


                txtnommusicfull.setText(audio_artiste.getTitre_musique());
                txtnommusic.setText(audio_artiste.getTitre_musique());
                txtnomartiste.setText(audio_artiste.getNom_chanteur());
                txtnomartistefulll.setText(audio_artiste.getNom_chanteur());

                RequestOptions glideOptions = new RequestOptions()
                        .centerCrop()
                        .transform( new BlurTransformation(50))
                        .error(R.color.cardview_dark_background)
                        .placeholder(R.color.cardview_dark_background);

                RequestOptions glideOptions2 = new RequestOptions()
                        .centerCrop()
                        .error(R.color.cardview_dark_background)
                        .placeholder(R.color.cardview_dark_background);

                Glide.with(this)
                        .load(audio_artiste.getUrl_poster())
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(imgblur);


                Glide.with(this)
                        .load(audio_artiste.getUrl_poster())
                        .apply(glideOptions2)
                        .transition(withCrossFade())
                        .into(imgmusic);

                if (slidingUpPanelLayout != null) {

                    slidingUpPanelLayout.setPanelHeight(100);
                    if(slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){

                        imgplay.setVisibility(View.GONE);
                        txtnommusic.setVisibility(View.GONE);
                        txtnomartiste.setVisibility(View.GONE);

                    }else{

                        imgplay.setVisibility(View.VISIBLE);
                        txtnommusic.setVisibility(View.VISIBLE);
                        txtnomartiste.setVisibility(View.VISIBLE);
                    }


                    initExoPlayer(audio_artiste.getUrl_musique(), mCurrentPosition);
                   // exoPlayer.seekTo(mCurrentPosition);
                }




            }





        }

    }



    private void initExoPlayer(String musicLink,  int position) {

        if(exoPlayer == null){


            bandwidthMeter = new DefaultBandwidthMeter();
            extractorsFactory = new DefaultExtractorsFactory();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            dateSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()), (TransferListener) bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio_artiste.getUrl_musique()), dateSourceFactory, extractorsFactory, new Handler(), null);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
            exoPlayer.prepare(mediaSource);
            exoPlayer.seekTo(mCurrentPosition);
            exoPlayer.setPlayWhenReady(true);

            exoPlayer.addListener(new ExoPlayer.EventListener() {

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == ExoPlayer.STATE_BUFFERING) {


                    } else {


                        //fab.playAnimation();
                        //animator.start();


                    }

                    if (playbackState == ExoPlayer.STATE_READY) {

                        imgplay.setImageResource(R.drawable.pause_icon);
                        isPlaying = true;
                        //exoplayerIsPlaying = true;
                        //fab.playAnimation();

                        //linearLayout.setVisibility(View.GONE);
                        seekbar.setMax((int) (exoPlayer.getDuration() / 1000));

                    } else {

                       // exoplayerIsPlaying = false;
                       // fab.playAnimation();

                    }

                    if (playbackState == ExoPlayer.STATE_ENDED) {

                       // exoplayerIsPlaying = false;
                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }
                        //fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        //fab.playAnimation();

//                        if(listSimilarAudio.size() > playlistposi){
//
//                            currentplayinglink = listSimilarAudio.get(playlistposi).getUrl_musique();
//
//                            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentplayinglink), dateSourceFactory, extractorsFactory, new Handler(), null);
//                            exoPlayer.prepare(mediaSource);
//                            exoPlayer.setPlayWhenReady(true);
//
//                            seekBar.setProgress(0);
//                            toolbarProgress.setProgress(0);
//                            playlistposi++;
//
//
//                        }


                    }

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    //Toast.makeText(NowPlayingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    Snackbar.make(R.id.container, error.getMessage(), Snackbar.LENGTH_LONG)
//                            .setAction("Retry", null).show();

                }


            });

            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (exoPlayer != null) {
                      int  position = (int) exoPlayer.getCurrentPosition() / 1000; // In milliseconds
                        seekbar.setProgress(position);

                    }
                    mHandler.postDelayed(mRunnable, 1000);
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (fromUser && exoPlayer != null) {
//
                        exoPlayer.seekTo((long) progress * 1000);


                    }
//
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

    }

}
