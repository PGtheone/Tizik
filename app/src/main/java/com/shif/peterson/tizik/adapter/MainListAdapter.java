package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 27/03/2018.
 */

public class MainListAdapter  extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder>{

    private ArrayList<Audio_Artiste> itemsList;
    private Context mContext;

    final MainProductHandler productHandler;


    public interface MainProductHandler{

        void OnClick(Audio_Artiste produit);

    }

    public MainListAdapter(Context context, ArrayList<Audio_Artiste> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        productHandler = null;
    }

    public MainListAdapter(Context context, ArrayList<Audio_Artiste> itemsList, MainProductHandler productHandler) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.productHandler = productHandler;
    }



    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_recycler, null);
        MainListViewHolder mh = new MainListViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {

        Audio_Artiste singleItem = itemsList.get(position);

        holder.txtnomproduit.setText(singleItem.getTitre_musique());
        holder.txtalbum.setText(singleItem.getId_album());
        if (singleItem.getPrix() == 0){

            holder.txtprix.setText(mContext.getString(R.string.gratuit));
        }else {

            holder.txtprix.setText(String.valueOf(singleItem.getPrix()));
        }


//        RequestOptions glideOptions = new RequestOptions()
//                .centerCrop()
//                .error(R.drawable.ic_placeholder)
//                .placeholder(R.drawable.ic_placeholder);

        RequestOptions glideOptions = new RequestOptions()
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

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView imgproduit;
        protected TextView txtnomproduit;
        protected TextView txtalbum;
        protected TextView txtprix;


        public MainListViewHolder(View itemView) {
            super(itemView);

            imgproduit = (ImageView) itemView.findViewById(R.id.imgproduct);
            txtnomproduit = (TextView) itemView.findViewById(R.id.txtnomproduct);
            txtalbum = (TextView) itemView.findViewById(R.id.txtnomalbum);
            txtprix = (TextView) itemView.findViewById(R.id.txtprixproduct);


            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            Audio_Artiste produit = itemsList.get(getAdapterPosition());
            productHandler.OnClick(produit);

        }
    }
}
