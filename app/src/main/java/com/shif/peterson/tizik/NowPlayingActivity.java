package com.shif.peterson.tizik;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.SimilarAdater;
import com.shif.peterson.tizik.fragment.CommentDialogFragment;
import com.shif.peterson.tizik.fragment.NowPlayingBottomSheet;
import com.shif.peterson.tizik.fragment.SignUpFragment;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie_Audio;
import com.shif.peterson.tizik.model.Commentaire_Audio;
import com.shif.peterson.tizik.model.Utilisateur;
import com.wang.avi.AVLoadingIndicatorView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.ByteArrayOutputStream;
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
import static com.shif.peterson.tizik.utilis.Utils.calculerMoyenne;

public class NowPlayingActivity extends AppCompatActivity implements
        SimilarAdater.SimiClickListener,
        CommentDialogFragment.OnMusicCommentListener
{

    private final String TAG = NowPlayingActivity.class.getSimpleName().toString();

    private static final String CHANNEL_ID = "media_playback_channel";

    private String MUSIC_STATE_POSITION_KEY = "music_position";
    private String MUSIC_KEY = "music_key";
    private String MUSIC_AVIS_KEY = "music_avis";
    private String MUSIC_AVIS_SIZE = "musoc_avis_size";
    private String MUSIC_SIMILAR_KEY = "music_similar";
    private String MUSIC_PLAYLIST_POSI_KEY = "music_playlist_posi";
    private String MUSIC_PLAYING_LINK = "music_playing_link";
    int ACTIVITY_REQUEST_CODE = 10000;
    //private

    private final String MUSIC_EXTRA = "music_extra";
    private final String music_IS_PLAYING = "isPlaying";

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

    private ProgressBar toolbarProgress;

    private FirebaseAuth mAuth;

    Bitmap bitmap = null;
    int playlistposi;
    DataSource.Factory dateSourceFactory;
     ExtractorsFactory extractorsFactory;
    DividerItemDecoration itemDecor;

    int mCurrentPosition;
    double moyenne;
    int numComment;
    Audio_Artiste audio_artiste_selected;
    String currentplayinglink;



    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;


    private static final String COLLECTION_NAME = "Commentaire_Audio";
    public static CollectionReference getCommentCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static final String COLLECTION_NAME_AUDIO = "Audio_Artiste";
    public static CollectionReference getAudioCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static final String COLLECTION_NAME_CATEGORIE = "Categorie_Audio";
    public static CollectionReference getCategorieAudioCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        intitUI();
        mHandler = new Handler();

        if(savedInstanceState != null){

            mCurrentPosition = savedInstanceState.getInt(MUSIC_STATE_POSITION_KEY);
            audio_artiste_selected = savedInstanceState.getParcelable(MUSIC_KEY);
            moyenne = savedInstanceState.getDouble(MUSIC_AVIS_KEY);
            numComment = savedInstanceState.getInt(MUSIC_AVIS_SIZE);
            listSimilarAudio = savedInstanceState.getParcelableArrayList(MUSIC_SIMILAR_KEY);
            playlistposi = savedInstanceState.getInt(MUSIC_PLAYLIST_POSI_KEY);

            currentplayinglink = savedInstanceState.getString(MUSIC_PLAYING_LINK);
            updateUI(audio_artiste_selected);
            initExoPlayer(currentplayinglink);

            if(null != listSimilarAudio ){

                initListSimilar();

            }else{

                iniLitsAudio();
                initListSimilar();
            }



        }else{

            playlistposi = 0;

            if(getIntent().hasExtra( MUSIC_EXTRA)){

                audio_artiste_selected = getIntent().getParcelableExtra(MUSIC_EXTRA);
            }

            currentplayinglink = audio_artiste_selected.getUrl_musique();

            updateUI(audio_artiste_selected);

            getCommentCollectionReference()
                    .whereEqualTo("id_audio", audio_artiste_selected.getId_musique())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (null != queryDocumentSnapshots && 0 < queryDocumentSnapshots.size()){

                        List<Double> notes = new ArrayList<>();
                        numComment = queryDocumentSnapshots.size();
                        txtavis.setText(String.valueOf(queryDocumentSnapshots.size()));


                        List<Commentaire_Audio> commentaire_audios = queryDocumentSnapshots.toObjects(Commentaire_Audio.class);
                        for (Commentaire_Audio commentaire_audio : commentaire_audios){

                            notes.add((double) commentaire_audio.getNote());

                        }
                        moyenne = calculerMoyenne(notes);
                        ratingBar.setRating((float) moyenne);

                    }else{

                        txtavis.setText("0");
                        ratingBar.setRating(0);
                    }

                }
            });

            initExoPlayer(currentplayinglink);
            iniLitsAudio();
            initListSimilar();

            if (null != exoPlayer){

                exoPlayer.seekTo(mCurrentPosition);

            }else{


            }



        }



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

        btncomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                final CommentDialogFragment fragment = CommentDialogFragment.newInstance(audio_artiste_selected.getId_musique(),  mAuth.getInstance().getCurrentUser().getUid());
                fragment.show(fragmentTransaction, "Comment dialog");
            }
        });

        if(exoPlayer != null){

            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listSimilarAudio.size() > playlistposi){

                        currentplayinglink = listSimilarAudio.get(playlistposi).getUrl_musique();

                        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentplayinglink), dateSourceFactory, extractorsFactory, new Handler(), null);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(true);

                        seekBar.setProgress(0);
                        toolbarProgress.setProgress(0);
                        playlistposi++;


                    }
                }
            });

            imgprevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if( playlistposi > 0){

                        currentplayinglink = listSimilarAudio.get((playlistposi-1)).getUrl_musique();

                        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentplayinglink), dateSourceFactory, extractorsFactory, new Handler(), null);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(true);

                        seekBar.setProgress(0);
                        toolbarProgress.setProgress(0);
                        playlistposi--;


                    }
                }
            });
        }

        initializeMediaSession();

    }

    private void intitUI() {

        toolbarProgress = findViewById(R.id.toolbarprogress);
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

        animator = AnimatorInflater.loadAnimator(this, R.animator.clockwise_rotation);
        animator.setTarget(imgcover);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;

                    toolbarProgress.setVisibility(View.VISIBLE);
                } else {
                    appBarExpanded = true;

                    toolbarProgress.setVisibility(View.GONE);
                }
            }
        });


    }


    private void updateUI(final Audio_Artiste audio_artiste_selected){

        linearLayout.setVisibility(View.GONE);

        if( null != audio_artiste_selected.getUrl_poster()){

            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .transform( new BlurTransformation(50))
                    .error(R.color.cardview_dark_background)
                    .placeholder(R.color.cardview_dark_background);


            RequestOptions glideCircle = new RequestOptions()
                    .centerCrop()
                    .transform( new CircleCrop())
                    .error(R.drawable.ic_placeholder_headset)
                    .placeholder(R.drawable.ic_placeholder_headset)
                    ;

            Glide.with(this)
                    .load(audio_artiste_selected.getUrl_poster())
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(imgblur);

            Glide.with(this)
                    .load(audio_artiste_selected.getUrl_poster())
                    .apply(glideCircle)
                    .transition(withCrossFade())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {


                            if (resource instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                                if(bitmapDrawable.getBitmap() != null) {
                                    bitmap =  bitmapDrawable.getBitmap();
                                }
                            }

                            if(resource.getIntrinsicWidth() <= 0 || resource.getIntrinsicHeight() <= 0) {
                                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
                            } else {
                                bitmap = Bitmap.createBitmap(resource.getIntrinsicWidth(), resource.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            }

                            Canvas canvas = new Canvas(bitmap);
                            resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                            resource.draw(canvas);


                            if (bitmap != null){

                                Palette palette = Palette.from(bitmap).generate();
                                Palette.Swatch vibrant = palette.getVibrantSwatch();
                                Palette.Swatch dark = palette.getDarkVibrantSwatch();

                                if(null != vibrant && null != dark){

                                    collapsingToolbarLayout.setContentScrimColor(vibrant.getTitleTextColor());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getWindow().setStatusBarColor(dark.getRgb());
                                    }else{
                                        collapsingToolbarLayout.setStatusBarScrimColor(dark.getRgb());

                                    }
                                    collapsingToolbarLayout.setBackgroundColor(vibrant.getRgb());

                                }
                            }
                            return false;
                        }
                    })
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


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                if(playlistposi > 0){

                    intent.putExtra(MUSIC_EXTRA, audio_artiste_selected);
                }else{

                    intent.putExtra(MUSIC_EXTRA, listSimilarAudio.get(playlistposi));
                }

                intent.putExtra(MUSIC_STATE_POSITION_KEY, mCurrentPosition);

                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    private void iniLitsAudio() {

        listSimilarAudio = new ArrayList<>();

//        getCategorieAudioCollectionReference().whereEqualTo("id_audio", audio_artiste_selected.getId_musique())
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                if(null != queryDocumentSnapshots && !queryDocumentSnapshots.isEmpty()){
//
//                    List<Categorie_Audio> categorie_audios = queryDocumentSnapshots.toObjects(Categorie_Audio.class);
//                   for (Categorie_Audio categorie_audio : categorie_audios){
//
//                       getAudioCollectionReference().whereEqualTo("id_musique", categorie_audio.getId_audio())
//                       .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                           @Override
//                           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                               if(null != queryDocumentSnapshots){
//
//                                   List<Audio_Artiste> list = queryDocumentSnapshots.toObjects(Audio_Artiste.class);
//                                   for (Audio_Artiste audio_artiste : list){
//
//                                       listSimilarAudio.add(audio_artiste);
//                                   }
//
//                               }
//                           }
//                       });
//                   }
//
//
//
//                }else{
//
//
//                }
//
//            }
//        });
    //    getAudioCollectionReference().whereEqualTo("", audio_artiste_selected.get)


//        //todo firebase selection
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
        itemDecor = new DividerItemDecoration(NowPlayingActivity.this, DividerItemDecoration.VERTICAL);
        recyclersimilar.addItemDecoration(itemDecor);
        recyclersimilar.setAdapter(similarAdater);
        recyclersimilar.setNestedScrollingEnabled(false);
        recyclersimilar.setVisibility(View.VISIBLE);
    }

    private void initExoPlayer(String musicLink) {

        if(exoPlayer == null){

            playlistposi = 0;
             bandwidthMeter = new DefaultBandwidthMeter();
            extractorsFactory = new DefaultExtractorsFactory();
            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            dateSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getPackageName()), (TransferListener) bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(audio_artiste_selected.getUrl_musique()), dateSourceFactory, extractorsFactory, new Handler(), null);

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

                    if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                                exoPlayer.getCurrentPosition(), 1f);

                        exoplayerIsPlaying = true;
                        fab.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_PAUSE);
                        fab.playAnimation();

                        linearLayout.setVisibility(View.GONE);
                        seekBar.setMax(exoPlayer.getDuration() / 1000);

                    } else if((playbackState == ExoPlayer.STATE_READY)){
                        mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                                exoPlayer.getCurrentPosition(), 1f);

                        exoplayerIsPlaying = false;

                        fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        fab.playAnimation();


                    }


                    if (playbackState == ExoPlayer.STATE_ENDED) {

                        exoplayerIsPlaying = false;
                        if (mHandler != null) {
                            mHandler.removeCallbacks(mRunnable);
                        }
                        //fab.changeMode(FloatingMusicActionButton.Mode.PAUSE_TO_PLAY);
                        fab.playAnimation();

                        if(listSimilarAudio.size() > playlistposi){

                            audio_artiste_selected = listSimilarAudio.get(playlistposi);

                            currentplayinglink = listSimilarAudio.get(playlistposi).getUrl_musique();

                            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentplayinglink), dateSourceFactory, extractorsFactory, new Handler(), null);
                            exoPlayer.prepare(mediaSource);
                            exoPlayer.setPlayWhenReady(true);

                            seekBar.setProgress(0);
                            toolbarProgress.setProgress(0);
                            playlistposi++;


                        }


                    }

                    mMediaSession.setPlaybackState(mStateBuilder.build());
                    showNotification(mStateBuilder.build(), audio_artiste_selected);

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
                         mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000; // In milliseconds
                        seekBar.setProgress(mCurrentPosition);
                        toolbarProgress.setProgress(mCurrentPosition);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.now_playin_menu, menu);
      //  collapsingMenu = menu;
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

        String text = info+" "+getString(R.string.share_textt);


        if (bitmap != null) {

            Log.d("TAG", "bitmap is not null");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,baos);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"title",null);


            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "TIZIK"));

            File tbd = new File(path);
            tbd.deleteOnExit();

        } else {

            Log.d("TAG", "bitmap is null");
            // ...sharing failed, handle error
        }
    }


    private void releasePlayer() {
        mNotificationManager.cancelAll();

        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;

        exoplayerIsPlaying = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onCommentDOne(int size) {

        if(size != 0){

            txtavis.setText(String.valueOf(size));
        }

    }

    @Override
    public void onClick(int position) {

        if(null != exoPlayer){
            releasePlayer();
        }

        Intent intent =  new Intent(this, NowPlayingActivity.class);
        intent.putExtra(MUSIC_EXTRA, listSimilarAudio.get(position));
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putInt(MUSIC_STATE_POSITION_KEY, mCurrentPosition);
            outState.putParcelable(MUSIC_KEY, audio_artiste_selected);
            outState.putDouble(MUSIC_AVIS_KEY, moyenne);
            outState.putInt(MUSIC_AVIS_SIZE, numComment);
            outState.putParcelableArrayList(MUSIC_SIMILAR_KEY, (ArrayList<? extends Parcelable>) listSimilarAudio);
            outState.putInt(MUSIC_PLAYLIST_POSI_KEY, playlistposi);
            outState.putString(MUSIC_PLAYING_LINK, currentplayinglink);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(NowPlayingActivity.this, MainActivity.class);
        if(playlistposi > 0){

            intent.putExtra(MUSIC_EXTRA, audio_artiste_selected);
        }else{

            intent.putExtra(MUSIC_EXTRA, listSimilarAudio.get(playlistposi));
        }
        intent.putExtra(MUSIC_STATE_POSITION_KEY, mCurrentPosition);

        startActivity(intent);
        finish();
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    private void showNotification(PlaybackStateCompat state, Audio_Artiste audio_artiste) {

        // You only need to create the channel on API 26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.ic_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.ic_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.ic_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        NotificationCompat.Action nextAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.ic_next, getString(R.string.next),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (this, 0, new Intent(this, NowPlayingActivity.class).putExtra(MUSIC_EXTRA, audio_artiste), 0);


        //todo replace the icon
        builder.setContentTitle(audio_artiste.getTitre_musique())
                .setContentText(audio_artiste.getNom_chanteur())
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .addAction(nextAction)
                .setStyle( new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1));


        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }



    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }
    }



    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = CHANNEL_ID;
        // The user-visible name of the channel.
        CharSequence name = "Media playback";
        // The user-visible description of the channel.
        String description = "Media playback controls";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mNotificationManager.createNotificationChannel(mChannel);
    }
}



