package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Audio_Artiste;

import java.util.ArrayList;
import java.util.Random;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 27/03/2018.
 */

public class MainListAdapter  extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder>{

    private ArrayList<Audio_Artiste> itemsList;
    private Context mContext;

    final MainProductHandler productHandler;
    RequestOptions glideOptions;


    public interface MainProductHandler{

        void OnClick(Audio_Artiste produit);

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
            holder.txtprix.setVisibility(View.GONE);
        }else {

            holder.txtprix.setText(String.valueOf(singleItem.getPrix()));
        }


        int min = 1;
        int max = 3;

        Random r = new Random();
        int i = r.nextInt(max - min + 1) + min;


        glideOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.ic_tizik_logo_svg_gray)
                .placeholder(R.drawable.ic_tizik_logo_svg_gray);


        if (singleItem.getUrl_poster() != null){

            String url = singleItem.getUrl_poster();

            Glide.with(mContext)
                    .load(url)
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(holder.imgproduit);
        }else{



        }


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView imgproduit;
        protected MyTextView_Ubuntu_Bold txtnomproduit;
        protected MyTextView_Ubuntu_Regular txtalbum;
        protected MyTextView_Ubuntu_Regular txtprix;


        public MainListViewHolder(View itemView) {
            super(itemView);

            imgproduit = itemView.findViewById(R.id.imgproduct);
            txtnomproduit = itemView.findViewById(R.id.txtnomproduct);
            txtalbum = itemView.findViewById(R.id.txtnomalbum);
            txtprix = itemView.findViewById(R.id.txtprixproduct);


            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            Audio_Artiste produit = itemsList.get(getAdapterPosition());
            productHandler.OnClick(produit);

        }
    }
}
