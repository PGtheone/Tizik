package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.SectionDataModel;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 27/03/2018.
 */

public class RecyclerDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        MainListAdapter.MainProductHandler{



    ProductHandler productHandler;

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private List<String> publink;

    private static final int PUB_ROW = 1;
    private static final int PROD_ROW = 0;

    private static  int pubchoose = 1;

    @Override
    public void OnClick(Audio_Artiste audio) {

        productHandler.onClick(audio);
    }


    public interface ProductHandler{

        void onClick(Audio_Artiste audio);
    }

    public RecyclerDataAdapter(Context context, ArrayList<SectionDataModel> dataList, ProductHandler productHandler) {
        this.dataList = dataList;
        this.mContext = context;
        this.productHandler = productHandler;
        }

    public RecyclerDataAdapter(Context context, ArrayList<SectionDataModel> dataList, List<String> publink, ProductHandler productHandler) {
        this.dataList = dataList;
        this.mContext = context;
        this.publink = publink;
        this.productHandler = productHandler;
        pubchoose = 1;
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

        if(position == 2 || position == 6){

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

                ((RecyclerDataViewholder) holder).itemTitle.setText(sectionName);


                MainListAdapter mainListAdapter = new MainListAdapter(mContext, singleSectionItems, this);
                ((RecyclerDataViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((RecyclerDataViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                ((RecyclerDataViewholder) holder).recycler_view_list.setAdapter(mainListAdapter);

                ((RecyclerDataViewholder) holder).recycler_view_list.setNestedScrollingEnabled(false);

                break;

            case PUB_ROW:



                String pub;

                    if(position == 2){

                         pub = publink.get(1);
                    }else{

                         pub = publink.get(2);
                    }


                    RequestOptions glideOptions = new RequestOptions()
                            .override(900, 700)
                            .centerCrop()
                            .error(R.drawable.ic_placeholder_headset)
                            .placeholder(R.drawable.ic_placeholder_headset);


                    Glide.with(mContext)
                            .load(pub)
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
