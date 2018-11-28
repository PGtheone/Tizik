package com.shif.peterson.tizik.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.MusicUploadLastStepAdapter;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.SelectableItem;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.List;


public class StepFinishFragment extends Fragment implements
        BlockingStep,
        MusicUploadLastStepAdapter.AudioDeleteHandler
{

    MusicUploadLastStepAdapter musicUploadLastStepAdapter;
    List<Audio_Artiste> audioArtistes;
    private onChoosenMusicListener mListener;
    static List<SelectableItem> selectableItemList;
    View view;
    RecyclerView recyclerViewMusique;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration itemDecor;

    CardView cardSpecial;

    public StepFinishFragment() {
        // Required empty public constructor
    }

    public static StepFinishFragment newInstance() {
        StepFinishFragment fragment = new StepFinishFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.fragment_step_finish, container, false);

        initUI();
        return view;




    }

    private void initUI(){

        recyclerViewMusique = (RecyclerView) view.findViewById(R.id.recyclerviewmusic);
        cardSpecial = (CardView) view.findViewById(R.id.cardspecial);

        cardSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                final PlanDialogFragment newFragment = PlanDialogFragment.newInstance("p-001");
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "Plan");
            }
        });

      //  btncommande = (AppCompatButton) findViewById(R.id.btncommander);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCompleteClicked(List<SelectableItem> uri) {
        if (mListener != null) {
            mListener.onMusicChoosed(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onChoosenMusicListener) {
            mListener = (onChoosenMusicListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        if(selectableItemList != null){

            onCompleteClicked(selectableItemList);
        }

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

        if (selectableItemList != null){

              musicUploadLastStepAdapter = new MusicUploadLastStepAdapter(getContext(), selectableItemList, this );
           layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
           recyclerViewMusique.setLayoutManager(layoutManager);
            //itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);


            itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            recyclerViewMusique.addItemDecoration(itemDecor);

            recyclerViewMusique.setAdapter(musicUploadLastStepAdapter);
            recyclerViewMusique.setNestedScrollingEnabled(false);
            recyclerViewMusique.setVisibility(View.VISIBLE);

             }

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


    @Override
    public void onAudioDeleted(int position) {

        selectableItemList.remove(position);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onChoosenMusicListener {
        // TODO: Update argument type and name
        void onMusicChoosed(List<SelectableItem> selectableItemList);
    }

    public static void initListSelectedItem(List<SelectableItem> list){

        selectableItemList = list;
    }
}
