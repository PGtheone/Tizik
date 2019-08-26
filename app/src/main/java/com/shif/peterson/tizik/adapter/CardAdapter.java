package com.shif.peterson.tizik.adapter;

import com.google.android.material.card.MaterialCardView;

public interface CardAdapter {

        int MAX_ELEVATION_FACTOR = 8;

        float getBaseElevation();

        MaterialCardView getCardViewAt(int position);

        int getCount();
}
