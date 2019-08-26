package com.shif.peterson.tizik.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.adapter.CommentAdapter;
import com.shif.peterson.tizik.model.Commentaire_Audio;
import com.shif.peterson.tizik.model.Utilisateur;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class CommentDialogFragment extends DialogFragment {

    private static final String ARG_PARAM_ID_MUSIQUE = "id_musique";
    private static final String ARG_PARAM_ID_USER = "id_user";

    private RecyclerView recyclercomment;
    private EditText editComment;
    private Button btncomment;

    FirebaseFirestore mFirestore;
    Query mQuery;

    private ImageView imgstate;
    private TextView txtheadstate;
    private TextView txtdesc;
    private AVLoadingIndicatorView avi;


    private String mid_musique;
    private String mid_user;

    private List<Commentaire_Audio> commentaire_audios;
    private CommentAdapter commentAdapter;
    LinearLayout linearLayout;

     List<Utilisateur> utilisateurs;

    private OnMusicCommentListener mListener;
    private static final int LIMIT = 50;
    View view;
    int commentSize = 0;

    private static final String COLLECTION_NAME = "Commentaire_Audio";


    public static CollectionReference getCommentCollectionReference(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    public CommentDialogFragment() {
        // Required empty public constructor
    }


    public static CommentDialogFragment newInstance(String id_musique, String id_user) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_ID_MUSIQUE, id_musique);
        args.putString(ARG_PARAM_ID_USER, id_user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        if (getArguments() != null) {
            mid_musique = getArguments().getString(ARG_PARAM_ID_MUSIQUE);
            mid_user = getArguments().getString(ARG_PARAM_ID_USER);
        }

        initFirestore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view =  inflater.inflate(R.layout.fragment_comment_dialog, container, false);

       initUI();
        initRecyclerView();

       btncomment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if ( ! editComment.getText().toString().isEmpty()){

                   showDialog();
               }

           }
       });
        return view;

    }

    public void showDialog() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.rating_view, null, false);
        final AppCompatRatingBar rating = view.findViewById(R.id.rating);
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getContext());

        rating.setMax(5);
        popDialog.setIcon(android.R.drawable.btn_star_big_on);
        popDialog.setTitle("Vote!! ");
        popDialog.setView(view);
        popDialog.setPositiveButton(android.R.string.ok,

        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

            linearLayout.setVisibility(View.VISIBLE);

            final Commentaire_Audio commentaire_audio = new Commentaire_Audio();
            commentaire_audio.setId_commentaireAudio(UUID.randomUUID().toString());
            commentaire_audio.setId_audio(mid_musique);
            commentaire_audio.setId_utilisateur(mid_user);
            commentaire_audio.setCommentaire(editComment.getText().toString());

            Date now =  new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
           String date =  formatter.format(now);

            commentaire_audio.setDate_created(date);
            if(rating.getRating() != 0){

                commentaire_audio.setNote(rating.getRating());
            }else{

                commentaire_audio.setNote(0);
            }

            commentaire_audio.setCreated_by(mid_user);

            getCommentCollectionReference().add(commentaire_audio).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    linearLayout.setVisibility(View.GONE);
                    editComment.setText("");

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    linearLayout.setVisibility(View.GONE);
                }
            });

                dialog.dismiss();

            }

        });

        popDialog.create();
        popDialog.show();

    }

    private void initRecyclerView() {

        if (mQuery == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        commentAdapter = new CommentAdapter(mQuery) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.

                if (getItemCount() == 0) {
                    Log.i("TAG", "empty RecyclerView");

                    linearLayout.setVisibility(View.GONE);
                    imgstate.setVisibility(View.VISIBLE);
                    txtheadstate.setVisibility(View.VISIBLE);
                    txtdesc.setVisibility(View.VISIBLE);
                    recyclercomment.setVisibility(View.GONE);

                } else {

                    commentSize = getItemCount();

                    linearLayout.setVisibility(View.GONE);
                    imgstate.setVisibility(View.GONE);
                    txtheadstate.setVisibility(View.GONE);
                    txtdesc.setVisibility(View.GONE);
                    recyclercomment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(view.findViewById(R.id.container),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclercomment.setLayoutManager(linearLayoutManager);
       DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclercomment.addItemDecoration(itemDecor);
        recyclercomment.setAdapter(commentAdapter);
        recyclercomment.setNestedScrollingEnabled(false);
    }

    private void initUI() {

        utilisateurs = new ArrayList<>();

        commentaire_audios = new ArrayList<>();

        recyclercomment = view.findViewById(R.id.recyclercomment);
        avi = view.findViewById(R.id.avi);
        linearLayout = view.findViewById(R.id.linearprogress);
        txtheadstate = view.findViewById(R.id.txthead);
        txtdesc = view.findViewById(R.id.txtemptycomment);
        imgstate = view.findViewById(R.id.imgemptystate);

        btncomment = view.findViewById(R.id.btncomment);
        editComment = view.findViewById(R.id.editcomment);


        linearLayout.setVisibility(View.VISIBLE);
        imgstate.setVisibility(View.GONE);
        txtheadstate.setVisibility(View.GONE);
        txtdesc.setVisibility(View.GONE);
        recyclercomment.setVisibility(View.GONE);

    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onCommentDOne(commentSize);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMusicCommentListener) {
            mListener = (OnMusicCommentListener) context;
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


    public interface OnMusicCommentListener {
        // TODO: Update argument type and name
        void onCommentDOne(int size);
    }


    private void initFirestore() {

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("Commentaire_Audio")
                .whereEqualTo("id_audio", mid_musique)
                .orderBy("date_created", Query.Direction.ASCENDING)
                .limit(LIMIT);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (commentAdapter != null) {
            commentAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (commentAdapter != null) {
            commentAdapter.stopListening();
        }
    }
}
