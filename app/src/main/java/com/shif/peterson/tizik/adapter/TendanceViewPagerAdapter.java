package com.shif.peterson.tizik.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shif.peterson.tizik.fragment.ArtisteTendanceFragment;
import com.shif.peterson.tizik.fragment.TendanceFragment;

public class TendanceViewPagerAdapter extends FragmentStatePagerAdapter {

    int ITEMS = 2;

    public TendanceViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show image
                return TendanceFragment.newInstance();
            case 1: // Fragment # 1 - This will show image
                return ArtisteTendanceFragment.newInstance();
            default:// Fragment # 2-9 - Will show list
                return TendanceFragment.newInstance();
        }
    }
}


