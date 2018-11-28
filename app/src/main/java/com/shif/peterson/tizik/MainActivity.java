package com.shif.peterson.tizik;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shif.peterson.tizik.fragment.HomeFragment;
import com.shif.peterson.tizik.fragment.MainSignInDialogFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment detailFragment = HomeFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailFragment, "home container")
                .commit();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        } else if (id == R.id.nav_genre) {

        } else if (id == R.id.nav_favori) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        } else if (id == R.id.nav_playlist) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_upload) {




            if (mAuth.getInstance().getCurrentUser() != null){

                Intent intent = new Intent(MainActivity.this, UploadMusicActivity.class);
                intent.putExtra(EXTRA_ID_UTILISATEUR, mAuth.getCurrentUser().getUid());
                startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_notification) {

            if (mAuth.getInstance().getCurrentUser() != null){

                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                //startActivity(intent);

            }else{


                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignIn");
            }

        }else if (id == R.id.nav_reglages) {

        }else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
