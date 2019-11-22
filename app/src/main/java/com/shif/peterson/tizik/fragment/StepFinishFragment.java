package com.shif.peterson.tizik.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.MainActivity;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.MusicUploadLastStepAdapter;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.model.Plan;
import com.shif.peterson.tizik.model.UserPlan;
import com.shif.peterson.tizik.utilis.SelectableItem;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


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

    final Plan detailplan = new Plan();
    TextView txttotal;
    double somme = 0;
    List<Categorie> categories;
    UserPlan userPlan;


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

        recyclerViewMusique = view.findViewById(R.id.recyclerviewmusic);
      //  cardSpecial = view.findViewById(R.id.cardspecial);
        txttotal = view.findViewById(R.id.txttotal);


//        cardSpecial.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                final PlanDialogFragment newFragment = PlanDialogFragment.newInstance("p-001");
//                // newFragment.setTargetFragment(MainActivity.this, 0);
//                newFragment.show(ft, "Plan");
//            }
//        });


    }



    public void onCompleteClicked(List<SelectableItem> uri) {
        if (mListener != null) {
            for(int i = 0; i< uri.size(); i++){

                SelectableItem selectableItem = uri.get(i);
                selectableItem.setNom_chanteur(selectableItem.getArtiste());

            }

            if(uri.size() == canUpload(uri)){
                 double totaluse  = userPlan.getQuantite_used() + somme;
                mListener.onMusicChoosed(uri);
//                 if(totaluse > detailplan.getTempsUpload()){
//
//
//
//                 }else{
//
//                       mListener.onMusicChoosed(uri);
//
//                 }
//                Toast.makeText(getContext(), ""+totaluse, Toast.LENGTH_SHORT).show();


            }

        }
    }


    private int canUpload(List<SelectableItem> uri ){

        int valide = 0;
        for(int i = 0; i< uri.size(); i++){
            SelectableItem selectableItem = uri.get(i);
            if (selectableItem.getSelectedCategorie().isEmpty()){
                if(valide == 0){

                    valide = 0 ;
                }else{

                    valide = valide -1;
                }

                highlightEmptyItem(i);

            }else{

                valide = valide + 1;

            }

        }



            return valide;
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


            final ProgressDialog progressBar = new ProgressDialog(getContext());
            progressBar.setIndeterminate(true);
            progressBar.setMessage("Patientez...");
            progressBar.setCancelable(false);
            progressBar.show();

            FirebaseAuth   mAuth = FirebaseAuth.getInstance();
           FirebaseUser firebaseUser = mAuth.getCurrentUser();


            FirebaseFirestore.getInstance()
                    .collection(Categorie.class.getSimpleName())
                    .orderBy("nom_categorie", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (!queryDocumentSnapshots.isEmpty()){

                                categories = queryDocumentSnapshots.toObjects(Categorie.class);

                                musicUploadLastStepAdapter = new MusicUploadLastStepAdapter(getContext(), selectableItemList, categories,new MusicUploadLastStepAdapter.AudioDeleteHandler() {
                                    @Override
                                    public void onAudioDeleted(int position) {

                                        somme = 0;
                                        if (!selectableItemList.isEmpty()){

                                            for(SelectableItem selectableItem : selectableItemList){

                                                somme += selectableItem.getDuree_musique();
                                                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                                cal.setTimeInMillis((long)somme);
                                                String date = DateFormat.format("mm:ss", cal).toString();
                                                txttotal.setText(date);
                                            }

                                        }else{


                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }

                                    }
                                });


                                layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                                recyclerViewMusique.setLayoutManager(layoutManager);

                                itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                                recyclerViewMusique.addItemDecoration(itemDecor);

                                recyclerViewMusique.setAdapter(musicUploadLastStepAdapter);
                                recyclerViewMusique.setNestedScrollingEnabled(false);
                                recyclerViewMusique.setVisibility(View.VISIBLE);


                                somme = 0;
                                for(SelectableItem selectableItem : selectableItemList){

                                    somme += selectableItem.getDuree_musique();
                                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                                    cal.setTimeInMillis((long)somme);
                                    String date = DateFormat.format("mm:ss", cal).toString();
                                    txttotal.setText(date);

                                }


                            }


                        }
                    });


            FirebaseFirestore.getInstance()
                    .collection(UserPlan.class.getSimpleName())
                    .whereEqualTo("id_User", firebaseUser.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                    if (!queryDocumentSnapshots.isEmpty()){
                        List<UserPlan> userPlans = queryDocumentSnapshots.toObjects(UserPlan.class);
                        for (UserPlan userPla : userPlans){
                            if(userPla.isIs_active()){
                                userPlan = userPla;
                            }
                        }


                        //todo update the code

                        if(userPlan.getId_Plan().equals("0ur5131b-ZBPN-kcae6c7m-yn65S4YEtewq")){

                            detailplan.setId_Plan("0ur5131b-ZBPN-kcae6c7m-yn65S4YEtewq");
                            detailplan.setDescription("Plan Gratuit avec accès limité");
                            detailplan.setTitre("Plan Freemium");
                            detailplan.setPrix(0);
                            detailplan.setTailleMax(20);
                            detailplan.setTempsUpload(72000);
                            detailplan.setUniteMonetaire("Gourdes");

                            progressBar.dismiss();

                        }
//                        FirebaseFirestore.getInstance()
//                                .collection(Plan.class.getSimpleName())
//                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                if (!queryDocumentSnapshots.isEmpty()){
//                                    Toast.makeText(getContext(), "la", Toast.LENGTH_SHORT).show();
//                                    List<Plan> list = queryDocumentSnapshots.toObjects(Plan.class);
//
//                                     detailplan.setId_Plan(list.get(0).getId_Plan());
//                                    detailplan.setDescription(list.get(0).getDescription());
//                                    detailplan.setTitre(list.get(0).getTitre());
//                                    detailplan.setPrix(list.get(0).getPrix());
//                                    detailplan.setTailleMax(list.get(0).getTailleMax());
//                                    detailplan.setTempsUpload(list.get(0).getTempsUpload());
//                                    detailplan.setUniteMonetaire(list.get(0).getUniteMonetaire());
//
//                                    progressBar.dismiss();
//                                }
//                            }
//                        });


                    }
                }
            });




             }

    }


    public void highlightEmptyItem(int position){

        View view = layoutManager.findViewByPosition(position);
        AppCompatMultiAutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.recipient_auto_complete_text_view);
        autoCompleteTextView.setError(view.getContext().getResources().getString(R.string.vide));

        musicUploadLastStepAdapter.notifyItemChanged(position);

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }



    @Override
    public void onAudioDeleted(int position) {


    }


    public interface onChoosenMusicListener {
        void onMusicChoosed(List<SelectableItem> selectableItemList);
    }

    public static void initListSelectedItem(List<SelectableItem> list){

        selectableItemList = list;
    }
}
