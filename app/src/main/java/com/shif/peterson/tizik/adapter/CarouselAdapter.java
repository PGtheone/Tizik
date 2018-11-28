package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by ydnar on 28/03/2018.
 */

public class CarouselAdapter extends PagerAdapter{

    public Context mContext;
    public List<String> publiciteList;
    LayoutInflater layoutInflater;


    public CarouselAdapter(Context mContext, List<String> publiciteList) {
        this.mContext = mContext;
        this.publiciteList = publiciteList;
        layoutInflater = (LayoutInflater)mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return publiciteList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View)object);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_carousel, container, false);

        final String pub = publiciteList.get(position);

        ImageView imgpub = (ImageView)view.findViewById(R.id.img_placeholder_carousel);

        RequestOptions glideOptions = new RequestOptions()
                .override(600, 300)
                .fitCenter()
                .error(R.drawable.ic_placeholder_headset)
                .placeholder(R.drawable.ic_placeholder_headset);



        Glide.with(mContext)
                .load(pub)
                .apply(glideOptions)
                .transition(withCrossFade())
                .into(imgpub);


        container.addView(view);

        return view;
    }




}
