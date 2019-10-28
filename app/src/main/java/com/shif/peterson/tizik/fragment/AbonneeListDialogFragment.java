package com.shif.peterson.tizik.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Abonnement;
import com.shif.peterson.tizik.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     AbonneeListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link AbonneeListDialogFragment.Listener}.</p>
 */
public class AbonneeListDialogFragment extends BottomSheetDialogFragment {


    private static final String ARG_LIST_ABONNEE = "list_abonnee";
    private static final String COLLECTION_NAME = "Utilisateur";
    private Listener mListener;

    public static AbonneeListDialogFragment newInstance(List<Abonnement> list_abonnee) {
        final AbonneeListDialogFragment fragment = new AbonneeListDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_LIST_ABONNEE, (ArrayList<? extends Parcelable>) list_abonnee);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_abonnee_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Abonnement> abonnementList = getArguments()
                .getParcelableArrayList(ARG_LIST_ABONNEE);
        recyclerView.setAdapter(new UtilisateurAdapter(abonnementList));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onUtilisateurClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView txtnom;
        final Button btnFollow;
        final ImageView imgprofil;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.fragment_abonnee_list_dialog_item, parent, false));
            imgprofil = itemView.findViewById(R.id.imgprofil);
            txtnom = itemView.findViewById(R.id.txtnomartiste);
            btnFollow = itemView.findViewById(R.id.btnfollow);


            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onUtilisateurClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class UtilisateurAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<Abonnement> mlist_abonnee;

        UtilisateurAdapter(List<Abonnement> list_abonnee) {
            mlist_abonnee = list_abonnee;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            String idAbonnee = mlist_abonnee.get(position).getId_utilisateur();

            FirebaseFirestore
                    .getInstance()
                    .collection(COLLECTION_NAME)
                    .whereEqualTo("id_utilisateur", idAbonnee)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if(null != queryDocumentSnapshots){

                                final List<Utilisateur> users = queryDocumentSnapshots.toObjects(Utilisateur.class);

                                for (Utilisateur utilisateur : users){

                                    holder.txtnom.setText(utilisateur.getNom_complet());
                                    if(utilisateur.getUrl_photo() != null){

                                        RequestOptions glideOptions = new RequestOptions()
                                                .centerCrop()
                                                .transform(new CircleCrop())
                                                .error(R.color.cardview_dark_background)
                                                .placeholder(android.R.drawable.stat_notify_error);


                                        Glide.with(getContext())
                                                .load(utilisateur.getUrl_photo())
                                                .apply(glideOptions)
                                                .transition(withCrossFade())
                                                .into(holder.imgprofil);

                                    }

                                }

                            }
                        }
                    });


        }

        @Override
        public int getItemCount() {
            return mlist_abonnee.size();
        }



    }

}
