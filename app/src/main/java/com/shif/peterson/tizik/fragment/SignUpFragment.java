package com.shif.peterson.tizik.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.TendanceActivity;
import com.shif.peterson.tizik.model.UserPlan;
import com.shif.peterson.tizik.model.Utilisateur;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends DialogFragment implements View.OnClickListener{

    private final String LOG = SignUpFragment.class.getSimpleName();

    private ImageView imgback;
    private EditText editnomcomplet;
    private EditText editemail;
    private TextInputEditText editpassword;


    private Button btninscrire;
    private Button btnconnect;

    FirebaseAuth mAuth;
    private AVLoadingIndicatorView avLoadingIndicatorView;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        imgback = view.findViewById(R.id.imgclose);
        editnomcomplet = view.findViewById(R.id.editnomcomplet);
        editemail = view.findViewById(R.id.editemail);
        editpassword = view.findViewById(R.id.editpassword);
        avLoadingIndicatorView = view.findViewById(R.id.avi);




        btnconnect = view.findViewById(R.id.btninscrire);
        btninscrire = view.findViewById(R.id.btnemailsignup);




        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
//                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
//                // newFragment.setTargetFragment(MainActivity.this, 0);
//                newFragment.show(ft, "SignUp SignIn");

            }
        });


        mAuth = FirebaseAuth.getInstance();

        btninscrire.setOnClickListener(this);
        btnconnect.setOnClickListener(this);


        return  view;
    }




    protected boolean valide(){

        boolean valide = false;

        if (editnomcomplet.getText().toString().isEmpty()){

            editnomcomplet.setError(getString(R.string.champobligatoire));
            valide = false;

        }
        else if (editemail.getText().toString().isEmpty()){

            editemail.setError(getString(R.string.champobligatoire));
            valide = false;

        }else if (editpassword.getText().toString().isEmpty()){

            editpassword.setError(getString(R.string.champobligatoire));
            valide = false;

        }

        else{
            editemail.setError(null);
            editpassword.setError(null);
            editnomcomplet.setError(null);
            valide = true;

        }

        return valide;
    }


    protected void signUpWithEmailPassword(final String nom, String email, String password){



        if (valide()){

            startAnim();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Log.d(LOG, "signInWithCredential:success");

                                final FirebaseUser user = task.getResult().getUser();

                                Utilisateur utilisateur;

                                utilisateur = new Utilisateur();
                                utilisateur.setId_utilisateur(user.getUid());
                                utilisateur.setNom_complet(nom);
                                utilisateur.setEmail(user.getEmail());
                                utilisateur.setPassword(editpassword.getText().toString());
                                utilisateur.setType_utilisateur("Fan");

                                Date now =  new Date();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                                String date =  formatter.format(now);


                                final UserPlan userPlan = new UserPlan(UUID.randomUUID().toString(), utilisateur.getId_utilisateur(),"0ur5131b-ZBPN-kcae6c7m-yn65S4YEtewq", 0, date, true);

//
                                Utilisateur userfirestore = Utilisateur.getUserById(getActivity(), utilisateur.getId_utilisateur());
                                if (userfirestore == null){

                                    Utilisateur.getUtilisateurCollectionReference().add(utilisateur).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            UserPlan.getUserPlanCollectionReference().add(userPlan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    stopAnim();
                                                    Intent intent = new Intent(getActivity(), TendanceActivity.class);
                                                    startActivity(intent);
                                                    getDialog().dismiss();
                                                }
                                            });

                                        }
                                    });


                                }

                            } else {
                                stopAnim();
                                // If sign in fails, display a message to the user.
                                Log.w(LOG, "signInWithCredential:failure", task.getException());

                            }
                        }
                    });


        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){


            case R.id.btnemailsignup:

                signUpWithEmailPassword(editnomcomplet.getText().toString(), editemail.getText().toString(), editpassword.getText().toString());

                break;

            case R.id.btninscrire:

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                final SignUpFragment newFragment = SignUpFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignUp SignIn");

                break;
        }
    }

    void startAnim(){
        avLoadingIndicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avLoadingIndicatorView.hide();
        // or avi.smoothToHide();
    }




}
