package com.shif.peterson.tizik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.MusicPlaylistAdapter;
import com.shif.peterson.tizik.adapter.PlaylistPagerAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Playlist;
import com.shif.peterson.tizik.model.Playlist_Audio;
import com.shif.peterson.tizik.utilis.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DetailPlaylistActivity extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, PlaylistPagerAdapter.playlistClickHandler {

    public static String EXTRA_PLAYLIST = "extra_playlist";
    private final String MUSIC_EXTRA = "music_extra";
    private final String MUSIC_SIMILAR_KEY = "similar_music";

    Playlist playlist;
    List<Playlist_Audio> playlist_audioList;
    List<Audio_Artiste> audio_artisteList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    List<Playlist> playlistList;
    Query query;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration itemDecor;
    Bitmap bitmap = null;
    Intent serviceIntent;
    private ImageView imgbg;
    private RecyclerView recyclerView;
    private MusicPlaylistAdapter musicAdapter;
    private ProgressBar progressBar;
    private ViewPager mViewPager;
    private MyTextView_Ubuntu_Bold txtcountmusic;
    private PlaylistPagerAdapter playlistPagerAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference playlistAudioRef = db.collection("PlaylistAudio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);
        initUI();

        playlistList = new ArrayList<>();
        if(getIntent().hasExtra(EXTRA_PLAYLIST)){

            playlist = getIntent().getParcelableExtra(EXTRA_PLAYLIST);

        }

        playlistPagerAdapter = new PlaylistPagerAdapter(this);

        if(playlist == null){
            FirebaseFirestore
                    .getInstance()
                    .collection("Playlist")
                    .limit(10)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        playlistList.addAll(queryDocumentSnapshots.toObjects(Playlist.class));
                        playlist = playlistList.get(0);
                        query = playlistAudioRef
                                .whereEqualTo("id_playlist", playlist.getId_playlist())
                                .limit(50);

                        blurBackgroundList(playlist);
                        initList();

//                        playlistPagerAdapter = new PlaylistPagerAdapter(new PlaylistPagerAdapter.playlistClickHandler() {
//                            @Override
//                            public void onClick(Playlist playlist) {
//
//                                if(serviceIntent == null){
//
//                                    serviceIntent = new Intent(DetailPlaylistActivity.this, AudioPlayerService.class);
//                                    serviceIntent.putExtra(MUSIC_EXTRA, audio_artisteList.get(0));
//                                    serviceIntent.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, (ArrayList<? extends Parcelable>) audio_artisteList);
//                                    Util.startForegroundService(DetailPlaylistActivity.this, serviceIntent);
//                                }
//                            }
//                        });

//                        musicAdapter.notifyDataSetChanged();
                        for(Playlist playlist: playlistList){
                            playlistPagerAdapter.addPlaylist(playlist);
                        }

                        mViewPager.setAdapter(playlistPagerAdapter);
                        mCardShadowTransformer = new ShadowTransformer(mViewPager, playlistPagerAdapter);
                        mViewPager.setPageTransformer(false, mCardShadowTransformer);
                        mViewPager.setOffscreenPageLimit(3);

                    }
                }
            });

        }else{

//            playlistPagerAdapter = new PlaylistPagerAdapter(new PlaylistPagerAdapter.playlistClickHandler() {
//                @Override
//                public void onClick(Playlist playlist) {
//
//                    if(serviceIntent == null){
//
//                        serviceIntent = new Intent(DetailPlaylistActivity.this, AudioPlayerService.class);
//                        serviceIntent.putExtra(MUSIC_EXTRA, audio_artisteList.get(0));
//                        serviceIntent.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, (ArrayList<? extends Parcelable>) audio_artisteList);
//                        Util.startForegroundService(DetailPlaylistActivity.this, serviceIntent);
//                    }
//                }
//            });
            playlistList.add(playlist);
            playlistPagerAdapter.addPlaylist(playlist);
            mViewPager.setAdapter(playlistPagerAdapter);
            mCardShadowTransformer = new ShadowTransformer(mViewPager, playlistPagerAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);
//
//            // progress.setVisibility(View.VISIBLE);
//
            query = playlistAudioRef
                    .whereEqualTo("id_playlist", playlist.getId_playlist())
                    .limit(50);

            blurBackgroundList(playlist);
            initList();
            musicAdapter.notifyDataSetChanged();

        }



        mViewPager.setOnPageChangeListener(this);
    }

    private void initUI() {

        mViewPager = findViewById(R.id.viewpager);
        imgbg = findViewById(R.id.image_blur);
        recyclerView = findViewById(R.id.recyr_all_musique);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        txtcountmusic = findViewById(R.id.txtallmusiques);
        progressBar = findViewById(R.id.progress_bar);
    }


    private void blurBackgroundList(Playlist playlistselected){

        RequestOptions glideOptions = new RequestOptions()
                .centerCrop()
                .transform( new BlurTransformation(50))
                .error(R.color.colorPrimary)
                .placeholder(R.color.colorPrimary);

        Glide.with(this)
                .load(playlistselected.getUrlPoster())
                .apply(glideOptions)
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

                        collapsingToolbarLayout.setContentScrimColor(vibrant.getRgb());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(dark.getRgb());
                        }else{
                            collapsingToolbarLayout.setStatusBarScrimColor(dark.getRgb());

                        }
                       // collapsingToolbarLayout.setBackgroundColor(vibrant.getRgb());

                    }
                }
                return false;
            }
        })
                .into(imgbg);

    }


    private void initList(){

        if (query == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        musicAdapter = new MusicPlaylistAdapter(DetailPlaylistActivity.this, query, new MusicPlaylistAdapter.AudioClickHandler() {
            @Override
            public void OnClick(Audio_Artiste produit) {

                Intent intent2 =  new Intent(DetailPlaylistActivity.this, NowPlayingActivity.class);
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

                    //avl.setVisibility(View.GONE);
//                    empty_const.setVisibility(View.VISIBLE);
//                    recyclerViewMusique.setVisibility(View.GONE);
//                    progress.setVisibility(View.GONE);
//                    btnviewall.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            query = audioTendanceRef
//                                    .limit(100);
//                            musicAdapter.setQuery(query);
//
//                        }
//                    });

                } else {

                    Log.i("TAG", " RecyclerView");

                    txtcountmusic.setText(getItemCount()+" "+getString(R.string.music_found));
//                    txtMusiqueCount.setVisibility(View.VISIBLE);
//                    empty_const.setVisibility(View.GONE);
//                    recyclerViewMusique.setVisibility(View.VISIBLE);
//                    progress.setVisibility(View.GONE);
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

        layoutManager = new LinearLayoutManager(DetailPlaylistActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        itemDecor = new DividerItemDecoration(DetailPlaylistActivity.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(musicAdapter);
        recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        playlist = playlistList.get(position);
       // progress.setVisibility(View.VISIBLE);

            query = playlistAudioRef
                    .whereEqualTo("id_playlist", playlist.getId_playlist())
                    .limit(50);

            blurBackgroundList(playlist);
            musicAdapter.setQuery(query);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (musicAdapter != null ) {
            musicAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (musicAdapter != null ) {
            musicAdapter.stopListening();
        }
    }


    @Override
    public void onClick(Playlist playlist) {

//        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
//
//        if(serviceIntent == null){
//
//            serviceIntent = new Intent(this, AudioPlayerService.class);
//            serviceIntent.putExtra(MUSIC_EXTRA, audio_artisteList.get(0));
//            serviceIntent.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, (ArrayList<? extends Parcelable>) audio_artisteList);
//            Util.startForegroundService(this, serviceIntent);
//        }
    }
}
