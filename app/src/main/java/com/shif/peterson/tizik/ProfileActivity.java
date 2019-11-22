package com.shif.peterson.tizik;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.GridAdapter;
import com.shif.peterson.tizik.adapter.SimilarAdater;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Medium;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.fragment.AbonneeListDialogFragment;
import com.shif.peterson.tizik.fragment.AbonnementListDialogFragment;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Utilisateur;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_SIMILAR_KEY;
import static com.shif.peterson.tizik.utilis.Utils.tintMyDrawable;

public class ProfileActivity extends AppCompatActivity implements
GridAdapter.AudioClickHandler,
        AbonneeListDialogFragment.Listener,
        AbonnementListDialogFragment.AbonnementListener {

     static final String ID_USER_EXTRA = "ID_USER_EXTRA";
    List<Audio_Artiste> audio_artistes;
    private MyTextView_Ubuntu_Medium txtnomuser;
    SimilarAdater similarAdater;
    private MyTextView_Ubuntu_Bold txtcountpost;
    GridAdapter gridAdapter;
    //private MyTextView_Ubuntu_Regular txtnomartiste;
    private MyTextView_Ubuntu_Regular txtpost;
    private MyTextView_Ubuntu_Bold txtfollower;
    private MyTextView_Ubuntu_Regular txtcountfollower;
    private MyTextView_Ubuntu_Bold txtfollow;
    private MyTextView_Ubuntu_Regular txtcountfollow;
    private ImageView imgprofil;
    private RecyclerView recyclerViewprofil;
    private ImageView imglist;
    private ImageView imggrid;
    private Button btnabonner;
    private LinearLayout linearLayoutabonnement;
    private LinearLayout linearLayoutabonnee;

    private Utilisateur utilisateur;
    private String idUtilisateur;
    private AVLoadingIndicatorView avLoadingIndicatorView;
    private ConstraintLayout emptystate;
    private String presentation = "grid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().hasExtra(ID_USER_EXTRA)){

            idUtilisateur = getIntent().getStringExtra(ID_USER_EXTRA);
        }

        initUI();
        retrieveData();


    }

    private void retrieveData() {

        Log.d("TAG LOG", idUtilisateur);

        Task taskUtilisateur = FirebaseFirestore
                .getInstance()
                .collection("Utilisateur")
                .whereEqualTo("id_utilisateur", idUtilisateur)
                .get();


        Task taskAbonne = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("id_utilisateur", idUtilisateur)
                .get();

        Task taskAbonnement = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("id_fan", idUtilisateur)
                .get();



        Task taskAudioUploaded = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .whereEqualTo("uploaded_by", idUtilisateur)
                .get();


        //Playlist
        Task taskPlaylist = FirebaseFirestore
                .getInstance()
                .collection("Playlist")
                .limit(10)
                .get();

        Task taskFavori = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();


        final Task<List<QuerySnapshot>> allUserTask = Tasks.
                whenAllSuccess(
                        taskUtilisateur,
                        taskAbonne,
                        taskAbonnement,
                        taskAudioUploaded,
                        taskPlaylist,
                        taskFavori);

        allUserTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                for(int i = 0; i < querySnapshots.size(); i++) {
                    QuerySnapshot queryDocumentSnapshots = querySnapshots.get(i);
                    if (i == 0){

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<Utilisateur> userFirebaselist = new ArrayList<>();
                            userFirebaselist.addAll(queryDocumentSnapshots.toObjects(Utilisateur.class));
                            utilisateur = userFirebaselist.get(0);

                            updateProfil(utilisateur);


                        }

                    }else if (i == 1){

                        if (!queryDocumentSnapshots.isEmpty()){

                            Log.d("TAGs", queryDocumentSnapshots.toString());
                            List<Abonnement> abonnementList = new ArrayList<>();
                            abonnementList.addAll(queryDocumentSnapshots.toObjects(Abonnement.class));

                            updateAbonnee(abonnementList);

                        }else{

                            updateAbonnee(null);


                        }

                    }else if (i == 2){

                        if (!queryDocumentSnapshots.isEmpty()){

                            Log.d("TAGs", queryDocumentSnapshots.toString());
                            List<Abonnement> abonnementList = new ArrayList<>();
                            abonnementList.addAll(queryDocumentSnapshots.toObjects(Abonnement.class));
                            updateAbonnement(abonnementList);

                        }else{
                            updateAbonnement(null);

                        }

                    }else if (i == 3){

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<Audio_Artiste> audioUploadlist = new ArrayList<>();
                            audioUploadlist.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                            updateAudioUpload(audioUploadlist);

                        }else{
                            updateAudioUpload(null);
                            emptystate.setVisibility(View.VISIBLE);

                        }

                    }

                }



                stopAnim();

                imggrid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(presentation.equals("list")){

                            presentation = "grid";

                            Drawable drawable = imggrid.getDrawable();
                            imggrid.setImageDrawable(tintMyDrawable(drawable, getResources().getColor(R.color.colorPrimary)));

                            Drawable drawable2 = imglist.getDrawable();
                            imglist.setImageDrawable(tintMyDrawable(drawable2, getResources().getColor(android.R.color.darker_gray)));


                           gridPresentation();



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

                            linearPresentation();

                        }
                    }


                });

            }
        });


    }

    private void updateAudioUpload(final List<Audio_Artiste> audioUploadlist) {

        if(null == audioUploadlist){

            txtcountpost.setText("0");
        }else{

            txtcountpost.setText(String.valueOf(audioUploadlist.size()));

            similarAdater = new SimilarAdater(ProfileActivity.this, audioUploadlist, new SimilarAdater.SimiClickListener() {
                @Override
                public void onClick(int position) {

                    Intent intent2 =  new Intent(ProfileActivity.this, NowPlayingActivity.class);
                    intent2.putExtra(MUSIC_EXTRA, audioUploadlist.get(position));
                    intent2.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, null);
                    startActivity(intent2);

                }
            });
            gridAdapter = new GridAdapter(ProfileActivity.this, audioUploadlist, this);

            //linearPresentation();
            gridPresentation();

        }
    }


    private void linearPresentation(){

        recyclerViewprofil.removeAllViewsInLayout();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfileActivity.this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewprofil.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(ProfileActivity.this, DividerItemDecoration.VERTICAL);
        recyclerViewprofil.addItemDecoration(itemDecor);
        recyclerViewprofil.setAdapter(similarAdater);
        recyclerViewprofil.setNestedScrollingEnabled(false);
    }

    private void gridPresentation(){

        recyclerViewprofil.removeAllViewsInLayout();

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerViewprofil.setLayoutManager(manager);


       // GridLayoutManager gridLayoutManager = new GridLayoutManager(ProfileActivity.this, 2);
        //gridLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewprofil.setLayoutManager(manager);
       // DividerItemDecoration itemDecor = new DividerItemDecoration(ProfileActivity.this, DividerItemDecoration.VERTICAL);
        //recyclerViewprofil.addItemDecoration();
        recyclerViewprofil.setAdapter(gridAdapter);
        recyclerViewprofil.setNestedScrollingEnabled(false);

    }

    private void updateAbonnee(final List<Abonnement> abonnementList) {

        if(null == abonnementList){

            txtfollower.setText("0");
        }else{

            Toast.makeText(this, "here follower", Toast.LENGTH_SHORT).show();

            txtfollower.setText(String.valueOf(abonnementList.size()));

            linearLayoutabonnee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbonnementListDialogFragment
                            .newInstance(abonnementList)
                            .show(getSupportFragmentManager(), "MODAL");

                }
            });
        }
    }

    private void updateAbonnement(final List<Abonnement> abonnementList) {

        if(null == abonnementList){

            txtfollow.setText("0");
        }else{

            Toast.makeText(this, "here follow", Toast.LENGTH_SHORT).show();
            txtfollow.setText(String.valueOf(abonnementList.size()));
            linearLayoutabonnement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbonneeListDialogFragment
                            .newInstance(abonnementList)
                            .show(getSupportFragmentManager(), "MODAL");

                }
            });
        }



    }

    private void updateProfil(Utilisateur utilisateur) {

        txtnomuser.setText(utilisateur.getNom_complet());
        //txtnomartiste.setText(utilisateur.getUsername());

        RequestOptions glideOptions = new RequestOptions()
                .centerCrop()
                .transform(new CircleCrop())
                .error(R.drawable.ic_tizik_logo_svg)
                .placeholder(R.drawable.ic_tizik_logo_svg);

        Glide.with(ProfileActivity.this)
                .load(utilisateur.getUrl_photo())
                .apply(glideOptions)
                .transition(withCrossFade())
                .into(imgprofil);

    }

    private void initUI() {

        txtnomuser = findViewById(R.id.txtnomutilisateur);
       // txtnomartiste = (MyTextView_Ubuntu_Regular) findViewById(R.id.txtnomartiste);
        txtpost = findViewById(R.id.txtpost);
        txtcountpost = findViewById(R.id.txtcountpost);
        txtfollow = findViewById(R.id.txtfollow);
        txtcountfollow = findViewById(R.id.txtcountfollow);
        txtfollower = findViewById(R.id.txtfollower);
        txtcountfollower = findViewById(R.id.txtcountfollower);
        imgprofil = findViewById(R.id.imgprofil);
        recyclerViewprofil = findViewById(R.id.recyclerprofile);
        imglist = findViewById(R.id.imglist);
        imggrid = findViewById(R.id.imggrid);
        btnabonner = findViewById(R.id.btnabonner);
        avLoadingIndicatorView = findViewById(R.id.avi);
        emptystate = findViewById(R.id.emptyconst);
        linearLayoutabonnement = findViewById(R.id.linabonnement);
        linearLayoutabonnee = findViewById(R.id.linearabonne);



        btnabonner.setVisibility(View.GONE);

        startAnim();



        if(presentation.equals("list")){
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.colorAccent));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.placeholder_bg));


        }else{
            Drawable d = imglist.getDrawable();
            tintMyDrawable(d, getResources().getColor(R.color.placeholder_bg));

            Drawable dr = imggrid.getDrawable();
            tintMyDrawable(dr, getResources().getColor(R.color.colorAccent));

        }
    }


    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }


    @Override
    public void OnClick(Audio_Artiste produit) {

        Intent intent2 =  new Intent(ProfileActivity.this, NowPlayingActivity.class);
        intent2.putExtra(MUSIC_EXTRA, produit);
        intent2.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, null);
        startActivity(intent2);
    }

    @Override
    public void onUtilisateurClicked(int position) {
        //todo abonnement
    }

    @Override
    public void onAbonnementClicked(int position) {

        //todo abonnee
    }
}
