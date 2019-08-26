package com.shif.peterson.tizik.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Commentaire_Audio;
import com.shif.peterson.tizik.model.Utilisateur;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CommentAdapter extends FirestoreAdapter<CommentAdapter.commentViewHolder>{

    private static final String COLLECTION_NAME = "Utilisateur";


    public static CollectionReference getUtilisateurCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public CommentAdapter(Query query) {
        super(query);


    }

    @NonNull
    @Override
    public commentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new commentViewHolder(inflater.inflate(R.layout.item_comment, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final commentViewHolder holder, int i) {

        final Commentaire_Audio commentaire_audio = getSnapshot(i).toObject(Commentaire_Audio.class);


        getUtilisateurCollectionReference().whereEqualTo("id_utilisateur", commentaire_audio.getId_utilisateur())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(null != queryDocumentSnapshots){

                    List<Utilisateur> utilisateurs1 = queryDocumentSnapshots.toObjects(Utilisateur.class);
                    for(Utilisateur user : utilisateurs1){

                        Log.d("TAG", "Utilisateur"+user.getUrl_photo());
                        holder.txtnomuser.setText(user.getNom_complet());
                        if(null != user.getUrl_photo()){


                                RequestOptions glideCircle = new RequestOptions()
                                        .centerCrop()
                                        .transform( new CircleCrop())
                                        .error(R.drawable.circle_img_bg)
                                        .placeholder(R.drawable.circle_img_bg);

                                Glide.with(holder.imguser.getContext())
                                        .load(user.getUrl_photo())
                                        .apply(glideCircle)
                                        .transition(withCrossFade())
                                        .into(holder.imguser);




        }


                    }
                }

            }
        });



//
//
//        getUtilisateurCollectionReference().whereEqualTo("id_utilisateur", commentaire_audio.getId_utilisateur()).
//                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                if (null != queryDocumentSnapshots && !queryDocumentSnapshots.isEmpty()){
//
//                    Toast.makeText(getContext(), "heree", Toast.LENGTH_SHORT).show();
//                    List<Utilisateur> utilisateurs1 = queryDocumentSnapshots.toObjects(Utilisateur.class);
//                    for(Utilisateur user : utilisateurs1){
//
//                        utilisateurs.add(user);
//                    }
//
//                }
//
//
//
//            }
//
//        });




            holder.txtcomment.setText(commentaire_audio.getCommentaire());
            holder.appCompatRatingBar.setRating(commentaire_audio.getNote());
            holder.txtdatecomment.setText(commentaire_audio.getDate_created());


//


    }

    public class commentViewHolder extends RecyclerView.ViewHolder {

        private TextView txtnomuser;
        private TextView txtcomment;
        private TextView txtdatecomment;
        private AppCompatRatingBar appCompatRatingBar;
        private ImageView imguser;

        public commentViewHolder(@NonNull View itemView) {
            super(itemView);

            txtnomuser = itemView.findViewById(R.id.txtnomuser);
            txtdatecomment = itemView.findViewById(R.id.txtdatecomment);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            imguser = itemView.findViewById(R.id.imguser);
            appCompatRatingBar = itemView.findViewById(R.id.rate);

        }
    }
}
