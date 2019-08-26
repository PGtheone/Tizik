package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Utilisateur;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.ConnectionViewHolder> {


    private static final String COLLECTION_NAME = "Utilisateur";
    public Context mContext;
    List<Abonnement> abonnementList;

    public ConnectionAdapter(Context mContext,List<Abonnement> abonnementList) {
        this.abonnementList = abonnementList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_connection, parent, false);
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConnectionViewHolder holder, int position) {

        final Abonnement abonnement = abonnementList.get(position);
        FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .whereEqualTo("id_utilisateur", abonnement.getId_utilisateur())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(null != queryDocumentSnapshots){

                            final List<Utilisateur> abonnements = queryDocumentSnapshots.toObjects(Utilisateur.class);
                            for ( Utilisateur utilisateur: abonnements) {


                                RequestOptions glideOptions = new RequestOptions()
                                        .centerCrop()
                                        .error(R.drawable.ic_tizik_logo_svg)
                                        .placeholder(R.drawable.ic_tizik_logo_svg)
                                        .transforms(new CircleCrop());


                                Glide.with(mContext)
                                        .load(utilisateur.getUrl_photo())
                                        .apply(glideOptions)
                                        .transition(withCrossFade())
                                        .into(holder.imground);
                            }


                        }

                    }
                });

    }

    @Override
    public int getItemCount() {
        return abonnementList.size();
    }

    public class ConnectionViewHolder extends RecyclerView.ViewHolder{

        public ImageView imground;

        public ConnectionViewHolder(@NonNull View itemView) {
            super(itemView);

            imground = itemView.findViewById(R.id.imground);
        }
    }
}
