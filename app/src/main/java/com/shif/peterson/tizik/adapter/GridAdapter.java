package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.RoundishImageView;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {


    final AudioClickHandler productHandler;
    List<Audio_Artiste> audio_artistes;
    RequestOptions glideOptions;
    private Context mContext;

    public GridAdapter(Context mcontext, List<Audio_Artiste> audio_artistes, AudioClickHandler productHandler) {
        this.mContext = mcontext;
        this.productHandler = productHandler;
        this.audio_artistes = audio_artistes;

    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, null);
        return new GridViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {

        Audio_Artiste audio_artiste = audio_artistes.get(position);

        if(null != audio_artiste){
            holder.txtnomproduit.setText(audio_artiste.getTitre_musique());
            holder.txtalbum.setText(audio_artiste.getId_album());
            if (audio_artiste.getPrix() == 0){

                holder.txtprix.setText(mContext.getString(R.string.gratuit));
            }else {

                holder.txtprix.setText(String.valueOf(audio_artiste.getPrix()));
            }

            glideOptions = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_placeholder_headset)
                    .placeholder(R.drawable.ic_placeholder_headset);


            if (audio_artiste.getUrl_poster() != null){

                String url = audio_artiste.getUrl_poster();

                Glide.with(mContext)
                        .load(url)
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(holder.imgproduit);
            }else{


//            String url = audio_artiste.getPhoto_produits().get(0).getUrlPhoto();
//
//            Glide.with(mContext)
//                    .load(url)
//                    .apply(glideOptions)
//                    .transition(withCrossFade())
//                    .into(holder.imgproduit);
            }





        }
    }

        @Override
    public int getItemCount() {
        return  (null != audio_artistes ? audio_artistes.size() : 0);
    }

    public interface AudioClickHandler{

        void OnClick(Audio_Artiste produit);

    }

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected RoundishImageView imgproduit;
        protected MyTextView_Ubuntu_Bold txtnomproduit;
        protected MyTextView_Ubuntu_Regular txtalbum;
        protected MyTextView_Ubuntu_Regular txtprix;


        public GridViewHolder(View itemView) {
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
