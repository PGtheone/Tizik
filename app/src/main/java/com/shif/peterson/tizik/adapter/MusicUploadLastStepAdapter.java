package com.shif.peterson.tizik.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.ChipDrawable;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.model.Categorie;
import com.shif.peterson.tizik.utilis.CenteredImageSpan;
import com.shif.peterson.tizik.utilis.SelectableItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MusicUploadLastStepAdapter extends RecyclerView.Adapter<MusicUploadLastStepAdapter.MusicUploadLastStepViewHolder>{

    private List<SelectableItem> audio_artisteList;
    private Context mcontext;
    List<Categorie> cagorieselectedlist;
    Glide glide;
    final AudioDeleteHandler audioDeleteHandler;
    private List<Categorie> categories;



    public interface AudioDeleteHandler{

        void onAudioDeleted(int position);
    }


    public MusicUploadLastStepAdapter( Context mcontext, List<SelectableItem> audio_artisteList, List<Categorie> categories,AudioDeleteHandler audioDeleteHandler) {
        this.audio_artisteList = audio_artisteList;
        this.mcontext = mcontext;
        this.categories = categories;
        this.audioDeleteHandler = audioDeleteHandler;

        cagorieselectedlist = new ArrayList<>();

    }

    @NonNull
    @Override
    public MusicUploadLastStepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_musique_selected, viewGroup, false);
        return new MusicUploadLastStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicUploadLastStepViewHolder holder, int i) {

        holder.imgposter.setImageResource(R.drawable.ic_tizik_logo_svg_gray);
        final Audio_Artiste audio_artiste = audio_artisteList.get(i);


        if (audio_artiste != null){

            if (audio_artiste.getUrl_poster()!=null){
                
                long songId = Long.parseLong(audio_artiste.getId_musique());
                Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songId);
                String[] dataColumn = {MediaStore.Audio.Media.DATA};
                Cursor coverCursor = mcontext.getContentResolver().query(songUri, dataColumn, null, null, null);
                coverCursor.moveToFirst();
                int dataIndex = coverCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                String filePath = coverCursor.getString(dataIndex);
                coverCursor.close();
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(filePath);
                byte[] coverBytes = retriever.getEmbeddedPicture();
                Bitmap songCover;
                if (coverBytes!=null){
                    //se l'array di byte non è vuoto, crea una bitmap
                    songCover = BitmapFactory.decodeByteArray(coverBytes, 0, coverBytes.length);

                    RequestOptions glideOptions = new RequestOptions()
                            .centerCrop()
                            .error(R.drawable.ic_tizik_logo_svg_gray)
                            .placeholder(R.drawable.ic_tizik_logo_svg_gray);


                    Glide.with(mcontext)
                            .load(songCover)
                            .apply(glideOptions)
                            .transition(withCrossFade())
                            .into(holder.imgposter);

                    audio_artiste.setUrl_poster(filePath);
                }

                else{
                    songCover=null;
                }

            }

            holder.txtTitreMusique.setText(audio_artiste.getTitre_musique());

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis((long)audio_artiste.getDuree_musique());
            String date = DateFormat.format("mm:ss", cal).toString();
            holder.txtduree.setText(date);


            if (audio_artiste.getPrix() == 0){

                holder.txtprice.setText(mcontext.getString(R.string.gratuit));
                holder.txtprice.setVisibility(View.GONE);

            }else{

                holder.txtprice.setText(String.valueOf(audio_artiste.getPrix()));

            }

            holder.txtnomArtiste.setText(((SelectableItem) audio_artiste).getArtiste());

            holder.initAutoCompleteTextView(categories, audio_artiste);

        }

    }


    @Override
    public int getItemCount() {
        return (null != audio_artisteList ? audio_artisteList.size() : 0);
    }

    public class MusicUploadLastStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgposter;
        private ImageView imgmore;
        private TextView txtTitreMusique;
        private TextView txtnomArtiste;
        private TextView txtduree;
        private TextView txtprice;
        private AppCompatMultiAutoCompleteTextView contactAutoCompleteTextView;
        private ImageView imgcheck;
        private ConstraintLayout constraintLayout;


        public MusicUploadLastStepViewHolder(@NonNull View itemView) {
            super(itemView);

            contactAutoCompleteTextView = itemView.findViewById(R.id.recipient_auto_complete_text_view);
            imgposter = itemView.findViewById(R.id.imgmusique);
            imgmore = itemView.findViewById(R.id.imgmore);
            txtTitreMusique = itemView.findViewById(R.id.txtnommusique);
            txtnomArtiste = itemView.findViewById(R.id.txtnomartiste);
            txtduree = itemView.findViewById(R.id.txtduree);
            txtprice = itemView.findViewById(R.id.txtprix);
            imgcheck = itemView.findViewById(R.id.imgcheck);
            constraintLayout = itemView.findViewById(R.id.constraint);



            imgmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(mcontext, imgmore);
                    popupMenu.getMenuInflater().inflate(R.menu.list_selected_musique_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            int id = item.getItemId();

                            switch (id){

                                case R.id.nav_delete:

                                    Audio_Artiste audio_artiste = audio_artisteList.get(getAdapterPosition());
                                    audio_artisteList.remove(audio_artiste);
                                    audioDeleteHandler.onAudioDeleted(getAdapterPosition());
                                    if(getAdapterPosition() == 0)
                                        notifyDataSetChanged();
                                        else
                                        notifyItemRemoved(getAdapterPosition());

                                    return true;
                                default:
                                    return false;

                            }

                        }
                    });

                    popupMenu.show();

                }
            });

            itemView.setOnClickListener(this);
        }


        public void initAutoCompleteTextView(final List<Categorie> categories, final Audio_Artiste audio_artiste){

            boolean expanded = audio_artiste.isSelected();
            constraintLayout.setVisibility(expanded ? View.VISIBLE : View.INVISIBLE);

            contactAutoCompleteTextView.setAdapter(new CategorieAdapter(mcontext,
                    R.layout.category_autocomplete, categories));
          contactAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            // Minimum number of characters the user has to type before the drop-down list is shown
            contactAutoCompleteTextView.setThreshold(1);
            contactAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Categorie selectedContact = (Categorie) adapterView.getItemAtPosition(i);

                    if (audio_artiste.selectedCategorie.contains(selectedContact)){

                        int cursorPosition = contactAutoCompleteTextView.getSelectionStart();
                        // Name length plus a space and a comma corresponding to the Token
                        int spanLength = selectedContact.getNom_categorie().length() + 2;
                        Editable text = contactAutoCompleteTextView.getText();

                        text.setSpan(new StrikethroughSpan(), cursorPosition - spanLength, cursorPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        text.setSpan(new ForegroundColorSpan(mcontext.getResources().getColor(R.color.colorAccent)), cursorPosition - spanLength, cursorPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


                   }else{

                       ((SelectableItem) audio_artiste).selectedCategorie.add(selectedContact);
                       createRecipientChip(selectedContact, audio_artiste);

                   }

                }
            });
        }

        public void createRecipientChip(Categorie selectedContact, Audio_Artiste audio_artiste){


            ChipDrawable chip = ChipDrawable.createFromResource(mcontext, R.xml.standalone_chip);

            CenteredImageSpan span = new CenteredImageSpan(chip);
            int cursorPosition = contactAutoCompleteTextView.getSelectionStart();
            // Name length plus a space and a comma corresponding to the Token
            int spanLength = selectedContact.getNom_categorie().length() + 2;
            Editable text = contactAutoCompleteTextView.getText();

            chip.setChipIcon(ContextCompat.getDrawable(mcontext, R.drawable.ic_tizik_logo_svg));

            chip.setText(selectedContact.getNom_categorie());
            chip.setBounds(0, 0, chip.getIntrinsicWidth(), chip.getIntrinsicHeight());
            text.setSpan(span, cursorPosition - spanLength, cursorPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            if (audio_artiste.getSelectedCategorie().size() == 3){

                imgcheck.setVisibility(View.VISIBLE);
                ((Animatable) imgcheck.getDrawable()).start();
                audio_artiste.setSelected(false);
                audio_artiste.setSelectedCategorie(cagorieselectedlist);
                constraintLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        notifyItemChanged(getAdapterPosition());


                    }
                }, 800);


                cagorieselectedlist = new ArrayList<>();


            }else{


                cagorieselectedlist.add(selectedContact);


            }


        }

        @Override
        public void onClick(View v) {

        }

    }
}
