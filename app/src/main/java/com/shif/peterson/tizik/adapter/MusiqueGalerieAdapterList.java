package com.shif.peterson.tizik.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shif.peterson.tizik.R;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Medium;
import com.shif.peterson.tizik.customfonts.MyTextView_Ubuntu_Regular;
import com.shif.peterson.tizik.model.Audio_Artiste;
import com.shif.peterson.tizik.utilis.OnItemSelectedListener;
import com.shif.peterson.tizik.utilis.RoundishImageView;
import com.shif.peterson.tizik.utilis.SelectableItem;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MusiqueGalerieAdapterList extends RecyclerView.Adapter<MusiqueGalerieAdapterList.MusiqueGalerieVListiewHolder> implements OnItemSelectedListener{

    private final static int FADE_DURATION = 1000;
    private static final int TYPE_ITEM = 1;
    private static int IMG_SELECTED_LIMIT = 5;
    private static int IMG_SELECTED = 0;
    private final Cursor mValues;
    //   private List<ImageItem> imageItemList;
    Context context;
    Glide glide;
    Bundle bundle=new Bundle();
    OnItemSelectedListener listener;
    List<SelectableItem> audioArtisteList;
    int DATA;
    private boolean isMultiSelectionEnabled = true;

    public MusiqueGalerieAdapterList(OnItemSelectedListener listener, Context context,Cursor items, boolean isMultiSelectionEnabled) {

        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        this.context = context;
        audioArtisteList = new ArrayList<>();

        mValues = items;
        //get columns
        int titleColumn = mValues.getColumnIndex
                (android.provider.MediaStore.Audio.Media.TITLE);
        int idColumn = mValues.getColumnIndex
                (android.provider.MediaStore.Audio.Media._ID);
        int artistColumn = mValues.getColumnIndex
                (android.provider.MediaStore.Audio.Media.ARTIST);
        int duration = mValues.getColumnIndex
                (MediaStore.Audio.Media.DURATION);
        int albumColumn = mValues.getColumnIndex
                (MediaStore.Audio.Media.ALBUM);
        DATA =  mValues.getColumnIndex
                (MediaStore.Audio.Media._ID);
        int PATH = mValues.getColumnIndex( MediaStore.Audio.Media.DATA);
//        int poster = mValues.getColumnIndex
//                (android.provider.MediaStore.Audio.AudioColumns.)

        if(mValues!= null && mValues.moveToFirst()){


            do {
                String thisId = java.lang.String.valueOf(mValues.getLong(idColumn));
                String thisTitle = mValues.getString(titleColumn);
                String thisArtist = mValues.getString(artistColumn);
                double durationMusique = mValues.getDouble(duration);
                String urlMusique = String.valueOf(mValues.getString(PATH));
                String album = mValues.getString(albumColumn);

                long songId = Long.parseLong(thisId);
                Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songId);
                String[] dataColumn = {MediaStore.Audio.Media.DATA};
                Cursor coverCursor = context.getContentResolver().query(songUri, dataColumn, null, null, null);
                coverCursor.moveToFirst();
                int dataIndex = coverCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                String posterPath = coverCursor.getString(dataIndex);



                Audio_Artiste audio_artiste = new Audio_Artiste(thisId, urlMusique, thisTitle, thisArtist, durationMusique, urlMusique);
                audio_artiste.setUrl_poster(posterPath);
                audioArtisteList.add(new SelectableItem(audio_artiste, thisArtist, album, false));


            }
            while (mValues.moveToNext());
        }

    }

    @NonNull
    @Override
    public MusiqueGalerieVListiewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View view = LayoutInflater.from(context).inflate(R.layout.item_galerie_list,viewGroup, false);
        return new MusiqueGalerieVListiewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MusiqueGalerieVListiewHolder holder, int position) {

        SelectableItem selectableItem = audioArtisteList.get(position);

        holder.placeholderView.setVisibility(selectableItem.isSelected() ? View.VISIBLE : View.INVISIBLE);
        holder.imggalerie.setImageResource(R.drawable.ic_tizik_logo_svg_gray);



        long songId = Long.parseLong(selectableItem.getId_musique());
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,songId);
        String[] dataColumn = {MediaStore.Audio.Media.DATA};
        Cursor coverCursor = context.getContentResolver().query(songUri, dataColumn, null, null, null);
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

            Glide.with(context)
                    .load(songCover)
                    .apply(glideOptions)
                    .transition(withCrossFade())
                    .into(holder.imggalerie);

            holder.imggalerie.setClipToOutline(true);


        }

        else{
            songCover=null;
        }

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());

        holder.txttitremusique.setText(selectableItem.getTitre_musique());
        holder.txtartiste.setText(selectableItem.getArtiste());


    }


    @Override
    public int getItemCount() {
        return audioArtisteList.size();
    }

    public List<Audio_Artiste> getSelectedItems() {

        List<Audio_Artiste> selectedItems = new ArrayList<>();
        for (SelectableItem item : audioArtisteList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public void onItemSelected(SelectableItem item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableItem selectableItem : audioArtisteList) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }


    public class MusiqueGalerieVListiewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public static final int MULTI_SELECTION = 2;
        public static final int SINGLE_SELECTION = 1;
        final RoundishImageView imggalerie;
        final ImageView placeholderView;
        final MyTextView_Ubuntu_Medium txttitremusique;
        final MyTextView_Ubuntu_Regular txtartiste;
        SelectableItem mItem;
        OnItemSelectedListener itemSelectedListener;


        public MusiqueGalerieVListiewHolder(@NonNull View itemView, OnItemSelectedListener listener) {
            super(itemView);
            itemSelectedListener = listener;

            imggalerie = itemView.findViewById(R.id.imggalerie);
            placeholderView = itemView.findViewById(R.id.placeholder_view);
            txttitremusique = itemView.findViewById(R.id.txttitle);
            txtartiste = itemView.findViewById(R.id.txtartiste);

            itemView.setOnClickListener(this);
        }

        public void setChecked(boolean value) {
            if (value) {
                placeholderView.setVisibility(View.VISIBLE);
//                textView.setBackgroundColor(Color.LTGRAY);
            } else {
                placeholderView.setVisibility(View.INVISIBLE);
                // textView.setBackground(null);
            }
            mItem.setSelected(value);

        }

        @Override
        public void onClick(View v) {

            if (mItem.isSelected()) {
                setChecked(false);
            } else {
                setChecked(true);
            }
            itemSelectedListener.onItemSelected(mItem);


        }
    }
}
