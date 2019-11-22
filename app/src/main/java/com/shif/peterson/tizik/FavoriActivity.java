package com.shif.peterson.tizik;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.shif.peterson.tizik.adapter.FavoriAdapter;
import com.shif.peterson.tizik.model.Favori;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class FavoriActivity extends AppCompatActivity implements FavoriAdapter.AudioDeleteHandler {


    private final String EXTRA_ID_UTILISATEUR = "extra_id_utilisateur";
    FirebaseFirestore mFirestore;
    Query mQuery;
    LinearLayout linearLayout;
    View view;
    int favoriSize = 0;
    private RecyclerView recyclerfav;
    private ImageView imgstate;
    private TextView txtheadstate;
    private TextView txtdesc;
    private AVLoadingIndicatorView avi;
    private List<Favori> favori_audios;
    private FavoriAdapter favoriAdapter;
    private String mid_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favori);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scrollview)
                    .setBackgroundResource(R.drawable.bg_top_radius);
        }


        if(getIntent().hasExtra(EXTRA_ID_UTILISATEUR)){

            mid_user = getIntent().getStringExtra(EXTRA_ID_UTILISATEUR);
            initFirestore(mid_user);
        }

        initUI();
        initRecyclerView();




    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        favoriAdapter = new FavoriAdapter(this, mQuery,this);

        favoriAdapter = new FavoriAdapter(FavoriActivity.this,mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

                Log.i("TAG", " RecyclerView");

                if (getItemCount() == 0) {
                    Log.i("TAG", "empty RecyclerView");

                    linearLayout.setVisibility(View.GONE);
                    imgstate.setVisibility(View.VISIBLE);
                    txtheadstate.setVisibility(View.VISIBLE);
                    txtdesc.setVisibility(View.VISIBLE);
                    recyclerfav.setVisibility(View.GONE);

                } else {

                    Log.i("TAG", " RecyclerView");
                    favoriSize = getItemCount();

                    linearLayout.setVisibility(View.GONE);
                    imgstate.setVisibility(View.GONE);
                    txtheadstate.setVisibility(View.GONE);
                    txtdesc.setVisibility(View.GONE);
                    recyclerfav.setVisibility(View.VISIBLE);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FavoriActivity.this, RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerfav.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerfav.addItemDecoration(itemDecor);
        recyclerfav.setAdapter(favoriAdapter);
        recyclerfav.setNestedScrollingEnabled(false);
    }

    private void initUI() {


        favori_audios = new ArrayList<>();

        recyclerfav = findViewById(R.id.recyclerfav);
        avi = findViewById(R.id.avi);
        linearLayout = findViewById(R.id.linearprogress);
        txtheadstate = findViewById(R.id.txthead);
        txtdesc = findViewById(R.id.txtemptycomment);
        imgstate = findViewById(R.id.imgemptystate);


        linearLayout.setVisibility(View.VISIBLE);
        imgstate.setVisibility(View.GONE);
        txtheadstate.setVisibility(View.GONE);
        txtdesc.setVisibility(View.GONE);
        recyclerfav.setVisibility(View.GONE);

    }

    private void initFirestore(String idUser) {

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Favori")
                .whereEqualTo("id_utilisateur",idUser);

    }

    @Override
    public void onAudioDeleted(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (favoriAdapter != null) {
            favoriAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (favoriAdapter != null) {
            favoriAdapter.stopListening();
        }
    }
}
