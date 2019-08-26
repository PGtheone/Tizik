package com.shif.peterson.tizik.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Categorie;

import java.util.ArrayList;
import java.util.List;


public class TendanceFragment extends Fragment {


   // public List<String> tendance;
   // RecyclerView recyclerView;
    View view;
   // TendanceRecyclerViewAdapter tendanceRecyclerViewAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;

    ChipCloud chipCloud;
    String[] tendanceArray;
    List<Categorie> listTendance;
    List<String> chiptendance;
    List<String> listSelectedGenre;

   ProgressDialog progressDialog;


    private OnTendanceChoosedListener mListener;

    public TendanceFragment() {
        // Required empty public constructor
    }


    public static TendanceFragment newInstance() {
        TendanceFragment fragment = new TendanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_tendance, container, false);
        chipCloud = view.findViewById(R.id.chipcloud);

        progressDialog =  new ProgressDialog(getContext());
        progressDialog.setTitle("Patientez");
        progressDialog.setCancelable(false);
        progressDialog.show();

        listSelectedGenre = new ArrayList<>();
        chiptendance = new ArrayList<>();

        chipCloud.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntu.regular.ttf"));

        FirebaseFirestore.getInstance().collection(Categorie.class.getSimpleName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()){

                    listTendance = queryDocumentSnapshots.toObjects(Categorie.class);
                    for ( Categorie categorie : listTendance){

                        chiptendance.add(categorie.getNom_categorie());
                        Log.d("TAG", chiptendance.get(0));

                    }

                    String[] tendanceArray = new String[chiptendance.size()];
                    tendanceArray = chiptendance.toArray(tendanceArray);
                    chipCloud.addChips(tendanceArray);

                    progressDialog.dismiss();

                }
            }
        });


      chipCloud.setChipListener(new ChipListener() {
          @Override
          public void chipSelected(int i) {

              onTendanceChoosed(listTendance.get(i), true);
          }

          @Override
          public void chipDeselected(int i) {

              onTendanceChoosed(listTendance.get(i),false);

          }
      });

        return view;
    }

    public void onTendanceChoosed(Categorie index, boolean choosen) {
        if (mListener != null) {
            mListener.onTendanceChoose(index, choosen);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTendanceChoosedListener) {
            mListener = (OnTendanceChoosedListener) context;
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


    public interface OnTendanceChoosedListener {

        void onTendanceChoose(Categorie index, boolean choosen);
    }
}
