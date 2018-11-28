package com.piyay.pegasus.piyay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.piyay.pegasus.piyay.R;
import com.piyay.pegasus.piyay.model.Produit;
import com.piyay.pegasus.piyay.model.SectionDataModel;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 27/03/2018.
 */

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        MainListAdapter.MainProductHandler{



    ProductHandler productHandler;

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private String publink;

    private static final int PUB_ROW = 1;
    private static final int PROD_ROW = 0;

    @Override
    public void OnClick(Produit produit) {

        productHandler.onClick(produit);
    }


    public interface ProductHandler{

        void onClick(Produit produit);
    }

    public RecyclerDataAdapter(Context context, ArrayList<SectionDataModel> dataList, ProductHandler productHandler) {
        this.dataList = dataList;
        this.mContext = context;
        this.productHandler = productHandler;
        }

    public RecyclerDataAdapter(Context context, ArrayList<SectionDataModel> dataList, String publink, ProductHandler productHandler) {
        this.dataList = dataList;
        this.mContext = context;
        this.publink = publink;
        this.productHandler = productHandler;
    }

//    @NonNull
//    @Override
//    public RecyclerDataViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, null);
//        RecyclerDataViewholder mh = new RecyclerDataViewholder(v);
//        return mh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerDataViewholder holder, int position) {
//
//        final String sectionName = dataList.get(position).getHeaderTitle();
//
//        ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
//
//        holder.itemTitle.setText(sectionName);
//
//        MainListAdapter mainListAdapter = new MainListAdapter(mContext, singleSectionItems);
//        holder.recycler_view_list.setHasFixedSize(true);
//        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        holder.recycler_view_list.setAdapter(mainListAdapter);
//
//
//        holder.recycler_view_list.setNestedScrollingEnabled(false);
//
//
//    }


    @Override
    public int getItemViewType(int position) {

        if(position == 2){

            return PUB_ROW;

        }else{

            return PROD_ROW;

        }
    }

    public boolean isPub(int position){
        return position == 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layoutId;

        switch (viewType){

            case PROD_ROW:

                layoutId = R.layout.item_main_list;
                View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new RecyclerDataViewholder(view);

            case PUB_ROW:

                layoutId = R.layout.item_single_row_pub;
                View view1 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new SingleRowDataViewholder(view1);


            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType){

            case PROD_ROW:

                final String sectionName = dataList.get(position).getHeaderTitle();

                ArrayList singleSectionItems = dataList.get(position).getAllItemsInSection();
                ArrayList PhotoProd = dataList.get(position).getAllPhotoItemsInSection();

                ((RecyclerDataViewholder) holder).itemTitle.setText(sectionName);

                MainListAdapter mainListAdapter = new MainListAdapter(mContext, singleSectionItems,PhotoProd, this);
                ((RecyclerDataViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((RecyclerDataViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                ((RecyclerDataViewholder) holder).recycler_view_list.setAdapter(mainListAdapter);

                ((RecyclerDataViewholder) holder).recycler_view_list.setNestedScrollingEnabled(false);

                break;

            case PUB_ROW:


                RequestOptions glideOptions = new RequestOptions()
                        .override(900, 700)
                        .centerCrop()
                        .error(R.drawable.ic_placeholder)
                        .placeholder(R.drawable.ic_placeholder);


                Glide.with(mContext)
                        .load(publink)
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(((SingleRowDataViewholder) holder).imgpub);


                break;


        }


    }

    @Override
    public int getItemCount() {
        
        return (null != dataList ? dataList.size() : 0);
    }

    public class RecyclerDataViewholder extends RecyclerView.ViewHolder{

        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected TextView txtMore;


        public RecyclerDataViewholder(View itemView) {
            super(itemView);

            this.itemTitle = (TextView) itemView.findViewById(R.id.itemtitle);
            this.recycler_view_list = (RecyclerView) itemView.findViewById(R.id.mainsubrecycler);
            this.txtMore= (TextView) itemView.findViewById(R.id.itemtitleviewmore);

        }
    }

    public static class SingleRowDataViewholder extends RecyclerView.ViewHolder{

        protected ImageView imgpub;


        public SingleRowDataViewholder(View itemView) {
            super(itemView);

            this.imgpub = (ImageView) itemView.findViewById(R.id.imgplaceholder);

        }
    }
}
