package com.shif.peterson.tizik;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.Allocation;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
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
import com.shif.peterson.tizik.adapter.SimilarAdater;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.wang.avi.AVLoadingIndicatorView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;
import jp.wasabeef.glide.transformations.BitmapTransformation;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.tankery.lib.circularseekbar.CircularSeekBar;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class NowPlayingActivity extends AppCompatActivity implements
        SimilarAdater.RemoveFromListInterface {

    private final String MUSIC_EXTRA = "music_extra";

    Audio_Artiste audio_artiste_selected;

CollapsingToolbarLayout collapsingToolbarLayout;
    AVLoadingIndicatorView avLoadingIndicatorView;
    LinearLayout linearLayout;

    private ImageView imgblur;
    private ImageView imgcover;
    private MaterialIconView imgprevious;
    private MaterialIconView imgnext;



    private TextView txtsongName;
    private TextView txtalbum;
    private TextView txtgenre;
    private TextView txtdesc;
    private TextView txtavis;
    private TextView txtprix;
    private Button btncomment;
    private Button btnfavori;
    private AppCompatRatingBar ratingBar;
    private Toolbar toolbar;

    RecyclerView recyclersimilar;
    SimilarAdater similarAdater;
    List<Audio_Artiste> listSimilarAudio;

    FloatingMusicActionButton fab;


    //Exoplayer
    ExoPlayer exoPlayer;
    BandwidthMeter bandwidthMeter;
    boolean exoplayerIsPlaying = false;

    Animator animator;


    private Handler mHandler;
    private Runnable mRunnable;

    CircularSeekBar seekBar;

    Menu collapsingMenu;
    AppBarLayout appBarLayout;
    private boolean appBarExpanded = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().hasExtra( MUSIC_EXTRA)){

            audio_artiste_selected = getIntent().getParcelableExtra(MUSIC_EXTRA);
        }

        mHandler = new Handler();
        

        intitUI();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(exoPlayer != null ){

                   if (exoplayerIsPlaying){

                      pausePlayer();
                      fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                      fab.playAnimation();
                      exoplayerIsPlaying = false;
                      animator.end();
                   }else{

                       resumePlayer();
                       fab.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                       fab.playAnimation();

                       exoplayerIsPlaying = true;
                       animator.start();

                   }
               }
            }
        });
    }

    private void intitUI() {

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        avLoadingIndicatorView = findViewById(R.id.avi);
        linearLayout = findViewById(R.id.linearprogress);


        imgblur = findViewById(R.id.image_blur);
        imgcover = findViewById(R.id.circularimageview);
        imgprevious = findViewById(R.id.previous);
        imgnext = findViewById(R.id.next);
        seekBar = findViewById(R.id.seek_bar);

        txtsongName = findViewById(R.id.txtnom);
        txtalbum = findViewById(R.id.txtalbum);
        txtgenre = findViewById(R.id.txtcategorie);
        ratingBar = findViewById(R.id.ratingbar);
        txtavis = findViewById(R.id.txtavis);
        txtprix = findViewById(R.id.txtprix);
        txtdesc = findViewById(R.id.expandableTextView);

        btnfavori = findViewById(R.id.btnfavori);
        btncomment = findViewById(R.id.btncomment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclersimilar = findViewById(R.id.recyclersimilar);
        fab = findViewById(R.id.fab);

        if( null != audio_artiste_selected.getUrl_poster()){

            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .transform( new BlurTransformation(50))
                    .error(R.color.cardview_dark_background)
                    .placeholder(R.color.cardview_dark_background);


            RequestOptions glideCircle = new RequestOptions()
                    .centerCrop()
                    .transform( new CircleCrop())
                    .error(R.color.cardview_dark_background)
                    .placeholder(R.drawable.ic_placeholder_headset);

            Glide.with(this)
                    .load(audio_artiste_selected.getUrl_poster())
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(imgblur);

            Glide.with(this)
                    .load(audio_artiste_selected.getUrl_poster())
                    .apply(glideCircle)
                    .transition(withCrossFade())
                    .into(imgcover);



        }

        collapsingToolbarLayout.setTitle(audio_artiste_selected.getTitre_musique());
        txtsongName.setText(audio_artiste_selected.getNom_chanteur());
        txtalbum.setText(audio_artiste_selected.getId_album());
        if (null != audio_artiste_selected.getDescription_musique()){

            txtdesc.setText(audio_artiste_selected.getDescription_musique());
        }else{

            txtdesc.setVisibility(View.GONE);
        }

        animator = AnimatorInflater.loadAnimator(this, R.animator.clockwise_rotation);
        animator.setTarget(imgcover);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });

        iniLitsAudio();
        initListSimilar();
        initExoPlayer();


    }

    private void iniLitsAudio() {

        listSimilarAudio = new ArrayList<>();
        //todo firebase selection
        for (int j = 0; j <= 10; j++) {

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
            listSimilarAudio.add(produit);

        }


    }

    private void initListSimilar() {

        similarAdater = new SimilarAdater(NowPlayingActivity.this, listSimilarAudio, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NowPlayingActivity.this, LinearLayoutManager.VERTICAL, false);
       linearLayoutManager.setAutoMeasureEnabled(true);
        recyclersimilar.setLayoutManager(linearLayoutManager);
        recyclersimilar.setAdapter(similarAdater);
        recyclersimilar.setNestedScrollingEnabled(false);
        recyclersimilar.setVisibility(View.VISIBLE);
    }

    private void initExoPlayer() {

        //DefaultBandwidthMeter class implies estimated available network bandwidth based on measured download speed.
        bandwidthMeter = new DefaultBandwidthMeter();

        //An ExtractorFactory is there for providing an array of the extractor for the media formats.
        final ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


        //AdaptiveTrackSelection.Factory is a bandwidth based adaptive TrackSelection. It gives the highest quality state of a buffer according to your network condition.
        TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DataSource.Factory dateSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()), (TransferListener) bandwidthMeter);

        MediaSource[] mediaSources = new MediaSource[listSimilarAudio.size()];
        //MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio_artiste_selected.getUrl_musique()), dateSourceFactory, extractorsFactory, new Handler(), null);

        for (int i = 0; i < mediaSources.length; i++) {

            String songUri = listSimilarAudio.get(i).getUrl_musique();
            mediaSources[i] = new ExtractorMediaSource(Uri.parse(songUri), dateSourceFactory, extractorsFactory, new Handler(), null);


        }


        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(trackSelectionFactory));
        exoPlayer.prepare(mediaSource);
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


                    fab.playAnimation();
                    animator.start();


                }

                if (playbackState == ExoPlayer.STATE_READY) {
                    exoplayerIsPlaying = true;
                    fab.playAnimation();

                    linearLayout.setVisibility(View.GONE);
                    seekBar.setMax(exoPlayer.getDuration() / 1000);

                } else {

                    exoplayerIsPlaying = false;
                    fab.playAnimation();

                }

                if (playbackState == ExoPlayer.STATE_ENDED) {

                    exoplayerIsPlaying = false;
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                    fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                    fab.playAnimation();
                    animator.end();

                }

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                //Toast.makeText(NowPlayingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar.make(linearLayout, error.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Retry", null).show();

            }


        });

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null) {
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000; // In milliseconds
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(mRunnable, 1000);
            }
        };
        mHandler.postDelayed(mRunnable, 1000);


        seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

                if (fromUser && exoPlayer != null) {

                    exoPlayer.seekTo((long) progress * 1000);


                }

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {


            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });


    }


    public void pausePlayer(){
        if(exoPlayer != null){
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    public void resumePlayer(){
        if(exoPlayer != null){
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.getPlaybackState();
        }
    }

    @Override
    public void OnRemoveFromList(int position) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (collapsingMenu != null
                && (!appBarExpanded || collapsingMenu.size() != 1)) {
            //collapsed
            collapsingMenu.add(R.string.partager)
                    .setIcon(R.drawable.ic_action_share)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            collapsingMenu.add(R.string.telecharger)
                    .setIcon(R.drawable.ic_action_download)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsingMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.now_playin_menu, menu);
        collapsingMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                if(null != audio_artiste_selected){

                    onShareItem(audio_artiste_selected.getTitre_musique());
                }

                return true;
            case R.id.action_download:


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onShareItem(String info) {
        // Get access to bitmap image from view
        ImageView ivImage = (ImageView) findViewById(R.id.circularimageview);
        String text = info+" "+getString(R.string.share_textt);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "TIZIK"));
        } else {

            // ...sharing failed, handle error
        }
    }


    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){

            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();

            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public Uri getBitmapFromDrawable(Bitmap bmp){

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            // wrap File object into a content provider. NOTE: authority here should match authority in manifest declaration
           // bmpUri = FileProvider.getUriForFile(BookDetailActivity.this, "com.codepath.fileprovider", file);  // use this version for API >= 24

            bmpUri = Uri.fromFile(file);
            // **Note:** For API < 24, you may use bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
