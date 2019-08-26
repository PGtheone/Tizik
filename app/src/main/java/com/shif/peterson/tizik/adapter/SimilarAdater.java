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
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chibde.visualizer.BarVisualizer;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SimilarAdater extends RecyclerView.Adapter<SimilarAdater.SimilarViewHolder>{


    private Context mContext;
    private List<Audio_Artiste> audioArtisteList;
    private final SimiClickListener onclickhandler;


    public SimilarAdater(Context mContext, List<Audio_Artiste> audioArtisteList, SimiClickListener onclickhandler) {
        this.mContext = mContext;
        this.audioArtisteList = audioArtisteList;
        this.onclickhandler = onclickhandler;
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

            if (null != ( audio_artiste).getNom_chanteur()){

                if (!( audio_artiste).getNom_chanteur().isEmpty()){
                    holder.txtartiste.setText(( audio_artiste).getNom_chanteur());
                }else{

                    holder.txtartiste.setText(R.string.exo_track_unknown);
                }


            }else{
                holder.txtartiste.setText(R.string.exo_track_unknown);
            }
            holder.txtnomMusique.setText(audio_artiste.getTitre_musique());
            if (audio_artiste.getUrl_poster() != null){

                RequestOptions glideOptions = new RequestOptions()
                        .centerCrop()
                        .error(R.color.colorPrimary)
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(10)))
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


        void onClick(int position);
    }
    public class SimilarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         ImageView imgposter;
         TextView txtnomMusique;
         TextView txtartiste;
         BarVisualizer visualizer;


        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = itemView.findViewById(R.id.imgmusic);
            txtartiste = itemView.findViewById(R.id.song_artist);
            txtnomMusique = itemView.findViewById(R.id.song_title);
            visualizer = itemView.findViewById(R.id.visualizer);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onclickhandler.onClick(getAdapterPosition());

        }
    }
}
