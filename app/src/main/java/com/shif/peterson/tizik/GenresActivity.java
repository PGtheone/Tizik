package com.shif.peterson.tizik;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.adapter.GenreAdapter;
import com.shif.peterson.tizik.model.Categorie;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import static com.shif.peterson.tizik.AllMusicActivity.EXTRA_CATEGORIE;
import static com.shif.peterson.tizik.AllMusicActivity.EXTRA_CATEGORIE_NAME;

public class GenresActivity extends AppCompatActivity {

    RecyclerView genreRecyclerview;
    AVLoadingIndicatorView avi;
    ConstraintLayout constraintLayout;
    List<Categorie> categorieList;
    GenreAdapter genreAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            findViewById(R.id.scrollview)
                    .setBackgroundResource(R.drawable.bg_top_radius);
        }


        genreRecyclerview = findViewById(R.id.recyclergenres);
        avi = findViewById(R.id.avi);
        constraintLayout = findViewById(R.id.constraintLayout);

        constraintLayout.setVisibility(View.GONE);
        avi.setVisibility(View.GONE);

        FirebaseFirestore.getInstance().collection(Categorie.class.getSimpleName()).orderBy("nom_categorie", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){

                    categorieList = queryDocumentSnapshots.toObjects(Categorie.class);

                    categorieList = queryDocumentSnapshots.toObjects(Categorie.class);
                    genreAdapter = new GenreAdapter(GenresActivity.this, categorieList, new GenreAdapter.CategorieClickHandler() {
                        @Override
                        public void OnCategorieClickListener(Categorie categorie) {

                            Intent intent = new Intent(GenresActivity.this, AllMusicActivity.class);
                            intent.putExtra(EXTRA_CATEGORIE, categorie.getId_categorie());
                            intent.putExtra(EXTRA_CATEGORIE_NAME, categorie.getNom_categorie());

                            startActivity(intent);

                        }
                    });

                    GridLayoutManager glm = new GridLayoutManager(GenresActivity.this, GenresActivity.this.getResources().getInteger(R.integer.shr_column_count));
                    genreRecyclerview.setLayoutManager(glm);
                    genreRecyclerview.setAdapter(genreAdapter);

                    constraintLayout.setVisibility(View.VISIBLE);
                    avi.setVisibility(View.GONE);

                }
            }
        });
    }


}
