package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.shif.peterson.tizik.AllMusicActivity;
import com.shif.peterson.tizik.DetailPlaylistActivity;
import com.shif.peterson.tizik.GenresActivity;
import com.shif.peterson.tizik.NowPlayingActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.Playlist;
import com.shif.peterson.tizik.model.SectionDataModel;
import com.shif.peterson.tizik.utilis.RoundishImageView;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.shif.peterson.tizik.utilis.ExoPlayerHelper.MUSIC_EXTRA;


/**
 * Created by ydnar on 27/03/2018.
 */

public class RecyclerDataAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MainListAdapter.MainProductHandler,
        PlaylistAdapter.PlaylistHandler {


    final static String EXTRA_CATEGORIE = "extra_categorie";
    final static String EXTRA_CATEGORIE_NAME = "extra_categorie_name";
    ProductHandler productHandler;



    private ArrayList<SectionDataModel> dataList;
    private Context mContext;

    private static final int PLAYLIST_ROW = 0;
    private static final int GRID_ROW = 1;
    private static final int CONNECTION_ROW = 2;
    private static final int PROD_ROW = 3;
    private static final int PUB_ROW = 4;

    PlaylistAdapter.PlaylistHandler playlistHandler;
    private List<Object> mainObject;
    private List<Categorie> publink;
    private List<Playlist> playlists;
    private List<Audio_Artiste> audio_artistes;
    private List<Abonnement> listAbonnement;


    @Override
    public void OnClick(Audio_Artiste audio) {

        productHandler.onClick(audio);
    }

    public RecyclerDataAdapter(Context context, List<Object> dataList, ProductHandler productHandler, PlaylistAdapter.PlaylistHandler playlistHandler) {
        this.mainObject = dataList;
        this.mContext = context;
        this.productHandler = productHandler;
        this.playlistHandler = playlistHandler;
    }

    @Override
    public void onPlaylistClick(Playlist position) {

        playlistHandler.onPlaylistClick(position);

    }


    public interface ProductHandler{

        void onClick(Audio_Artiste audio);
    }

    @Override
    public void onPlayClick(Playlist position) {

        playlistHandler.onPlayClick(position);

    }

    @Override
    public int getItemViewType(int position) {

        if(position == 0 ){

            return PLAYLIST_ROW;

        }else if(position == 1 ){

            return GRID_ROW;

        } else if(position == 2 ){

            return CONNECTION_ROW;

        }else if(position == 4 ){

            return PUB_ROW;

        } else{

            return PROD_ROW;

        }



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

            case GRID_ROW:

                layoutId = R.layout.item_single_top_music;
                View view5 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new SingleRowDataGridViewholder(view5);


            case CONNECTION_ROW:

                layoutId = R.layout.item_single_connection;
                View view4 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new SingleRowConnectionViewholder(view4);

            case PUB_ROW:

                layoutId = R.layout.item_single_row_pub;
                View view1 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new SingleRowDataViewholder(view1);

            case PLAYLIST_ROW:

                layoutId = R.layout.item_main_list;
                View view2 = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
                return new PlaylistDataViewholder(view2);


            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType){

            case PROD_ROW:


                 dataList = (ArrayList<SectionDataModel>) mainObject.get(position);
                for (SectionDataModel sectionDataModel: dataList) {

                    final String sectionName = sectionDataModel.getHeaderTitle();
                    ArrayList singleSectionItems = sectionDataModel.getAllItemsInSection();

                    ((RecyclerDataViewholder) holder).itemTitle.setText(sectionName);

                    MainListAdapter mainListAdapter = new MainListAdapter(mContext, singleSectionItems, this);
                    ((RecyclerDataViewholder) holder).recycler_view_list.setHasFixedSize(true);
                    ((RecyclerDataViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                    ((RecyclerDataViewholder) holder).recycler_view_list.setAdapter(mainListAdapter);

                    ((RecyclerDataViewholder) holder).recycler_view_list.setNestedScrollingEnabled(false);



                }

                break;

            case GRID_ROW:


                audio_artistes = (List<Audio_Artiste>) mainObject.get(position);
                TopGridAdapter topGridAdapter = new TopGridAdapter(mContext, audio_artistes, new TopGridAdapter.TopgridClickListener() {
                    @Override
                    public void onClick(Audio_Artiste audio_artiste) {

                        Intent intent2 =  new Intent(mContext, NowPlayingActivity.class);
                        intent2.putExtra(MUSIC_EXTRA, audio_artiste);
                        mContext.startActivity(intent2);
                    }
                });

                GridLayoutManager glm1 = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.shr_column_count), GridLayoutManager.HORIZONTAL, false);

                ((SingleRowDataGridViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((SingleRowDataGridViewholder) holder).recycler_view_list.setLayoutManager(glm1);
               // ((SingleRowDataGridViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));

                ((SingleRowDataGridViewholder) holder).recycler_view_list.setAdapter(topGridAdapter);


                break;

            case CONNECTION_ROW:


                listAbonnement = (List<Abonnement>) mainObject.get(position);
                Log.d("TAG ABON", listAbonnement.toString());

                RequestOptions glideOptions = new RequestOptions()
                        .centerCrop()
                        .error(R.drawable.ic_tizik_logo_svg)
                        .placeholder(R.drawable.ic_tizik_logo_svg);

                Glide.with(mContext)
                        .load("https://firebasestorage.googleapis.com/v0/b/tizik-1b4be.appspot.com/o/CarouselImage%2Fcarousel2.jpg?alt=media&token=29095dc9-e725-48a7-b5fd-f4a4ea1c42b9")
                        .apply(glideOptions)
                        .transition(withCrossFade())
                        .into(((SingleRowConnectionViewholder) holder).imgbg);

                ConnectionAdapter connectionAdapter = new ConnectionAdapter(mContext, listAbonnement);
                ((SingleRowConnectionViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((SingleRowConnectionViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                ((SingleRowConnectionViewholder) holder).recycler_view_list.setAdapter(connectionAdapter);


                break;

            case PUB_ROW:


                publink = (List<Categorie>) mainObject.get(position);
                GenreAdapter  genreAdapter = new GenreAdapter(mContext, publink, new GenreAdapter.CategorieClickHandler() {
                    @Override
                    public void OnCategorieClickListener(Categorie categorie) {

                        Intent intent = new Intent(mContext, AllMusicActivity.class);
                        intent.putExtra(EXTRA_CATEGORIE, categorie.getId_categorie());
                        intent.putExtra(EXTRA_CATEGORIE_NAME, categorie.getNom_categorie());
                        mContext.startActivity(intent);

                    }
                }) ;

                GridLayoutManager glm = new GridLayoutManager(mContext, mContext.getResources().getInteger(R.integer.shr_column_count));
                ((SingleRowDataViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((SingleRowDataViewholder) holder).recycler_view_list.setLayoutManager(glm);
                ((SingleRowDataViewholder) holder).recycler_view_list.setAdapter(genreAdapter);


                break;


            case PLAYLIST_ROW:

                final String playlisHead =  mContext.getString(R.string.newplaylist);
                ((PlaylistDataViewholder) holder).itemTitle.setText(playlisHead);

                playlists = (List<Playlist>) mainObject.get(position);

                PlaylistAdapter playlistAdapter = new PlaylistAdapter(mContext, playlists, this);
                ((PlaylistDataViewholder) holder).recycler_view_list.setHasFixedSize(true);
                ((PlaylistDataViewholder) holder).recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                ((PlaylistDataViewholder) holder).recycler_view_list.setAdapter(playlistAdapter);

                ((PlaylistDataViewholder) holder).recycler_view_list.setNestedScrollingEnabled(false);



                break;
        }


    }

    @Override
    public int getItemCount() {
        
        return (null != mainObject ? (mainObject.size()) : 0);
    }

    public static class SingleRowDataViewholder extends RecyclerView.ViewHolder{

        protected MyTextView_Ubuntu_Bold itemTitle;
        protected RecyclerView recycler_view_list;
        protected MyTextView_Ubuntu_Regular txtMore;


        public SingleRowDataViewholder(final View itemView) {
            super(itemView);

            this.itemTitle = itemView.findViewById(R.id.itemtitle);
            this.recycler_view_list = itemView.findViewById(R.id.mainsubrecycler);
            this.txtMore= itemView.findViewById(R.id.itemtitleviewmore);

            txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), GenresActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }

    public static class SingleRowDataGridViewholder extends RecyclerView.ViewHolder{

        protected MyTextView_Ubuntu_Bold itemTitle;
        protected RecyclerView recycler_view_list;
        protected MyTextView_Ubuntu_Regular txtMore;


        public SingleRowDataGridViewholder(final View itemView) {
            super(itemView);

            this.itemTitle = itemView.findViewById(R.id.itemtitle);
            this.recycler_view_list = itemView.findViewById(R.id.mainsubrecycler);
            this.txtMore= itemView.findViewById(R.id.itemtitleviewmore);

            txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), AllMusicActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }

    public static class SingleRowConnectionViewholder extends RecyclerView.ViewHolder{

        protected MyTextView_Ubuntu_Bold itemTitle;
        protected RoundishImageView imgbg;
        protected RecyclerView recycler_view_list;
        protected MyTextView_Ubuntu_Regular txtMore;


        public SingleRowConnectionViewholder(final View itemView) {
            super(itemView);

            this.itemTitle = itemView.findViewById(R.id.itemtitle);

            this.imgbg= itemView.findViewById(R.id.imggalerie);
            this.recycler_view_list = itemView.findViewById(R.id.mainsubrecycler);
            this.txtMore= itemView.findViewById(R.id.itemtitleviewmore);


            txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(itemView.getContext(), AllMusicActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }

    public static class RecyclerDataViewholder extends RecyclerView.ViewHolder{

        protected MyTextView_Ubuntu_Bold itemTitle;
        protected RecyclerView recycler_view_list;
        protected MyTextView_Ubuntu_Regular txtMore;


        public RecyclerDataViewholder(View itemView) {
            super(itemView);

            this.itemTitle = itemView.findViewById(R.id.itemtitle);
            this.recycler_view_list = itemView.findViewById(R.id.mainsubrecycler);
            this.txtMore= itemView.findViewById(R.id.itemtitleviewmore);

        }
    }

    public static class PlaylistDataViewholder extends RecyclerView.ViewHolder{

        protected MyTextView_Ubuntu_Bold itemTitle;
        protected RecyclerView recycler_view_list;
        protected MyTextView_Ubuntu_Regular txtMore;


        public PlaylistDataViewholder(final View view) {
            super(view);

            this.itemTitle = view.findViewById(R.id.itemtitle);
            this.recycler_view_list = view.findViewById(R.id.mainsubrecycler);
            this.txtMore= view.findViewById(R.id.itemtitleviewmore);

            txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(view.getContext(), DetailPlaylistActivity.class);
                    view.getContext().startActivity(intent);
                }
            });


        }
    }
}
