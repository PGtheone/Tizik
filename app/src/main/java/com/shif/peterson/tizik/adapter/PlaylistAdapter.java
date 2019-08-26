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
import com.shif.peterson.tizik.model.Playlist;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>{

    final PlaylistHandler playlistHandler;
    public List<Playlist> playlists;
    public Context mContext;

    public PlaylistAdapter(Context mContext, List<Playlist> playlists, PlaylistHandler playlistHandler) {
        this.playlists = playlists;
        this.mContext = mContext;
        this.playlistHandler = playlistHandler;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        if (playlist != null){

            RequestOptions glideOptions = new RequestOptions()
                    .centerCrop()
                    .error(R.drawable.ic_tizik_logo_svg)
                    .placeholder(R.drawable.circle_bg);

            Glide.with(mContext)
                    .load(playlist.getUrlPoster())
                    .transition(withCrossFade())
                    .apply(glideOptions)
                    .into(holder.imgposter);

            holder.txtnomplaylist.setText(playlist.getNom());
        }

    }

    @Override
    public int getItemCount() {
        return (null != playlists ? playlists.size() : 0);
    }

    public interface PlaylistHandler{

        void onPlaylistClick(Playlist position);
        void onPlayClick(Playlist position);
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgposter;
        ImageView imgiconplay;
        MyTextView_Ubuntu_Bold txtnomplaylist;



        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            imgposter = itemView.findViewById(R.id.imgbg);
            imgiconplay = itemView.findViewById(R.id.imgiconplay);
            txtnomplaylist = itemView.findViewById(R.id.txtnomplaylist);

//            imgiconplay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    playlistHandler.onPlayClick(playlists.get(getAdapterPosition()));
//                }
//            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            playlistHandler.onPlaylistClick(playlists.get(getAdapterPosition()));

        }
    }
}
