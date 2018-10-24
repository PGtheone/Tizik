package com.shif.peterson.tizik.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Utilisateur;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainSignInDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainSignInDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String LOG = MainSignInDialogFragment.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //google login
    private GoogleSignInClient mGoogleSignInClient;


    private Button btnloginemail;
    private Button btnlogintelephone;
    private AppCompatButton btnInscrire;


    private SignInButton signInButton;

    private ImageView imgback;


    public MainSignInDialogFragment() {
        // Required empty public constructor
    }


    public static MainSignInDialogFragment newInstance() {
        MainSignInDialogFragment fragment = new MainSignInDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_sign_in_dialog, container, false);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();

        btnloginemail = view.findViewById(R.id.btnemaillogin);
        btnInscrire = (AppCompatButton)  view.findViewById(R.id.btninscrire);

        signInButton = view.findViewById(R.id.btngmail);

        imgback = (ImageView) view.findViewById(R.id.imgclose);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();

            }
        });


        //Configure google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions
                                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                                            .requestEmail()
                                            .build();


        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        //COnfigure facebook login
        btnloginemail.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        btnInscrire.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btngmail:

                signIn();

                break;

            case R.id.btnemaillogin:

                this.dismiss();

                android.support.v4.app.FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                final EmailSigninFragment newFragment1 = EmailSigninFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment1.show(ft1, "email SignIn");

                break;

            case R.id.btninscrire:

                this.dismiss();

                android.support.v4.app.FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                final SignUpFragment newFragment2 = SignUpFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment2.show(ft2, "Phone SignIn");

                break;


        }



    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
       startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Utilisateur utilisateur;

                            utilisateur = new Utilisateur();
                            utilisateur.setId_utilisateur(user.getUid());
                            utilisateur.setNom_complet(user.getDisplayName());
                            utilisateur.setEmail(user.getEmail());
                            utilisateur.setUrl_photo(user.getPhotoUrl().toString());
                            utilisateur.setTelephone(user.getPhoneNumber());


                            Utilisateur userfirestore = Utilisateur.getUserById(getActivity(), utilisateur.getId_utilisateur());
                            if (userfirestore == null){

                                Utilisateur.getUtilisateurCollectionReference().add(utilisateur).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Log.d(LOG, "Success");
                                        getDialog().dismiss();
                                    }
                                });

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG, "signInWithCredential:failure", task.getException());

                        }

                    }
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle( account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(LOG, "Google sign in failed "+e.getMessage(), e);

            }
        }
    }
}
