package com.shif.peterson.tizik;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Medium;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Utilisateur;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

     static final String ID_USER_EXTRA = "ID_USER_EXTRA";
    List<Audio_Artiste> audio_artistes;
    private MyTextView_Ubuntu_Medium txtnomuser;
    private MyTextView_Ubuntu_Regular txtnomartiste;
    private MyTextView_Ubuntu_Regular txtpost;
    private MyTextView_Ubuntu_Bold txtcountpost;
    private MyTextView_Ubuntu_Regular txtfollower;
    private MyTextView_Ubuntu_Bold txtcountfollower;
    private MyTextView_Ubuntu_Regular txtfollow;
    private MyTextView_Ubuntu_Bold txtcountfollow;
    private ImageView imgprofil;
    private RecyclerView recyclerViewprofil;
    private Utilisateur utilisateur;
    private String idUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        txtnomuser = (MyTextView_Ubuntu_Medium) findViewById(R.id.txtnomutilisateur);
//        txtnomartiste = (MyTextView_Ubuntu_Regular) findViewById(R.id.txtnomartiste);
//        txtpost = (MyTextView_Ubuntu_Regular) findViewById(R.id.txtpost);
//        txtcountpost = (MyTextView_Ubuntu_Bold) findViewById(R.id.txtcountpost);
//        txtfollow = (MyTextView_Ubuntu_Regular) findViewById(R.id.txtfollow);
//        txtcountfollow = (MyTextView_Ubuntu_Bold) findViewById(R.id.txtcountfollow);
//        txtfollower = (MyTextView_Ubuntu_Regular) findViewById(R.id.txtfollower);
//        txtcountfollower = (MyTextView_Ubuntu_Bold) findViewById(R.id.txtcountfollower);
//        imgprofil = (ImageView) findViewById(R.id.imgprofil);
//        recyclerViewprofil = (RecyclerView) findViewById(R.id.recyclerprofile);

//        if (getIntent().hasExtra(ID_USER_EXTRA)){
//
//            idUtilisateur = getIntent().getParcelableExtra(ID_USER_EXTRA);
//        }

       // retrieveData();

        //initUI();

    }

    private void retrieveData() {

        Task taskUtilisateur = FirebaseFirestore
                .getInstance()
                .collection("Utilisateur")
                .get();


        Task taskAbonne = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        Task taskAbonnement = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();


        Task taskAudioPlayded = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        Task taskAudioUploaded = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
                .limit(10)
                .get();

        Task taskPlaylist = FirebaseFirestore
                .getInstance()
                .collection("Abonnement")
                .whereEqualTo("prix", 0)
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
                        taskAudioPlayded,
                        taskAudioUploaded,
                        taskPlaylist,
                        taskFavori);

        allUserTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

            }
        });


    }

    private void initUI() {
    }
}
