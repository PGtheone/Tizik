package com.shif.peterson.tizik;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.fragment.DetailAudioBottomSheet;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.services.AudioPlayerService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.tankery.lib.circularseekbar.CircularSeekBar;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_SIMILAR_KEY;


public class NowPlayingActivity extends AppCompatActivity implements
        ServiceConnection
{

    private static final int[] STATE_PLAY = new int[]{R.attr.state_play, -R.attr.state_pause};

    private static final String CHANNEL_ID = "media_playback_channel";

    private String MUSIC_STATE_POSITION_KEY = "music_position";
    private String MUSIC_KEY = "music_key";
    private String MUSIC_AVIS_KEY = "music_avis";
    private String MUSIC_AVIS_SIZE = "musoc_avis_size";
    private String MUSIC_PLAYLIST_POSI_KEY = "music_playlist_posi";
    private String MUSIC_PLAYING_LINK = "music_playing_link";
    int ACTIVITY_REQUEST_CODE = 10000;
    private static final int[] STATE_PAUSE = new int[]{-R.attr.state_play, R.attr.state_pause};
    private final String music_IS_PLAYING = "isPlaying";


    LinearLayout linearLayout;

    private ImageView imgblur;
    private ImageView imgcover;
    private final String TAG = NowPlayingActivity.class.getSimpleName();
    //Exoplayer
    SimpleExoPlayer exoPlayer;
    AudioPlayerService mService;
    Intent serviceIntent;
    IBinder myBinder;
    private AppCompatImageView imgprevious;

    List<Audio_Artiste> listSimilarAudio;
    private AppCompatImageView imgnext;
    boolean exoplayerIsPlaying = false;

    Animator animator;

    private Handler mHandler;
    private Runnable mRunnable;

    CircularSeekBar seekBar;

    private FirebaseAuth mAuth;

    Bitmap bitmap = null;
    int playlistposi;


    int mCurrentPosition;
    double moyenne;
    int numComment;
    Audio_Artiste audio_artiste_selected;
    String currentplayinglink;
    private MyTextView_Ubuntu_Bold txtsongName;
    private MyTextView_Ubuntu_Regular txtalbum;
    private ImageView imgplaypause;
    private ImageView imgback;
    private ImageView imgmore;
    private boolean mBound = false;



    private static final String COLLECTION_NAME = "Commentaire_Audio";
    public static CollectionReference getCommentCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listSimilarAudio = new ArrayList<>();

        intitUI();
        mHandler = new Handler();
        mAuth = FirebaseAuth.getInstance();

        if(savedInstanceState == null){

            if(getIntent().hasExtra(MUSIC_EXTRA)){

                audio_artiste_selected = getIntent().getParcelableExtra(MUSIC_EXTRA);
                listSimilarAudio = getIntent().getParcelableArrayListExtra(MUSIC_SIMILAR_KEY);
                if (audio_artiste_selected != null){

                    if(serviceIntent == null){

                        serviceIntent = new Intent(this, AudioPlayerService.class);
                        serviceIntent.putExtra(MUSIC_EXTRA, audio_artiste_selected);
                        serviceIntent.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, (ArrayList<? extends Parcelable>) listSimilarAudio);
                        Util.startForegroundService(this, serviceIntent);

                         }


                   // updateUI(audio_artiste_selected);
                }

            }
        }else{



            audio_artiste_selected = savedInstanceState.getParcelable(MUSIC_KEY);
            moyenne = savedInstanceState.getDouble(MUSIC_AVIS_KEY);
            numComment = savedInstanceState.getInt(MUSIC_AVIS_SIZE);
          //  recyclersimilar.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(MUSIC_SIMILAR_KEY));
            playlistposi = savedInstanceState.getInt(MUSIC_PLAYLIST_POSI_KEY);

           // updateUI(audio_artiste_selected);



        }


//
//        btncomment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (audio_artiste_selected != null && mAuth.getInstance().getCurrentUser() != null){
//
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    final CommentDialogFragment fragment = CommentDialogFragment.newInstance(audio_artiste_selected.getId_musique(),  mAuth.getInstance().getCurrentUser().getUid());
//                    fragment.show(fragmentTransaction, "Comment dialog");
//                }
//
//
//            }
//        });
//
//

    }

    private void intitUI() {


        imgblur = findViewById(R.id.image_blur);
        imgcover = findViewById(R.id.circularimageview);
        imgprevious = findViewById(R.id.previous);
        imgnext = findViewById(R.id.next);
        imgback = findViewById(R.id.imgback);
        imgmore = findViewById(R.id.imgmore);

        seekBar = findViewById(R.id.seek_bar);

        txtsongName = findViewById(R.id.txtnom);
        txtalbum = findViewById(R.id.txtalbum);
        imgplaypause = findViewById(R.id.imgplay);
        imgplaypause.setImageState(STATE_PLAY, true);

        animator = AnimatorInflater.loadAnimator(this, R.animator.clockwise_rotation);
        animator.setTarget(imgcover);


        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NowPlayingActivity.super.onBackPressed();
            }
        });



    }

    private void updateUI(final Audio_Artiste audio_artiste_selected){


        if( null != audio_artiste_selected.getUrl_poster()){

            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .transform( new BlurTransformation(50))
                    .error(R.color.colorPrimary)
                    .placeholder(R.color.colorPrimary);


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

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getWindow().setStatusBarColor(dark.getRgb());
                                    }

                                }
                            }
                            return false;
                        }
                    })
                    .into(imgcover);




        }


        txtsongName.setText(audio_artiste_selected.getTitre_musique());
        txtalbum.setText(audio_artiste_selected.getId_album());





        //comments

//        getCommentCollectionReference()
//                    .whereEqualTo("id_audio", audio_artiste_selected.getId_musique())
//                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                    if (null != queryDocumentSnapshots && 0 < queryDocumentSnapshots.size()){
//
//                        List<Double> notes = new ArrayList<>();
//                        numComment = queryDocumentSnapshots.size();
//                        txtavis.setText(String.valueOf(queryDocumentSnapshots.size()));
//
//
//                        List<Commentaire_Audio> commentaire_audios = queryDocumentSnapshots.toObjects(Commentaire_Audio.class);
//                        for (Commentaire_Audio commentaire_audio : commentaire_audios){
//
//                            notes.add((double) commentaire_audio.getNote());
//
//                        }
//                        moyenne = calculerMoyenne(notes);
//                        ratingBar.setRating((float) moyenne);
//
//                    }else{
//
//                        txtavis.setText("0");
//                        ratingBar.setRating(0);
//                    }
//
//                }
//            });


    }

    private void initExoPlayer2() {

        exoPlayer = mService.getplayerInstance();

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


                    imgplaypause.setImageState(STATE_PAUSE, true);
                    animator.start();


                }

                if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){


                    exoplayerIsPlaying = true;
                     seekBar.setMax(exoPlayer.getDuration() / 1000);

                } else if((playbackState == ExoPlayer.STATE_READY)){

                    exoplayerIsPlaying = false;

                }


                if (playbackState == ExoPlayer.STATE_ENDED ) {

                    exoplayerIsPlaying = false;
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }

                    exoPlayer.next();


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
                    mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000; // In milliseconds
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
    protected void onStart() {
        super.onStart();

        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();

            unbindService(this);

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
        exoPlayer.stop();
        exoPlayer.release();

        exoplayerIsPlaying = false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putParcelable(MUSIC_KEY, audio_artiste_selected);
            outState.putDouble(MUSIC_AVIS_KEY, moyenne);
            outState.putInt(MUSIC_AVIS_SIZE, numComment);


    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        final AudioPlayerService.LocalBinder binder = (AudioPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;


            initExoPlayer2();
            updateUI(audio_artiste_selected);
            listSimilarAudio = binder.getAudio();


        imgplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exoPlayer != null ){

                    if (exoplayerIsPlaying){

                        pausePlayer();
                        imgplaypause.setImageState(STATE_PLAY, true);
                        exoplayerIsPlaying = false;
                        animator.pause();

                    }else{

                        resumePlayer();
                        imgplaypause.setImageState(STATE_PAUSE, true);
                        exoplayerIsPlaying = true;
                        animator.start();
                    }


                }


            }
        });


        if(exoPlayer != null){


            imgmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DetailAudioBottomSheet
                            .newInstance(binder.getAudio().get(exoPlayer.getCurrentWindowIndex()).getId_musique(), listSimilarAudio)
                            .show(getSupportFragmentManager(), "MODAL");

                }
            });


            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    exoPlayer.next();


                }
            });

            imgprevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    exoPlayer.previous();

                }
            });



            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    updateUI(binder.getAudio().get(exoPlayer.getCurrentWindowIndex()));
                }
            });

        }


    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        mBound = false;
    }

}



