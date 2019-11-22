package com.shif.peterson.tizik.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.DownloadUtil;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.PLAYBACK_CHANNEL_ID;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.PLAYBACK_NOTIFICATION_ID;

public class AudioPlayerService extends Service {

    private final String MUSIC_SIMILAR_KEY = "similar_music";
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private final IBinder mBinder = new LocalBinder();
    public SimpleExoPlayer player;
    Context context;
    ConcatenatingMediaSource concatenatingMediaSource;
   // private MediaSessionConnector mediaSessionConnector;
    CacheDataSourceFactory cacheDataSourceFactory;
    Bitmap bitmap;
    List<Audio_Artiste> SAMPLES;
    Audio_Artiste audio_artiste;
     FirebaseFirestore db = FirebaseFirestore.getInstance();
     CollectionReference audioTendanceRef = db.collection("AudioTendance");

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, getString(R.string.app_name)));
         cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(context),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);

         concatenatingMediaSource = new ConcatenatingMediaSource();



    }



    @Override
    public void onDestroy() {
//        mediaSession.release();
       // mediaSessionConnector.setPlayer(null, null);
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        SAMPLES = new ArrayList<>();
        if(intent.hasExtra(MUSIC_SIMILAR_KEY)){

            SAMPLES = intent.getParcelableArrayListExtra(MUSIC_SIMILAR_KEY);
        }
        if(intent.hasExtra(MUSIC_EXTRA)){

            audio_artiste = intent.getParcelableExtra(MUSIC_EXTRA);
        }
        SAMPLES.add(0, audio_artiste);

        if(SAMPLES.size() == 1){

            iniLitsAudio();
        }else{
            startPlayer(SAMPLES);

        }


        return START_NOT_STICKY;
    }


    public void startPlayer(List<Audio_Artiste> audioArtisteList){


        for (Audio_Artiste sample : audioArtisteList) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(Uri.parse(sample.getUrl_musique()));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        player.prepare(concatenatingMediaSource);
        player.setPlayWhenReady(true);

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.playback_channel_name,
                PLAYBACK_NOTIFICATION_ID,
                new MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return SAMPLES.get(player.getCurrentWindowIndex()).getTitre_musique();

                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {

                        Intent intent1 = new Intent(context, NowPlayingActivity.class);
                        intent1.putExtra(MUSIC_EXTRA, SAMPLES.get( player.getCurrentWindowIndex()));

                        return PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return SAMPLES.get(player.getCurrentWindowIndex()).getNom_chanteur();
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {

                        Glide.with(context)
                                .load( SAMPLES.get(player.getCurrentWindowIndex()).getUrl_poster())
                                .transition(withCrossFade())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

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
                                        return false;
                                    }
                                }).submit();


                        return bitmap;
                    }
                }
        );
        playerNotificationManager.setNotificationListener(new NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(player);



    }

    private void iniLitsAudio() {

        concatenatingMediaSource = new ConcatenatingMediaSource();

        FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .limit(10)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){

                    SAMPLES.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                    startPlayer(SAMPLES);
                }
            }
        });


    }

    public List<Audio_Artiste> getSAMPLES(){

        if (SAMPLES == null || SAMPLES.isEmpty()){

            iniLitsAudio();
        }

        return SAMPLES;
    }

    public SimpleExoPlayer getplayerInstance() {
        if (player != null ) {
            startPlayer(SAMPLES);
        }
        return player;
    }


    public class LocalBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }

        public List<Audio_Artiste> getAudio(){

            return getSAMPLES();
        }
    }

}
