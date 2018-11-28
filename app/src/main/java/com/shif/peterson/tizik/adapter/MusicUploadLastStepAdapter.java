package com.shif.peterson.tizik.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.SelectableItem;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MusicUploadLastStepAdapter extends RecyclerView.Adapter<MusicUploadLastStepAdapter.MusicUploadLastStepViewHolder>{

    private List<SelectableItem> audio_artisteList;
    private Context mcontext;

    Glide glide;

    final AudioDeleteHandler audioDeleteHandler;


    public interface AudioDeleteHandler{

        void onAudioDeleted(int position);
    }


    public MusicUploadLastStepAdapter( Context mcontext, List<SelectableItem> audio_artisteList, AudioDeleteHandler audioDeleteHandler) {
        this.audio_artisteList = audio_artisteList;
        this.mcontext = mcontext;
        this.audioDeleteHandler = audioDeleteHandler;
    }

    @NonNull
    @Override
    public MusicUploadLastStepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_musique_selected, viewGroup, false);

        return new MusicUploadLastStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicUploadLastStepViewHolder holder, int i) {

        Audio_Artiste audio_artiste = audio_artisteList.get(i);


        if (audio_artiste != null){

            if (audio_artiste.getUrl_poster()!=null){

                Toast.makeText(mcontext, "here", Toast.LENGTH_SHORT).show();
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
                            .error(R.color.cardview_dark_background)
                            .placeholder(android.R.drawable.stat_notify_error);


                    glide.with(mcontext)
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

           // holder.musiquecheck.setChecked(true);

            holder.txtTitreMusique.setText(audio_artiste.getTitre_musique());
//            holder.txtnomArtiste.setText(((SelectableItem) audio_artiste).getArtiste());
  //          holder.txtAlbum.setText(((SelectableItem) audio_artiste).getNomAlbum());

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis((long)audio_artiste.getDuree_musique());
            String date = DateFormat.format("mm:ss", cal).toString();
            holder.txtduree.setText(String.valueOf(date));

            holder.txtAlbum.setText(((SelectableItem) audio_artiste).getNomAlbum());

            if (audio_artiste.getPrix() == 0){

                holder.txtprice.setText(mcontext.getString(R.string.gratuit));
            }else{

                holder.txtprice.setText(String.valueOf(audio_artiste.getPrix()));

            }

            holder.txtnomArtiste.setText(((SelectableItem) audio_artiste).getArtiste());



        }

    }

    @Override
    public int getItemCount() {
        return (null != audio_artisteList ? audio_artisteList.size() : 0);
    }

    public class MusicUploadLastStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       // private CheckBox musiquecheck;
        private ImageView imgposter;
        private ImageView imgmore;
        private TextView txtTitreMusique;
        private TextView txtnomArtiste;
        private TextView txtAlbum;
        private TextView txtduree;
        private TextView txtprice;


        public MusicUploadLastStepViewHolder(@NonNull View itemView) {
            super(itemView);

            //musiquecheck = (CheckBox) itemView.findViewById(R.id.checkbox);
            imgposter = (ImageView) itemView.findViewById(R.id.imgmusique);
            imgmore = (ImageView) itemView.findViewById(R.id.imgmore);
            txtTitreMusique = (TextView) itemView.findViewById(R.id.txtnommusique);
            txtnomArtiste = (TextView) itemView.findViewById(R.id.txtnomartiste);
            txtAlbum = (TextView) itemView.findViewById(R.id.txtalbum);
            txtduree = (TextView) itemView.findViewById(R.id.txtduree);
            txtprice = (TextView) itemView.findViewById(R.id.txtprix);

//            musiquecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    //productCheckHandler.onProductChecked(getAdapterPosition());
//                    if(!isChecked){
//
//                        audioUnCheckHandler.onAudioUnChecked(getAdapterPosition());
//
//                    }else{
//
//                        audioCheckHandler.onAudioChecked(getAdapterPosition());
////
////                        if(isFirst == true && compteur < list.size()){
////
////                            compteur = compteur + 1;
////
////
////                        }else {
////
////                            isFirst = false;
////                            productCheckHandler.onProductChecked(getAdapterPosition());
////
////                        }
//
//
//
//
//                    }
//                }
//            });

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

        @Override
        public void onClick(View v) {

        }
    }
}
