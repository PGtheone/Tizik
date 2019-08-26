package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.text.format.DateFormat;
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
import com.shif.peterson.tizik.model.Categorie_Audio;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class AllMusiqueAdapter extends FirestoreAdapter<AllMusiqueAdapter.AllMusiqueViewHolder> {

    final AudioClickHandler productHandler;
    Glide glide;
    Context mContext;
    List<Audio_Artiste> audio_artistes;
    private CollectionReference audioRef =  FirebaseFirestore.
            getInstance()
            .collection("Audio");

    public AllMusiqueAdapter(Context context, Query query, AudioClickHandler productHandler) {
        super(query);
        this.mContext = context;
        this.productHandler = productHandler;


    }

    @NonNull
    @Override
    public AllMusiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_allmuisque, parent, false);
        return new AllMusiqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllMusiqueViewHolder holder, int position) {

        Categorie_Audio categorie_audio = getSnapshot(position).toObject(Categorie_Audio.class);


        audioRef
                .whereEqualTo("id_musique", categorie_audio.getId_Audio())
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
        private TextView txtduree;


        public AllMusiqueViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = itemView.findViewById(R.id.imgmusique);
            imgmore = itemView.findViewById(R.id.imgmore);
            txtTitreMusique = itemView.findViewById(R.id.txtnommusique);
            txtnomArtiste = itemView.findViewById(R.id.txtnomartiste);
            txtduree = itemView.findViewById(R.id.txtduree);

//            imgmore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    PopupMenu popupMenu = new PopupMenu(mContext, imgmore);
//                    popupMenu.getMenuInflater().inflate(R.menu.list_selected_musique_menu, popupMenu.getMenu());
//
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//
//                            int id = item.getItemId();
//
//                            switch (id){
//
//                                case R.id.nav_delete:
//
//                                    Audio_Artiste audio_artiste = audio_artisteList.get(getAdapterPosition());
//                                    audio_artisteList.remove(audio_artiste);
//                                    audioDeleteHandler.onAudioDeleted(getAdapterPosition());
//                                    notifyItemRemoved(getAdapterPosition());
//
//
//                                    return true;
//                                default:
//                                    return false;
//
//                            }
//
//                        }
//                    });
//
//                    popupMenu.show();
//
//                }
//            });


        }


        public void bind(final Audio_Artiste audio_artiste, final AudioClickHandler productHandlers) {


            txtTitreMusique.setText(audio_artiste.getTitre_musique());
            if (( audio_artiste).getNom_chanteur() != null && !( audio_artiste).getNom_chanteur().isEmpty()){
                txtnomArtiste.setText(( audio_artiste).getNom_chanteur());
            }else{
                txtnomArtiste.setText(R.string.exo_track_unknown);
            }


            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis((long)audio_artiste.getDuree_musique());
            String date = DateFormat.format("mm:ss", cal).toString();
            txtduree.setText(date);


            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_tizik_logo_svg_gray)
                    .placeholder(R.drawable.ic_tizik_logo_svg_gray);


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
