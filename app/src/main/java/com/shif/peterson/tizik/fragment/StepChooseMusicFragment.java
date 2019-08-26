package com.shif.peterson.tizik.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.MusiqueGalerieAdapter;
import com.shif.peterson.tizik.adapter.MusiqueGalerieAdapterList;
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

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class StepChooseMusicFragment extends Fragment implements BlockingStep,
        OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>
{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TextView txtcountphoto;
    private MusiqueGalerieAdapter galerieAdapter;
    ProgressDialog progressBar;
    private MediaQuery mediaQuery;
    private List<Audio_Artiste> imageItemList;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    int COMPTEUR_PHOTO = 0;

    View view;
    private MusiqueGalerieAdapterList galerieAdapterList;

    private List<SelectableItem> selectedImageItemList;


    private OnFragmentInteractionListener mListener;

    public StepChooseMusicFragment() {
        // Required empty public constructor
    }


    public static StepChooseMusicFragment newInstance() {
        StepChooseMusicFragment fragment = new StepChooseMusicFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_step_choose_music, container, false);

        swipeRefreshLayout= view.findViewById(R.id.swiperefresh);
        recyclerView= view.findViewById(R.id.recyclerchoosephoto);
        txtcountphoto = view.findViewById(R.id.txtcountphoto);


        return view;
    }


    protected void updateGalerie(){

       // imageItemList=new ArrayList<Audio_Artiste>();
        mediaQuery = new MediaQuery(getContext());
        //imageItemList = new ArrayList<>();
       // Cursor cursor = mediaQuery.getAllMusique();

      //  cursor = mediaQuery.getAllMusique();

      //  galerieAdapter = new MusiqueGalerieAdapter(this, getContext(), cursor, true);
        // galerieAdapter = new GalerieAdapter(imageItemList,getContext(), StepChoosePhotoFragment.this, StepChoosePhotoFragment.this);

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
       // GridLayoutManager glm = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.shr_column_count));

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

    }

    public void onNextButtonClicked(List<SelectableItem> selectableItems) {
        if (mListener != null) {
            mListener.onFragmentInteraction(selectableItems);
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

        if(!selectedImageItemList.isEmpty() && selectedImageItemList != null){

            onNextButtonClicked(selectedImageItemList);
            callback.goToNextStep();

        }


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


        selectedImageItemList = new ArrayList<>();
        COMPTEUR_PHOTO = 0;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkandAskPermission();
        }
        getLoaderManager().initLoader(1, null, this);


    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onItemSelected(SelectableItem item) {

        if(item.isSelected()){

            COMPTEUR_PHOTO = COMPTEUR_PHOTO + 1;
            selectedImageItemList.add(item);
        }else{

            COMPTEUR_PHOTO = COMPTEUR_PHOTO - 1 ;
            selectedImageItemList.remove(item);

        }

    }

    private boolean addPermission(List<String> permissionsList, String permission) {


        if (checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            return shouldShowRequestPermissionRationale(permission);
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
         progressBar = new ProgressDialog(getContext());
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Patientez...");
        progressBar.setCancelable(false);
        progressBar.show();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED +" Desc Limit 100";

        CursorLoader cursorLoader = new CursorLoader(getContext(), musicUri,null, null, null, sortOrder);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {


        galerieAdapterList = new MusiqueGalerieAdapterList(this, getContext(),  cursor, true);


       // galerieAdapter=new MusiqueGalerieAdapter(this, getContext(),  cursor, true);
      // GridLayoutManager glm = new GridLayoutManager(getContext(), getContext().getResources().getInteger(R.integer.shr_column_count));
        //int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        //recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                updateGalerie();
//
//            }
//        });

        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(galerieAdapterList);
        DividerItemDecoration  itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        progressBar.hide();

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(List<SelectableItem> selectableItems);
    }
}
