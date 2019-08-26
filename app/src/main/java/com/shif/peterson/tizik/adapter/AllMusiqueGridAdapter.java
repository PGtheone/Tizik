package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie_Audio;
import com.shif.peterson.tizik.utilis.RoundishImageView;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AllMusiqueGridAdapter extends FirestoreAdapter<AllMusiqueGridAdapter.AllMusiqueGridViewHolder>{

    final AudioClickHandler productHandler;
    List<Audio_Artiste> audio_artistes;
    RequestOptions glideOptions;
    private CollectionReference audioRef =  FirebaseFirestore.
            getInstance()
            .collection("Audio");
    private Context mContext;

    public AllMusiqueGridAdapter(Context mcontext, Query query, AudioClickHandler productHandler) {
        super(query);
        this.mContext = mcontext;
        this.productHandler = productHandler;


    }

    @NonNull
    @Override
    public AllMusiqueGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_recycler, null);
        AllMusiqueGridViewHolder mh = new AllMusiqueGridViewHolder(v);
        return mh;
    }



//    @Override
//    public int getItemCount() {
//        return  (null != itemsList ? itemsList.size() : 0);
//    }

    @Override
    public void onBindViewHolder(@NonNull final AllMusiqueGridViewHolder holder, int position) {

        Categorie_Audio categorie_audio = getSnapshot(position).toObject(Categorie_Audio.class);


        audioRef
                .whereEqualTo("id_musique", categorie_audio.getId_Audio())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    audio_artistes = queryDocumentSnapshots.toObjects(Audio_Artiste.class);

                    for (Audio_Artiste singleItem : audio_artistes) {

                        holder.txtnomproduit.setText(singleItem.getTitre_musique());
                        holder.txtalbum.setText(singleItem.getId_album());
                        if (singleItem.getPrix() == 0){

                            holder.txtprix.setText(mContext.getString(R.string.gratuit));
                        }else {

                            holder.txtprix.setText(String.valueOf(singleItem.getPrix()));
                        }

                        glideOptions = new RequestOptions()
                                .centerCrop()
                                .error(R.drawable.ic_placeholder_headset)
                                .placeholder(R.drawable.ic_placeholder_headset);


                        if (singleItem.getUrl_poster() != null){

                            String url = singleItem.getUrl_poster();

                            Glide.with(mContext)
                                    .load(url)
                                    .apply(glideOptions)
                                    .transition(withCrossFade())
                                    .into(holder.imgproduit);
                        }else{


//            String url = singleItem.getPhoto_produits().get(0).getUrlPhoto();
//
//            Glide.with(mContext)
//                    .load(url)
//                    .apply(glideOptions)
//                    .transition(withCrossFade())
//                    .into(holder.imgproduit);
                        }

                    }




                }
            }
        });




    }

    public interface AudioClickHandler{

        void OnClick(Audio_Artiste produit);

    }

//    public AllMusiqueGridAdapter( Context mContext, List<Audio_Artiste> itemsList,AudioClickHandler productHandler) {
//        this.itemsList = itemsList;
//        this.mContext = mContext;
//        this.productHandler = productHandler;
//    }

    public class AllMusiqueGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected RoundishImageView imgproduit;
        protected MyTextView_Ubuntu_Bold txtnomproduit;
        protected MyTextView_Ubuntu_Regular txtalbum;
        protected MyTextView_Ubuntu_Regular txtprix;


        public AllMusiqueGridViewHolder(View itemView) {
            super(itemView);

            imgproduit = itemView.findViewById(R.id.imgproduct);
            txtnomproduit = itemView.findViewById(R.id.txtnomproduct);
            txtalbum = itemView.findViewById(R.id.txtnomalbum);
            txtprix = itemView.findViewById(R.id.txtprixproduct);


            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            productHandler.OnClick(audio_artistes.get(getAdapterPosition()));

        }
    }

}
