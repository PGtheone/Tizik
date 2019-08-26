package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.shif.peterson.tizik.model.Favori;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FavoriAdapter extends FirestoreAdapter<FavoriAdapter.FavoriViewHolder>{

    private static final String COLLECTION_NAME = "Audio";
    final AudioDeleteHandler audioDeleteHandler;
    Glide glide;
    private List<Audio_Artiste> audio_artisteList;
    private Context mcontext;


    public FavoriAdapter(Context mcontext,Query query,  AudioDeleteHandler audioDeleteHandler) {
        super(query);
        this.mcontext = mcontext;
        this.audioDeleteHandler = audioDeleteHandler;


    }


//    public FavoriAdapter( Context mcontext, List<Audio_Artiste> audio_artisteList, AudioDeleteHandler audioDeleteHandler) {
//        this.audio_artisteList = audio_artisteList;
//        this.mcontext = mcontext;
//        this.audioDeleteHandler = audioDeleteHandler;
//    }

    public static CollectionReference getAudioCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    @NonNull
    @Override
    public FavoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_allmuisque, parent, false);
        return new FavoriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriViewHolder holder, int position) {


       final Favori favori = getSnapshot(position).toObject(Favori.class);

        getAudioCollectionReference().whereEqualTo("id_musique", favori.getId_media())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(null != queryDocumentSnapshots){

                    final List<Audio_Artiste> audio_artistes = queryDocumentSnapshots.toObjects(Audio_Artiste.class);

                    for(Audio_Artiste audio_artiste : audio_artistes){

                        if (audio_artiste != null){

                            if (audio_artiste.getUrl_poster()!=null){



                                RequestOptions glideOptions = new RequestOptions()
                                        .centerCrop()
                                        .error(R.color.cardview_dark_background)
                                        .placeholder(android.R.drawable.stat_notify_error);


                                Glide.with(mcontext)
                                        .load(audio_artiste.getUrl_poster())
                                        .apply(glideOptions)
                                        .transition(withCrossFade())
                                        .into(holder.imgposter);


                                holder.txtTitreMusique.setText(audio_artiste.getTitre_musique());

                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                cal.setTimeInMillis((long)audio_artiste.getDuree_musique());
                                String date = DateFormat.format("mm:ss", cal).toString();
                                holder.txtduree.setText(date);




                                if (!( audio_artiste).getNom_chanteur().isEmpty()){
                                    holder.txtnomArtiste.setText(( audio_artiste).getNom_chanteur());
                                }else{
                                    holder.txtnomArtiste.setText(R.string.exo_track_unknown);
                                }
                                //holder.txtnomArtiste.setText(audio_artiste.getNom_chanteur());

                            }
                        }


                    }
                }

            }
        });



    }

//    @Override
//    public int getItemCount() {
//        return (null != audio_artisteList ? audio_artisteList.size() : 0);
//    }


    public interface AudioDeleteHandler{

        void onAudioDeleted(int position);
    }


    public class FavoriViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        // private CheckBox musiquecheck;
        private ImageView imgposter;
        private ImageView imgmore;
        private TextView txtTitreMusique;
        private TextView txtnomArtiste;
        private TextView txtduree;





        public FavoriViewHolder(@NonNull View itemView) {
            super(itemView);

            //musiquecheck = (CheckBox) itemView.findViewById(R.id.checkbox);
            imgposter = itemView.findViewById(R.id.imgmusique);
            imgmore = itemView.findViewById(R.id.imgmore);
            txtTitreMusique = itemView.findViewById(R.id.txtnommusique);
            txtnomArtiste = itemView.findViewById(R.id.txtnomartiste);
            txtduree = itemView.findViewById(R.id.txtduree);




            imgmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mcontext, imgmore);
                    popupMenu.getMenuInflater().inflate(R.menu.list_selected_musique_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            int id = item.getItemId();

                            switch (id){

                                case R.id.nav_delete:

                                    Audio_Artiste audio_artiste = audio_artisteList.get(getAdapterPosition());
                                    audio_artisteList.remove(audio_artiste);
                                    audioDeleteHandler.onAudioDeleted(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());


                                    return true;
                                default:
                                    return false;

                            }

                        }
                    });

                    popupMenu.show();

                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
