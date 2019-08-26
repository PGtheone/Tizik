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
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TopGridAdapter extends RecyclerView.Adapter<TopGridAdapter.TopGridViewHolder> {

    private final TopgridClickListener onclickhandler;
    private Context mContext;
    private List<Audio_Artiste> audioArtisteList;

    public TopGridAdapter(Context mContext, List<Audio_Artiste> audioArtisteList, TopgridClickListener onclickhandler) {
        this.mContext = mContext;
        this.audioArtisteList = audioArtisteList;
        this.onclickhandler = onclickhandler;
    }

    @NonNull
    @Override
    public TopGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_top_music, parent, false);
        return new TopGridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopGridViewHolder holder, int position) {

        Audio_Artiste audio_artiste = audioArtisteList.get(position);
        if (audio_artiste != null) {

            if ( null != (audio_artiste).getNom_chanteur() ) {
                holder.txtartiste.setText((audio_artiste).getNom_chanteur());
            } else {
                holder.txtartiste.setText(R.string.exo_track_unknown);
            }
            holder.txtnomMusique.setText(audio_artiste.getTitre_musique());
            if (audio_artiste.getUrl_poster() != null) {

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

            holder.txtcount.setText(String.valueOf(position+1));
        }

        }

    @Override
    public int getItemCount() {
        return (null != audioArtisteList ? audioArtisteList.size() : 0);
    }


    public interface TopgridClickListener{

        void onClick(Audio_Artiste audio_artiste);
    }

    public class TopGridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgposter;
        TextView txtnomMusique;
        TextView txtartiste;
        TextView txtcount;


        public TopGridViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = itemView.findViewById(R.id.imggalerie);
            txtartiste = itemView.findViewById(R.id.txtartiste);
            txtnomMusique = itemView.findViewById(R.id.txttitle);
            txtcount = itemView.findViewById(R.id.txtcount);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onclickhandler.onClick(audioArtisteList.get(getAdapterPosition()));

        }
    }
}
