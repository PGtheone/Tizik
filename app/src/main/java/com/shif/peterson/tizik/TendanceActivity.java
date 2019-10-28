package com.shif.peterson.tizik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shif.peterson.tizik.adapter.TendanceViewPagerAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.fragment.ArtisteTendanceFragment;
import com.shif.peterson.tizik.fragment.TendanceFragment;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.User_Tendance;
import com.shif.peterson.tizik.model.Utilisateur;
import com.shif.peterson.tizik.utilis.NonSwipeableViewPager;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.shif.peterson.tizik.utilis.UtilPreferences.saveUserTendance;

public class TendanceActivity extends AppCompatActivity implements
        TendanceFragment.OnTendanceChoosedListener,
        ArtisteTendanceFragment.OnArtistChoosenListener {

    private static final String COLLECTION_NAME_USERTENDANCE = "UserTendance";
    private static final String COLLECTION_NAME_USERFan= "Abonnement";
    MyTextView_Ubuntu_Bold txthead;
    MyTextView_Ubuntu_Regular txtstep;
    MyTextView_Ubuntu_Regular txtnext;
    NonSwipeableViewPager viewpager;
    TendanceViewPagerAdapter viewPagerAdapter;
    ImageView imgnext;
    List<Categorie> selectedGenre;
    List<Utilisateur> selectedArtiste;
    AVLoadingIndicatorView avl;
    FirebaseFirestore mFirestore;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;

    public static CollectionReference getUserTendanceCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME_USERTENDANCE);
    }

    public static CollectionReference getUserFanCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME_USERFan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_tendance);

        mAuth = FirebaseAuth.getInstance();

        txthead = findViewById(R.id.txtlike);
        txtstep = findViewById(R.id.txtstep);
        txtnext = findViewById(R.id.txtnext);
        viewpager = findViewById(R.id.viewpager);
        imgnext = findViewById(R.id.imgnext);

        avl = findViewById(R.id.avi);


        viewPagerAdapter = new TendanceViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewPagerAdapter);

        selectedGenre = new ArrayList<>();
        selectedArtiste = new ArrayList<>();


        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = viewpager.getCurrentItem();
                if(position == 0){

                    if(selectedGenre != null && selectedGenre.size() >= 4){

                        txtnext.setText(getString(R.string.finish));
                        txtstep.setText(getString(R.string.step2_of_2));
                        viewpager.setCurrentItem(1);

                    }

                }else{

                    if(selectedArtiste != null && selectedArtiste.size() >= 4){

                        avl.setVisibility(View.VISIBLE);
                         firebaseUser = mAuth.getCurrentUser();
                         for(Categorie categorie : selectedGenre){

                             Date now =  new Date();
                             SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                             String date =  formatter.format(now);
                             User_Tendance user_tendance = new User_Tendance(UUID.randomUUID().toString(), firebaseUser.getUid(), categorie.getId_categorie(), "true", date);
                             getUserTendanceCollectionReference().add(user_tendance).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                 @Override
                                 public void onSuccess(DocumentReference documentReference) {


                                     saveUserTendance(getApplicationContext(), selectedGenre);
                                 }
                             });
                         }

                         for (Utilisateur utilisateur : selectedArtiste){

                             Date now =  new Date();
                             SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                             String date =  formatter.format(now);
                             Abonnement abonnement = new Abonnement(
                                     UUID.randomUUID().toString()
                                     , utilisateur.getId_utilisateur()
                                     , firebaseUser.getUid()
                                     , true
                                     , firebaseUser.getUid()
                                     , date,
                                     null );
                             getUserFanCollectionReference().add(abonnement);

                         }

                        avl.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(TendanceActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                }

            }

        });


    }

    @Override
    public void onTendanceChoose(Categorie categorie, boolean choosen) {

        if(choosen == true){

            selectedGenre.add(categorie);

        }else{

            selectedGenre.remove(categorie);

        }


    }


    @Override
    public void onArtisteChoosen(Utilisateur utilisateur, boolean choosen) {

        if(choosen == true){

            selectedArtiste.add(utilisateur);

        }else{

            selectedArtiste.remove(utilisateur);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
