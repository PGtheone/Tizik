package com.shif.peterson.tizik;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.fragment.SignUpFragment;
import com.shif.peterson.tizik.model.HistoriquePlan;
import com.shif.peterson.tizik.model.UserPlan;
import com.shif.peterson.tizik.model.Utilisateur;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.shif.peterson.tizik.model.UserPlan.getUserPlanCollectionReference;

public class MainSignInActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String LOG = MainSignInActivity.class.getSimpleName();

    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //google login
    private GoogleSignInClient mGoogleSignInClient;
    private AppCompatButton btnInscrire;
    private SignInButton signInButton;

    private ImageView imgback;

    private EditText editemail;
    private TextInputEditText editpassword;
    private Button btnconnect;
    private Button btnsignup;
    private AppCompatButton btnfacebooklogin;

    private AVLoadingIndicatorView avLoadingIndicatorView;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_sign_in_dialog);

        initUI();

        mAuth = FirebaseAuth.getInstance();
        //Configure google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();

        btnconnect.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        btnfacebooklogin.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        btnInscrire.setOnClickListener(this);


    }

    private void initUI() {

        btnInscrire = findViewById(R.id.btninscrire);
        signInButton = findViewById(R.id.btngmail);
        imgback = findViewById(R.id.imgclose);
        editemail = findViewById(R.id.editemail);
        editpassword = findViewById(R.id.editpassword);

        btnconnect = findViewById(R.id.btnemaillogin);
        btnsignup = findViewById(R.id.btninscrire);
        btnfacebooklogin = findViewById(R.id.btnfacebook);

        avLoadingIndicatorView = findViewById(R.id.avi);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainSignInActivity.super.onBackPressed();

            }
        });
    }


    protected boolean valide(){

        boolean valide = false;

        if (editemail.getText().toString().isEmpty()){

            editemail.setError(getString(R.string.champobligatoire));
            valide = false;

        }else if (editpassword.getText().toString().isEmpty()){

            editpassword.setError(getString(R.string.champobligatoire));
            valide = false;

        }

        else{
            editemail.setError(null);
            editpassword.setError(null);
            valide = true;

        }

        return valide;
    }

    protected void seConnecterAvecEmailPassword(String email, String password){

        if(valide()){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            stopAnim();
                            Intent intent  = new Intent(MainSignInActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    stopAnim();
                }
            });
        }



    }

    protected void seConnecterAvecFacebook(){


        FacebookSdk.sdkInitialize(this);
//        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(MainSignInActivity.this, Arrays.asList("email", "public_profile"));

        Log.d("TAG", "facebook: Here");

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("TAG success", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d("TAG", "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("TAG", "facebook:onError"+error.getMessage(), error);
                stopAnim();

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d("TAG", "handleFacebookAccessToken:" + token);
        //progressDialog = ProgressDialog.show(this, "Loading...", "Please wait Posts is Loading...");
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Log.d("TAG", "signInWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                    signUser(user);

                }else{

                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainSignInActivity.this, "Authentication failed. Please Use another Account.",Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.btngmail:

                seConnecterAvecGoogle();

                break;

            case R.id.btnemaillogin:

                if (valide()) {

                    seConnecterAvecEmailPassword(editemail.getText().toString(), editpassword.getText().toString());



                } else {

                    stopAnim();
                    Toast.makeText(MainSignInActivity.this, "Show Error mesage", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btninscrire:


                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                final SignUpFragment newFragment2 = SignUpFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment2.show(ft2, "Phone SignIn");

                break;

            case R.id.btnfacebook:

                startAnim();
                seConnecterAvecFacebook();

                break;


        }
    }


    private void seConnecterAvecGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(MainSignInActivity.this,
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
                .addOnCompleteListener(MainSignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            signUser(user);


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
            startAnim();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle( account);
            } catch (ApiException e) {
                stopAnim();
                // Google Sign In failed, update UI appropriately
                Log.w(LOG, "Google sign in failed "+e.getMessage(), e);

            }
        }else{

            mCallbackManager.onActivityResult(requestCode, resultCode, data);


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


    public void signUser(FirebaseUser user){


        final Utilisateur utilisateur;

        utilisateur = new Utilisateur();
        utilisateur.setId_utilisateur(user.getUid());
        utilisateur.setNom_complet(user.getDisplayName());
        utilisateur.setEmail(user.getEmail());
        utilisateur.setUrl_photo(user.getPhotoUrl().toString());
        utilisateur.setTelephone(user.getPhoneNumber());
        utilisateur.setType_utilisateur("Fan");

        Date now =  new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        String date =  formatter.format(now);


        final UserPlan userPlan = new UserPlan(UUID.randomUUID().toString(), utilisateur.getId_utilisateur(),"0ur5131b-ZBPN-kcae6c7m-yn65S4YEtewq", 0, date, true);
        final HistoriquePlan historiquePlan = new HistoriquePlan(UUID.randomUUID().toString(), utilisateur.getId_utilisateur(), "0ur5131b-ZBPN-kcae6c7m-yn65S4YEtewq", date );


        Utilisateur
                .getUtilisateurCollectionReference()
                .whereEqualTo("id_utilisateur", utilisateur.getId_utilisateur())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(queryDocumentSnapshots.isEmpty()){

                            Utilisateur.getUtilisateurCollectionReference().add(utilisateur).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    getUserPlanCollectionReference().add(userPlan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {


                                            HistoriquePlan.getHistoriquePlanCollectionReference().add(historiquePlan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {


                                                    stopAnim();
                                                }
                                            });
//
                                        }
                                    });

                                }
                            });

                            Intent intent = new Intent(MainSignInActivity.this, TendanceActivity.class);
                            startActivity(intent);
                        }

                        else{


                            Intent intent = new Intent(MainSignInActivity.this, MainActivity.class);
                            startActivity(intent);


                        }
                    }
                });

    }
}
