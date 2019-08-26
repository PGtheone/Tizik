package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Playlist_Audio;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MusicPlaylistAdapter extends FirestoreAdapter
        <MusicPlaylistAdapter.AllMusiqueViewHolder>{

    final AudioClickHandler productHandler;
    Glide glide;
    Context mContext;
    List<Audio_Artiste> audio_artistes;
    List<Audio_Artiste> audios;
    private CollectionReference audioRef =  FirebaseFirestore.
            getInstance()
            .collection("Audio");

    public MusicPlaylistAdapter(Context context, Query query, AudioClickHandler productHandler) {
        super(query);
        this.mContext = context;
        this.productHandler = productHandler;
        audios = new ArrayList<>();

    }

    @NonNull
    @Override
    public AllMusiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_allmuisque, parent, false);
        return new AllMusiqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllMusiqueViewHolder holder, int position) {

        Playlist_Audio playlist_audio = getSnapshot(position).toObject(Playlist_Audio.class);

        audioRef
                .whereEqualTo("id_musique", playlist_audio.getId_Audio())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            audio_artistes = queryDocumentSnapshots.toObjects(Audio_Artiste.class);

                            for(Audio_Artiste audio_artiste : audio_artistes){

                                if (audio_artiste != null){

                                    holder.bind(audio_artiste, productHandler);

                                }

                            }

                        }
                    }
                })
        ;

    }

    public interface AudioClickHandler{

        void OnClick(Audio_Artiste produit);

    }

    class AllMusiqueViewHolder extends RecyclerView.ViewHolder{


        private ImageView imgposter;
        private ImageView imgmore;
        private TextView txtTitreMusique;
        private TextView txtnomArtiste;
        // private TextView txtduree;


        public AllMusiqueViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = itemView.findViewById(R.id.imgmusique);
            imgmore = itemView.findViewById(R.id.imgmore);
            txtTitreMusique = itemView.findViewById(R.id.txtnommusique);
            txtnomArtiste = itemView.findViewById(R.id.txtnomartiste);
            //   txtduree = (TextView) itemView.findViewById(R.id.txtduree);

        }

        public void bind(final Audio_Artiste audio_artiste, final AudioClickHandler productHandlers) {


            txtTitreMusique.setText(audio_artiste.getTitre_musique());
            txtnomArtiste.setText(( audio_artiste).getNom_chanteur());
            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .error(R.color.cardview_dark_background)
                    .placeholder(android.R.drawable.stat_notify_error);

            Glide.with(mContext)
                    .load(audio_artiste.getUrl_poster())
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(imgposter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    productHandlers.OnClick(audio_artiste);
                }
            });




        }


    }

}
