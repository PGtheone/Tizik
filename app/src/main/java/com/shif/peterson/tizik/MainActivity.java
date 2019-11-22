package com.shif.peterson.tizik;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.MenuAdapter;
import com.shif.peterson.tizik.adapter.PlaylistAdapter;
import com.shif.peterson.tizik.adapter.RecyclerDataAdapter;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.fragment.HomeFragment;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.Playlist;
import com.shif.peterson.tizik.model.SectionDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.shif.peterson.tizik.DetailPlaylistActivity.EXTRA_PLAYLIST;
import static com.shif.peterson.tizik.ProfileActivity.ID_USER_EXTRA;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_SIMILAR_KEY;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private static final String MUSIC_EXTRA = "music_extra";
    private final ArrayList<SectionDataModel> allMusic = new ArrayList<SectionDataModel>();


    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";

    private Handler mHandler;
    private Runnable mRunnable;
    boolean isPlaying;
    private final List<Categorie> allcategories = new ArrayList<>();
    private final List<Abonnement> allAbonnements = new ArrayList<>();
    private final List<Playlist> allPlaylist = new ArrayList<>();
    private final List<Audio_Artiste> allTop = new ArrayList<>();


    ImageView imgprofile;
    MyTextView_Ubuntu_Bold txtNom;
    MyTextView_Ubuntu_Regular txtusername;
    View headerView;
    DuoDrawerLayout drawer;
    DuoMenuView menuView;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private ArrayList<String> mTitles = new ArrayList<>();
    private androidx.appcompat.widget.Toolbar toolbbar;
    private MenuAdapter mMenuAdapter;
    private ArrayList<Object> mainObject = new ArrayList<>();
    private HomeFragment.NowPlayingHandler nowPlayingHandler;
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView listProduitRecyclerview;
    private RecyclerDataAdapter dataAdapter;


    private final static int ALL_PERMISSIONS_RESULT = 107;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbbar);

        FirebaseApp.initializeApp(this);

        initUI();


        mAuth = FirebaseAuth.getInstance();
        mHandler = new Handler();


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = mAuth.getCurrentUser();
                if(firebaseUser == null){

                    Intent intent = new Intent(MainActivity.this, MainSignInActivity.class);
                    startActivity(intent);


                }else{


                }
            }
        };

        initList();
        requestPermission();

    }

    private void requestPermission() {

        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(CAMERA);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scrollview)
                    .setBackgroundResource(R.drawable.bg_top_radius);
        }

        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.main_nav)));
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        listProduitRecyclerview = findViewById(R.id.listaudiorecycler);

        listProduitRecyclerview.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        //getWindow().setStatusBarColor(getResources().getColor(R.color.white));

        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        //toolbbar.setTitle("title");

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        toolbbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initList() {


        //Playlist
        Task taskPlaylist = FirebaseFirestore
                .getInstance()
                .collection("Playlist")
                .limit(10)
                .get();


        //task top
        Task taskTop = FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .orderBy("date_upload", Query.Direction.DESCENDING)
                .limit(9)
                .get();

        Task taskabonnement  =  FirebaseFirestore.getInstance()
                .collection("Abonnement")
                .whereEqualTo("id_fan", "pAOCKyv8dBgmryBX5qC7C25H2Zx1")
                .limit(5)
                .get();



        Task tasknew =   FirebaseFirestore
                .getInstance()
                .collection("Audio")
                .orderBy("date_upload", Query.Direction.DESCENDING)
                .limit(10)
                .get();



        Task taskCategorie =    FirebaseFirestore.getInstance()
                .collection(Categorie.class.getSimpleName())
                .orderBy("nom_categorie", Query.Direction.ASCENDING)
                .limit(6)
                .get();



        final Task<List<QuerySnapshot>> allMainTask = Tasks.whenAllSuccess(taskPlaylist,taskTop, taskabonnement, tasknew, taskCategorie);
        allMainTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

                for(int i = 0; i < querySnapshots.size(); i++){
                    QuerySnapshot queryDocumentSnapshots =  querySnapshots.get(i);
                    switch (i){
                        case 0:

                            if (!queryDocumentSnapshots.isEmpty()){

                                allPlaylist.addAll(queryDocumentSnapshots.toObjects(Playlist.class));
                                mainObject.add( allPlaylist);


                            }

                            break;

                        case 1:

                            if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null){

                                allTop.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                                mainObject.add( allTop);


                            }

                            break;

                        case 2:

                            if (!queryDocumentSnapshots.isEmpty()){


                                allAbonnements.addAll(queryDocumentSnapshots.toObjects(Abonnement.class));
                                mainObject.add( allAbonnements);
                                Log.d("TAG ABON", allAbonnements.toString()+" "+allAbonnements.size());

                            }else{

                                Log.d("TAG ABON", "PFFFF");

                            }

                            break;

                        case 3:

                            if(!queryDocumentSnapshots.isEmpty()){

                                SectionDataModel dm = new SectionDataModel();
                                dm.setHeaderTitle(getString(R.string.gratuit));
                                ArrayList<Audio_Artiste> singleItem = new ArrayList<Audio_Artiste>();

                                singleItem.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
                                dm.setAllItemsInSection(singleItem);

                                allMusic.add(dm);
                                mainObject.add( allMusic);
                            }

                            break;

                        case 4:

                            if (!queryDocumentSnapshots.isEmpty()){


                                allcategories.addAll(queryDocumentSnapshots.toObjects(Categorie.class));
                                mainObject.add( allcategories);

                            }
                            break;



                    }









//                    if (i == 0){
//
//                        if (!queryDocumentSnapshots.isEmpty()){
//
//                            allPlaylist.addAll(queryDocumentSnapshots.toObjects(Playlist.class));
//                            mainObject.add(0, allPlaylist);
//
//
//                        }
//                    }else if (i == 1){
//
//                        if (!queryDocumentSnapshots.isEmpty() && queryDocumentSnapshots != null){
//
//                            allTop.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
//                            mainObject.add(1, allTop);
//
//
//                        }
//                    } else if(i == 2){
//
//                        if (!queryDocumentSnapshots.isEmpty()){
//
//
//                            allAbonnements.addAll(queryDocumentSnapshots.toObjects(Abonnement.class));
//                            mainObject.add(2, allAbonnements);
//
//                        }
//                    }else if(i == 4){
//
//                        if (!queryDocumentSnapshots.isEmpty()){
//
//
//                            allcategories.addAll(queryDocumentSnapshots.toObjects(Categorie.class));
//                            mainObject.add(4, allcategories);
//
//                        }
//                    } else if(i == 3){
//
//                        if(!queryDocumentSnapshots.isEmpty()){
//
//                            SectionDataModel dm = new SectionDataModel();
//                            dm.setHeaderTitle(getString(R.string.gratuit));
//                            ArrayList<Audio_Artiste> singleItem = new ArrayList<Audio_Artiste>();
//
//                            singleItem.addAll(queryDocumentSnapshots.toObjects(Audio_Artiste.class));
//                            dm.setAllItemsInSection(singleItem);
//
//                            allMusic.add(dm);
//                            mainObject.add(3, allMusic);
//                        }
//
//
//                    }
                }


                dataAdapter = new RecyclerDataAdapter(MainActivity.this, mainObject, new RecyclerDataAdapter.ProductHandler() {
                    @Override
                    public void onClick(Audio_Artiste audio) {
                        Intent intent2 =  new Intent(MainActivity.this, NowPlayingActivity.class);
                        intent2.putExtra(MUSIC_EXTRA, audio);
                        intent2.putParcelableArrayListExtra(MUSIC_SIMILAR_KEY, null);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent2);

                    }
                }, new PlaylistAdapter.PlaylistHandler() {
                    @Override
                    public void onPlaylistClick(Playlist position) {

                        Intent intent2 =  new Intent(MainActivity.this, DetailPlaylistActivity.class);
                        intent2.putExtra(EXTRA_PLAYLIST, position);
                        startActivity(intent2);

                    }

                    @Override
                    public void onPlayClick(Playlist position) {

                    }
                });


                listProduitRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL, false));
                listProduitRecyclerview.setAdapter(dataAdapter);
                listProduitRecyclerview.setNestedScrollingEnabled(false);
                listProduitRecyclerview.getAdapter().notifyDataSetChanged();

                mShimmerViewContainer.setVisibility(View.GONE);
                listProduitRecyclerview.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            if (firebaseUser != null){
//
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(ID_USER_EXTRA, mAuth.getCurrentUser().getUid());
                    startActivity(intent);
//
                }else{
//
                 startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
               }


        } else if (id == R.id.nav_genre) {

            if (firebaseUser != null){
////
                    Intent intent = new Intent(MainActivity.this, GenresActivity.class);
                    startActivity(intent);
//
                }else{

                    startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
                }

        } else if (id == R.id.nav_upload) {


            if (firebaseUser != null){

                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                startActivity(intent);

            }else{

              startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
            }

        }else if (id == R.id.nav_favori) {

            if (firebaseUser != null){
////
                    Intent intent = new Intent(MainActivity.this, FavoriActivity.class);
                    intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                    startActivity(intent);

                }else{

                startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
                }
//

        }
//        else if (id == R.id.nav_playlist) {
//
//        }
        else if (id == R.id.nav_notification) {

        }else if (id == R.id.nav_reglages) {

        }else if (id == R.id.nav_logout) {

            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, MainSignInActivity.class));
            finish();

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


@TargetApi(Build.VERSION_CODES.M)
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    switch (requestCode) {

        case ALL_PERMISSIONS_RESULT:
            for (String perms : permissionsToRequest) {
                if (hasPermission(perms)) {

                } else {

                    permissionsRejected.add(perms);
                }
            }

            if (permissionsRejected.size() > 0) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    }
                                });
                        return;
                    }
                }

            }

            break;
    }

}

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }


    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
}
