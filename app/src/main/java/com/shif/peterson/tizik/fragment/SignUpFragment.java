package com.shif.peterson.tizik.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;
import com.shif.peterson.tizik.R;

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
    private CountryCodePicker ccpPays;

    private EditText editphone;
    private CountryCodePicker ccp;

    private Button btninscrire;
    private Button btnconnect;

    FirebaseAuth mAuth;

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

        imgback = (ImageView) view.findViewById(R.id.imgclose);
        editnomcomplet = (EditText) view.findViewById(R.id.editnomcomplet);
        editemail = (EditText) view.findViewById(R.id.editemail);
        editpassword = (TextInputEditText) view.findViewById(R.id.editpassword);

        editphone = (EditText) view.findViewById(R.id.edittel);
        ccp = (CountryCodePicker) view.findViewById(R.id.ccp);
        ccpPays = (CountryCodePicker) view.findViewById(R.id.countryCodePicker2);

        btnconnect = (Button) view.findViewById(R.id.btninscrire);
        btninscrire = (Button) view.findViewById(R.id.btnemailsignup);



        ccp.registerCarrierNumberEditText(editphone);
        ccp.setAutoDetectedCountry(true);
        ccp.setDefaultCountryUsingNameCode("ht");

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                final MainSignInDialogFragment newFragment = MainSignInDialogFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignUp SignIn");

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


    protected void signUpWithEmailPassword(String nom, String email, String password){

        String telephone = ccp.getFullNumberWithPlus();
        String pays = ccpPays.getSelectedCountryName();

        if (valide()){

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                Log.d(LOG, "signInWithCredential:success");

                                FirebaseUser user = task.getResult().getUser();

//                                Utilisateur utilisateur;
//
//                                utilisateur = new Utilisateur();
//                                utilisateur.setIdUtilisateur(user.getUid());
//                                utilisateur.setNomComplet(nom);
//                                utilisateur.setEmail(user.getEmail());
//                                utilisateur.setTelephone(telephone);
//                                utilisateur.setMotDePasse(editpassword.getText().toString());
//                                utilisateur.setPays(pays);
//
//                                Utilisateur userfirestore = UserNetworkUtils.getUserById(getActivity(), utilisateur.getIdUtilisateur());
//                                if (userfirestore == null){
//
//                                    UserNetworkUtils.getUtilisateurCollectionReference().add(utilisateur).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//
//                                            Log.d(LOG, "Success");
//                                            getDialog().dismiss();
//                                        }
//                                    });
//
//
//                                }

                            } else {
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

                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                final SignUpFragment newFragment = SignUpFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignUp SignIn");

                break;
        }
    }
}
