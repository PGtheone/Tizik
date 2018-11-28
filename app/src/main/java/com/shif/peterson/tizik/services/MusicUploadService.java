package com.shif.peterson.tizik.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shif.peterson.tizik.MainActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Utilisateur;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.UUID;

public class MusicUploadService extends MyBaseTaskService {

    private static final String TAG = "MyUploadService";

    /** Intent Actions **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";


    /** Intent Extras **/
    public static final String EXTRA_FILE_COVER_URI = "extra_file_cover_uri";
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    public static final String EXTRA_MUSIC_ID_ARTISTE = "extra_music_artiste";
    public static final String EXTRA_MUSIC_TITLE = "extra_music_title";
    public static final String EXTRA_MUSIC_ALBUM = "extra_music_album";
    public static final String EXTRA_MUSIC_DURATION = "extra_music_duration";
    public static final String EXTRA_MUSIC_PRIICE = "extra_music_price";
    public static final String EXTRA_MUSIC_NOM_ARTISTE = "extra_music_nom_artiste";

    // [START declare_ref]
    private StorageReference mStorageRef;
    private StorageReference mFileCoverStorageRef;
    // [END declare_ref]

    private static final String COLLECTION_NAME = "Audio";
    public static CollectionReference getMusicCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // [START get_storage_ref]
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFileCoverStorageRef = FirebaseStorage.getInstance().getReference();
        // [END get_storage_ref]
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            Uri fileUri = intent.getParcelableExtra(EXTRA_FILE_URI);
            Uri fileCoverUri = intent.getParcelableExtra(EXTRA_FILE_COVER_URI);

         String idArtiste =  intent.getStringExtra(EXTRA_MUSIC_ID_ARTISTE);
         String titreMusique =  intent.getStringExtra(EXTRA_MUSIC_TITLE);
         String nomAlbum = intent.getStringExtra(EXTRA_MUSIC_ALBUM);
         long durationMusique = intent.getLongExtra(EXTRA_MUSIC_DURATION, 0);
         double musicPrice = intent.getDoubleExtra(EXTRA_MUSIC_PRIICE, 0);

         Log.d(TAG, "Music info"+titreMusique);
         Log.d(TAG, "Music info"+nomAlbum);
         Log.d(TAG, "Music info"+durationMusique);

           // boolean iscover = intent.getBooleanExtra(EXTRA_PRODUIT_ISCOVER, false);

            // Make sure we have permission to read the data
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getContentResolver().takePersistableUriPermission(
//                        fileUri,
//                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }

            uploadFromUri(fileUri, fileCoverUri, idArtiste, titreMusique, nomAlbum, durationMusique, musicPrice);
        }

        return START_REDELIVER_INTENT;
    }


    // [START upload_from_uri]
    private void uploadFromUri(final Uri fileUri, final Uri fileCoverUri ,final String idArtiste, final String titreMusique, final String nomAlbum, final long duartionMusique, final double musicPrice ) {
        Log.d(TAG, "uploadFromUri:src:" + fileUri.toString());
        Log.d(TAG, "uploadCoverFromUri:src:" + fileCoverUri.toString());

        // [START_EXCLUDE]
        taskStarted();
        showProgressNotification(getString(R.string.progress_uploading), 0, 0);
        // [END_EXCLUDE]

        // [START get_child_ref]
        // Get a reference to store file at photos/<FILENAME>.jpg
        final StorageReference music_uploadRef = mStorageRef.child("Audio")
                .child(fileUri.getLastPathSegment());

        final StorageReference photo_coverRef = mFileCoverStorageRef.child("Images")
                .child(fileUri.getLastPathSegment());

        // [END get_child_ref]

        // Upload file to Firebase Storage
        Log.d(TAG, "uploadFromUri:dst:" + music_uploadRef.getPath());
        music_uploadRef.putFile(fileUri).
                addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        showProgressNotification(getString(R.string.progress_uploading),
                                taskSnapshot.getBytesTransferred(),
                                taskSnapshot.getTotalByteCount());
                    }
                })
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        // Forward any exceptions
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        Log.d(TAG, "uploadFromUri: upload success");

                        // Request the public download URL
                        return music_uploadRef.getDownloadUrl();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull final Uri downloadUri) {
                        // Upload succeeded
                        Log.d(TAG, "uploadFromUri: getDownloadUri success");


                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(fileUri.getPath());
                        byte[] coverBytes = retriever.getEmbeddedPicture();
                        Bitmap songCover;

                        if (coverBytes!=null){

                            songCover = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            songCover.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            final String MusicUrl = downloadUri.toString();

                            photo_coverRef.putBytes(data)
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                            showProgressNotification(getString(R.string.progress_uploading),
                                                    taskSnapshot.getBytesTransferred(),
                                                    taskSnapshot.getTotalByteCount());
                                        }
                                    })
                                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            // Forward any exceptions
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            Log.d(TAG, "uploadFromUri: upload success");


                                            // Request the public download URL
                                            return photo_coverRef.getDownloadUrl();

                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {

                                            Audio_Artiste audio_artiste = new Audio_Artiste();
                                            audio_artiste.setId_musique(UUID.randomUUID().toString());
                                            audio_artiste.setTitre_musique(titreMusique);
                                            audio_artiste.setuploaded_by(idArtiste);
                                            audio_artiste.setId_album(nomAlbum);
                                            audio_artiste.setDuree_musique((double) duartionMusique);
                                            audio_artiste.setPrix(musicPrice);
                                            audio_artiste.setUrl_musique(MusicUrl);
                                            audio_artiste.setUrl_poster(uri.toString());
                                            if(musicPrice == 0){

                                                audio_artiste.setIs_Free(true);
                                            }else{
                                                audio_artiste.setIs_Free(false);
                                            }
                                            audio_artiste.setIs_actif(true);
                                            audio_artiste.setDate_upload(new Date().toString());

                                           getMusicCollectionReference().add(audio_artiste).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    Log.d(TAG, "Success");

                                                }
                                            });



                                            //todo high priority add imageitem to backend in this side

//                                        Photo_Produit photo_produit = new Photo_Produit();
//                                        photo_produit.setIdPhotoProduit(UUID.randomUUID().toString());
//                                        photo_produit.setId_produit(idprod);
//                                        photo_produit.setCoverPhoto(iscover);
//                                        photo_produit.setUrlPhoto("product_photos/" + downloadUri.getLastPathSegment());
//
//                                        PhotoProduitNetworkUtils.getProdPicCollectionReference().add(photo_produit).addOnSuccessListener( new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//
//                                            }
//                                        });


                                            // [START_EXCLUDE]
                                            broadcastUploadFinished(downloadUri, fileUri);
                                            showUploadFinishedNotification(downloadUri, fileUri);
                                            taskCompleted();
                                            // [END_EXCLUDE]


                                        }
                                    });

                        }else{


                            //todo poster is null do some
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Upload failed
                        Log.w(TAG, "uploadFromUri:onFailure", exception);

                        // [START_EXCLUDE]
                        broadcastUploadFinished(null, fileUri);
                        showUploadFinishedNotification(null, fileUri);
                        taskCompleted();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END upload_from_uri]

    /**
     * Broadcast finished upload (success or failure).
     * @return true if a running receiver received the broadcast.
     */
    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        boolean success = downloadUrl != null;

        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;

        Intent broadcast = new Intent(action)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri);
        return LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(broadcast);
    }

    /**
     * Show a notification for a finished upload.
     */
    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
        // Hide the progress notification
        dismissProgressNotification();

        // Make Intent to MainActivity
        Intent intent = new Intent(this, MainActivity.class)
                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
                .putExtra(EXTRA_FILE_URI, fileUri)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        boolean success = downloadUrl != null;
        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
        showFinishedNotification(caption, intent, success);
    }

    public static IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPLOAD_COMPLETED);
        filter.addAction(UPLOAD_ERROR);

        return filter;
    }

}


