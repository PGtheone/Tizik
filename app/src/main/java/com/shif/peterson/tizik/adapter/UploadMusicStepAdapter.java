package com.shif.peterson.tizik.adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.fragment.StepChooseMusicFragment;
import com.shif.peterson.tizik.fragment.StepFinishFragment;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.util.List;

public class UploadMusicStepAdapter extends AbstractFragmentStepAdapter {

    List<Audio_Artiste> audioArtisteList;

    public UploadMusicStepAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }


    @Override
    public Step createStep(int position) {

        Step step;
        switch (position){

            case 0:

                step = StepChooseMusicFragment.newInstance();
                return step;

            case 1:

                step = StepFinishFragment.newInstance();
                return step;

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle(R.string.app_name) //can be a CharSequence instead
                .create();
    }
}
