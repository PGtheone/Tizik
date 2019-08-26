package com.shif.peterson.tizik.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.shif.peterson.tizik.MainSignInActivity;
import com.shif.peterson.tizik.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailSigninFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailSigninFragment extends DialogFragment implements View.OnClickListener{

    private String LOG = EmailSigninFragment.class.getSimpleName();

    private EditText editemail;
    private CountryCodePicker ccp;
    private TextInputEditText editpassword;
    private AppCompatButton btnconnect;
    private Button btnsignup;

    private ImageView imgback;

    FirebaseAuth mAuth;


    public EmailSigninFragment() {
        // Required empty public constructor
    }


    public static EmailSigninFragment newInstance() {
        EmailSigninFragment fragment = new EmailSigninFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_signin, container, false);


        editemail = view.findViewById(R.id.editemail);
        editpassword = view.findViewById(R.id.editpassword);

        btnconnect = view.findViewById(R.id.btnemaillogin);
        btnsignup = view.findViewById(R.id.btninscrire);

        imgback = view.findViewById(R.id.imgclose);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
                startActivity(new Intent(getContext(), MainSignInActivity.class));

            }
        });


        mAuth = FirebaseAuth.getInstance();


        btnconnect.setOnClickListener(this);
        btnsignup.setOnClickListener(this);

        return view;
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


    protected void signInWithEmailPassword(String email, String password){

        if (valide()){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            getDialog().dismiss();
                        }
                    });
        }
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnemaillogin:
                //todo add Loding screen
                if(valide()){
                    signInWithEmailPassword(editemail.getText().toString(), editpassword.getText().toString());

                }else{

                    Toast.makeText(getContext(), "Show Error mesage", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btninscrire:

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                final SignUpFragment newFragment = SignUpFragment.newInstance();
                // newFragment.setTargetFragment(MainActivity.this, 0);
                newFragment.show(ft, "SignUp SignIn");

                break;
        }

    }
}
