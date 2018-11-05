package com.shif.peterson.tizik.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.MusiqueGalerieAdapter;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.MediaQuery;
import com.shif.peterson.tizik.utilis.OnItemSelectedListener;
import com.shif.peterson.tizik.utilis.SelectableItem;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.checkSelfPermission;


public class StepChooseMusicFragment extends Fragment implements BlockingStep,
        OnItemSelectedListener
{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView txtcountphoto;
    private MusiqueGalerieAdapter galerieAdapter;
    private MediaQuery mediaQuery;
    private List<Audio_Artiste> imageItemList;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    int COMPTEUR_PHOTO = 0;

    View view;

    private List<Audio_Artiste> selectedImageItemList;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StepChooseMusicFragment() {
        // Required empty public constructor
    }


    public static StepChooseMusicFragment newInstance() {
        StepChooseMusicFragment fragment = new StepChooseMusicFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step_choose_music, container, false);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerchoosephoto);
        txtcountphoto = (TextView) view.findViewById(R.id.txtcountphoto);



        selectedImageItemList = new ArrayList<>();
        COMPTEUR_PHOTO = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkandAskPermission();
        }
        else {
            initMedia();
        }


        return view;
    }


    private void initMedia(){

        imageItemList=new ArrayList<Audio_Artiste>();
        mediaQuery=new MediaQuery(getContext());
        Cursor cursor = mediaQuery.getAllMusique();

//        imageItemList=mediaQuery.getAllMusique();
//        Log.d("ImageList","Count:"+imageItemList.size());
//        Log.d("ImageList","data : "+imageItemList.get(0).getDATA());
////        galerieAdapter=new GalerieAdapter(imageItemList,getContext(), StepChoosePhotoFragment.this, this);

        galerieAdapter=new MusiqueGalerieAdapter(this, getContext(), cursor, true);

        GridLayoutManager glm = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.shr_column_count));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                updateGalerie();

            }
        });


        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(galerieAdapter);
    }

    protected void updateGalerie(){

       // imageItemList=new ArrayList<Audio_Artiste>();
        mediaQuery = new MediaQuery(getContext());
        //imageItemList = new ArrayList<>();
        Cursor cursor = mediaQuery.getAllMusique();

        cursor = mediaQuery.getAllMusique();
        Log.d("ImageList","Count:"+imageItemList.size());
        galerieAdapter = new MusiqueGalerieAdapter(this, getContext(), cursor, true);
        // galerieAdapter = new GalerieAdapter(imageItemList,getContext(), StepChoosePhotoFragment.this, StepChoosePhotoFragment.this);
        GridLayoutManager glm = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.shr_column_count));

        swipeRefreshLayout.setRefreshing(false);

    }

    private void checkandAskPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = getString(R.string.accesspermission) + permissionsNeeded.get(0);
                for (int i = 0; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        initMedia();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onItemSelected(SelectableItem item) {

    }

    private boolean addPermission(List<String> permissionsList, String permission) {


        if (checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    initMedia();
                }
                else {
                    // Permission Denied
                    Toast.makeText(getContext(), R.string.denied, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
