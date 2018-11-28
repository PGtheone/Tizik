package com.piyay.pegasus.piyay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.piyay.pegasus.piyay.R;
import com.piyay.pegasus.piyay.model.Photo_Produit;
import com.piyay.pegasus.piyay.model.Produit;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 27/03/2018.
 */

public class MainListAdapter  extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder>{

    private ArrayList<Produit> itemsList;
    private ArrayList<Photo_Produit> photo_produitsitemsList;
    private Context mContext;

    final MainProductHandler productHandler;


    public interface MainProductHandler{

        void OnClick(Produit produit);

    }

    public MainListAdapter(Context context, ArrayList<Produit> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        productHandler = null;
    }

    public MainListAdapter(Context context, ArrayList<Produit> itemsList, MainProductHandler productHandler) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.productHandler = productHandler;
    }

    public MainListAdapter(Context context, ArrayList<Produit> itemsList,ArrayList<Photo_Produit> photo_produitsitemsList, MainProductHandler productHandler) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.productHandler = productHandler;
        this.photo_produitsitemsList = photo_produitsitemsList;
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

        Produit singleItem = itemsList.get(position);
        Photo_Produit photo_produit = new Photo_Produit() ;

        if(photo_produitsitemsList != null){

            photo_produit = photo_produitsitemsList.get(position);
        }


        holder.txtnomproduit.setText(singleItem.getNomProduit());
        holder.txtprix.setText(String.valueOf(singleItem.getPrixUnitaire()));
        //holder.txtcurrency.setText();
        //holder.txtnote.setText();

//        RequestOptions glideOptions = new RequestOptions()
//                .centerCrop()
//                .error(R.drawable.ic_placeholder)
//                .placeholder(R.drawable.ic_placeholder);

        RequestOptions glideOptions = new RequestOptions()
                .override(200, 200)
                .centerCrop()
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                ;
//                .error(R.drawable.ic_placeholder)
//                .placeholder(R.drawable.ic_placeholder);



        if (photo_produit.getUrlPhoto() != null){

            String url = photo_produit.getUrlPhoto();

            Glide.with(mContext)
                    .load(url)
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(holder.imgproduit);
        }else{


            String url = singleItem.getPhoto_produits().get(0).getUrlPhoto();

            Glide.with(mContext)
                    .load(url)
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(holder.imgproduit);
        }


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView imgproduit;
        protected TextView txtnomproduit;
        protected TextView txtcurrency;
        protected TextView txtprix;
        protected TextView txtnote;


        public MainListViewHolder(View itemView) {
            super(itemView);

            imgproduit = (ImageView) itemView.findViewById(R.id.imgproduct);
            txtnomproduit = (TextView) itemView.findViewById(R.id.txtnomproduct);
            txtcurrency = (TextView) itemView.findViewById(R.id.txtcurrency);
            txtprix = (TextView) itemView.findViewById(R.id.txtprixproduct);
            txtnote = (TextView) itemView.findViewById(R.id.txtrate);

            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            Produit produit = itemsList.get(getAdapterPosition());
            productHandler.OnClick(produit);

        }
    }
}
