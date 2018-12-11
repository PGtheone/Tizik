package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.media.audiofx.Visualizer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chibde.visualizer.BarVisualizer;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SimilarAdater extends RecyclerView.Adapter<SimilarAdater.SimilarViewHolder>{


    private Context mContext;
    private List<Audio_Artiste> audioArtisteList;
    private final SimiClickListener removeFromListListener;


    public SimilarAdater(Context mContext, List<Audio_Artiste> audioArtisteList, SimiClickListener removeFromListListener) {
        this.mContext = mContext;
        this.audioArtisteList = audioArtisteList;
        this.removeFromListListener = removeFromListListener;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_similar, viewGroup, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int i) {

        Audio_Artiste audio_artiste = audioArtisteList.get(i);
        if (audio_artiste != null){

            holder.txtartiste.setText(audio_artiste.getId_album());
            holder.txtnomMusique.setText(audio_artiste.getTitre_musique());
            if (audio_artiste.getUrl_poster() != null){

                RequestOptions glideOptions = new RequestOptions()
                        .centerCrop()
                        .error(R.color.cardview_dark_background)
                        .placeholder(android.R.drawable.stat_notify_error);


                Glide.with(mContext)
                        .load(audio_artiste.getUrl_poster())
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(holder.imgposter);

            }

           if (audio_artiste.isSelected()){

                holder.visualizer.setVisibility(View.VISIBLE);
           }


        }

    }

    @Override
    public int getItemCount() {

        return (null != audioArtisteList ? audioArtisteList.size() : 0);
    }

    public interface SimiClickListener{


        public void onClick( int position);
    }
    public class SimilarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView imgposter;
         ImageView imgmore;
         TextView txtnomMusique;
         TextView txtartiste;
         BarVisualizer visualizer;


        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = (ImageView) itemView.findViewById(R.id.imgmusic);
            imgmore = (ImageView) itemView.findViewById(R.id.imgmore);
            txtartiste = (TextView) itemView.findViewById(R.id.song_artist);
            txtnomMusique = (TextView) itemView.findViewById(R.id.song_title);
            visualizer = (BarVisualizer) itemView.findViewById(R.id.visualizer);

            itemView.setOnClickListener(this);

            imgmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mContext, imgmore);
                    popupMenu.getMenuInflater().inflate(R.menu.similar_list_sub_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            int id = item.getItemId();

                            switch (id){

                                case R.id.nav_delete:


                                  break;
                                case R.id.nav_favori:

                                    //todo add to favorite;


                                    break;
                                default:
                                    return false;

                            }

                            return true;
                        }


                    });

                    popupMenu.show();

                }
            });
        }

        @Override
        public void onClick(View v) {
            removeFromListListener.onClick(getAdapterPosition());

        }
    }
}
