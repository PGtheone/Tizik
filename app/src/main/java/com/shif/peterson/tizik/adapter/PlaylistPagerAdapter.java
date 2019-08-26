package com.shif.peterson.tizik.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Bold;
import com.shif.peterson.tizik.model.Playlist;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlaylistPagerAdapter extends PagerAdapter implements CardAdapter {

    final playlistClickHandler playlistClickHandler;
    private List<MaterialCardView> mViews;
    private List<Playlist> mData;
    private float mBaseElevation;

    public PlaylistPagerAdapter(playlistClickHandler playlistClickHandler) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.playlistClickHandler = playlistClickHandler;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void addPlaylist(Playlist item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public MaterialCardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_detail_playlist, container, false);
        container.addView(view);

        MaterialCardView cardView = view.findViewById(R.id.cardview);
        ImageView imgposter = view.findViewById(R.id.imgbg);
        ImageView imgiconplay = view.findViewById(R.id.imgiconplay);
       TextView txtnomplaylist = (MyTextView_Ubuntu_Bold) view.findViewById(R.id.txtnomplaylist);

       final Playlist playlist = mData.get(position);
       if (playlist != null){

           Glide.with(view.getContext())
                   .load(playlist.getUrlPoster())
                   .transition(withCrossFade())
                   .into(imgposter);

           txtnomplaylist.setText(playlist.getNom());
       }



        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playlistClickHandler.onClick(playlist);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }


    public interface playlistClickHandler {

        void onClick(Playlist playlist);
    }



}
