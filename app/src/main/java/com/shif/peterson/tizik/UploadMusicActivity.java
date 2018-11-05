package com.shif.peterson.tizik;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shif.peterson.tizik.adapter.UploadMusicStepAdapter;
import com.shif.peterson.tizik.fragment.StepChooseMusicFragment;
import com.shif.peterson.tizik.fragment.StepFinishFragment;
import com.stepstone.stepper.StepperLayout;

public class UploadMusicActivity extends AppCompatActivity
        implements StepChooseMusicFragment.OnFragmentInteractionListener,
        StepFinishFragment.OnFragmentInteractionListener {

    private StepperLayout mStepperLayout;
    private String CURRENT_STEP_POSITION_KEY = "position";
    UploadMusicStepAdapter uploadMusicStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_music);

        mStepperLayout = (StepperLayout) findViewById(R.id.step);
        int currentPosition = 0;

        if( savedInstanceState != null ){

            if(0 < savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY) ){

                currentPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
            }

//            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
//            produit1 = savedInstanceState.getParcelable(KEY_PRODUIT);
//            idprod = savedInstanceState.getString(KEY_PRODUIT_ID);


        }

        uploadMusicStepAdapter = new UploadMusicStepAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(uploadMusicStepAdapter, currentPosition);
        //mStepperLayout.setListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
