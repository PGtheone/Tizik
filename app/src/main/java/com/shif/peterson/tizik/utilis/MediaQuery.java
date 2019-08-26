package com.shif.peterson.tizik.utilis;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaQuery {

    private Context context;
    private int count = 0;
    private Cursor cursor;

    public MediaQuery(Context context){
        this.context=context;
    }


    public Cursor getAllMusique(){

     cursor = null;
     String sortOrder = MediaStore.Audio.Media.DATE_ADDED +" Desc Limit 10";

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, sortOrder);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            cursor =  musicCursor;
        }

return musicCursor;
    }

}
