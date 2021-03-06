package com.shif.peterson.tizik;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.shif.peterson.tizik.adapter.UploadMusicStepAdapter;
import com.shif.peterson.tizik.fragment.StepChooseMusicFragment;
import com.shif.peterson.tizik.fragment.StepFinishFragment;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.services.MusicUploadService;
import com.shif.peterson.tizik.utilis.SelectableItem;
import com.stepstone.stepper.StepperLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadMusicActivity extends AppCompatActivity
        implements StepChooseMusicFragment.OnFragmentInteractionListener,
        StepFinishFragment.onChoosenMusicListener {


    private final String TAG = UploadMusicActivity.class.getSimpleName();

    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";

    private String CURRENT_STEP_POSITION_KEY = "position";
    public static final String EXTRA_FILE_COVER_URI = "extra_file_cover_uri";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    public static final String EXTRA_MUSIC_NOM_ARTISTE = "extra_music_nom_artiste";
    public static final String EXTRA_MUSIC_ID_ARTISTE = "extra_music_artiste";
    public static final String EXTRA_MUSIC_TITLE = "extra_music_title";
    public static final String EXTRA_MUSIC_ALBUM = "extra_music_album";
    public static final String EXTRA_MUSIC_DURATION = "extra_music_duration";
    public static final String EXTRA_MUSIC_PRIICE = "extra_music_price";

    String nom_artiste;
    String titreMusique;
    String musicArtist;
    String musicAlbum;
    long musicDuration;
    double price;
    List<Categorie> categorieListToUpload;

    Uri mFileUri;
    Uri mPhotoUri;

    private StepperLayout mStepperLayout;
    UploadMusicStepAdapter uploadMusicStepAdapter;
    static List<SelectableItem> selectableItems;
    private BroadcastReceiver mBroadcastReceiver;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_music);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mStepperLayout = findViewById(R.id.step);
        int currentPosition = 0;

        if( savedInstanceState != null ){

            if(0 < savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) ){

                currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
            }


            mFileUri = savedInstanceState.getParcelable(EXTRA_FILE_URI);
            mPhotoUri = savedInstanceState.getParcelable(EXTRA_FILE_COVER_URI);
            titreMusique = savedInstanceState.getString(EXTRA_MUSIC_TITLE);
            nom_artiste = savedInstanceState.getString(EXTRA_MUSIC_NOM_ARTISTE);
            musicArtist = savedInstanceState.getString(EXTRA_MUSIC_ID_ARTISTE);
            musicAlbum = savedInstanceState.getString(EXTRA_MUSIC_ALBUM);
            musicDuration = savedInstanceState.getLong(EXTRA_MUSIC_DURATION);
            price = savedInstanceState.getDouble(EXTRA_MUSIC_PRIICE);


        }
        if(getIntent().hasExtra(EXTRA_ID_UTILISATEUR)){

            musicArtist = getIntent().getStringExtra(EXTRA_ID_UTILISATEUR);

        }

        uploadMusicStepAdapter = new UploadMusicStepAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(uploadMusicStepAdapter, currentPosition);

        onNewIntent(getIntent());


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d(TAG, "onReceive:" + intent);
                hideProgressDialog();

                switch (intent.getAction()) {

                    case MusicUploadService.UPLOAD_COMPLETED:

                        //PreferenceUtils.DeleteSelelectedPhoto(SellActivity.this);

                        //Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case MusicUploadService.UPLOAD_ERROR:
                        onUploadResultIntent(intent);
                        break;
                }

            }
        };
    }


    @Override
    public void onFragmentInteraction(List<SelectableItem> selectableItems) {

        UploadMusicActivity.selectableItems = new ArrayList<>();
        for (SelectableItem selectableItem : selectableItems) {

            selectableItem.setSelected(true);
            UploadMusicActivity.selectableItems.add(selectableItem);
        }
        StepFinishFragment.initListSelectedItem(selectableItems);
    }


    @Override
    public void onMusicChoosed(List<SelectableItem> selectableItemList) {

        for(int i = 0; i< selectableItemList.size(); i++){

            SelectableItem selectableItem = selectableItemList.get(i);
            selectableItem.setNom_chanteur(selectableItem.getArtiste());

            if(!selectableItem.getSelectedCategorie().isEmpty()) {

                uploadFromUri(Uri.fromFile(new File(selectableItem.getUrl_musique())),Uri.fromFile(new File(selectableItem.getUrl_poster())), selectableItem);
            }
        }
    }


    private void uploadFromUri(Uri fileUri, Uri photoUri, SelectableItem selectableItem) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());

        // Save the File URI
        mFileUri = fileUri;
        mPhotoUri = photoUri;

         titreMusique =  selectableItem.getTitre_musique();
         musicAlbum =  selectableItem.getNomAlbum();
         musicDuration = (long) selectableItem.getDuree_musique();
         price = selectableItem.getPrix();
         nom_artiste = selectableItem.getNom_chanteur();
         categorieListToUpload = selectableItem.getSelectedCategorie();

        startService(new Intent(this, MusicUploadService.class)
                .putExtra(MusicUploadService.EXTRA_FILE_URI, mFileUri)
                .putExtra(MusicUploadService.EXTRA_FILE_COVER_URI, mPhotoUri)
                .putExtra(MusicUploadService.EXTRA_MUSIC_ID_ARTISTE, musicArtist )
                .putExtra(MusicUploadService.EXTRA_MUSIC_TITLE, titreMusique)
                .putExtra(MusicUploadService.EXTRA_MUSIC_ALBUM, musicAlbum)
                .putExtra(MusicUploadService.EXTRA_MUSIC_DURATION, musicDuration)
                .putExtra(MusicUploadService.EXTRA_MUSIC_PRIICE, price)
                .putExtra(MusicUploadService.EXTRA_MUSIC_NOM_ARTISTE, nom_artiste)
                .putParcelableArrayListExtra(MusicUploadService.EXTRA_MUSIC_CATEGORY, (ArrayList<? extends Parcelable>) categorieListToUpload)
                .setAction(MusicUploadService.ACTION_UPLOAD));

        // todo replace the spinner with some more cool like a dialog box
        showProgressDialog(getString(R.string.progress_uploading));
    }



    private void showProgressDialog(String caption) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(caption);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // Check if this Activity was launched by clicking on an upload notification
        if (intent.hasExtra(MusicUploadService.EXTRA_DOWNLOAD_URL)) {
            onUploadResultIntent(intent);
        }

    }

    private void onUploadResultIntent(Intent intent) {
        // Got a new intent from MyUploadService with a success or failure

        mFileUri = intent.getParcelableExtra(MusicUploadService.EXTRA_FILE_URI);
        mPhotoUri = intent.getParcelableExtra(MusicUploadService.EXTRA_FILE_COVER_URI);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        outState.putParcelable(EXTRA_FILE_URI, mFileUri);
        outState.putParcelable(EXTRA_FILE_COVER_URI, mPhotoUri);

        outState.putString(EXTRA_MUSIC_TITLE, titreMusique);
        outState.putString(EXTRA_MUSIC_NOM_ARTISTE, nom_artiste);
        outState.putString(EXTRA_MUSIC_ID_ARTISTE, musicArtist);
        outState.putString(EXTRA_MUSIC_ALBUM, musicAlbum);
        outState.putLong(EXTRA_MUSIC_DURATION, musicDuration);
        outState.putDouble(EXTRA_MUSIC_PRIICE, price );
        outState.putParcelableArrayList(MusicUploadService.EXTRA_MUSIC_CATEGORY, (ArrayList<? extends Parcelable>) categorieListToUpload);



    }

    @Override
    public void onStart() {
        super.onStart();
        // Register receiver for uploads and downloads
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(mBroadcastReceiver, MusicUploadService.getIntentFilter());
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister download receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
