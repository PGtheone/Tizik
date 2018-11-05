package com.shif.peterson.tizik.utilis;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.shif.peterson.tizik.model.Audio_Artiste;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MediaQuery {

    private Context context;
    private int count = 0;
    private Cursor cursor;
    List<Audio_Artiste> imageItems;

    public MediaQuery(Context context){
        this.context=context;
    }


    public Cursor getAllMusique(){

    Cursor cursor = null;

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            cursor =  musicCursor;
        }

return musicCursor;
    }


//    public List<Audio_Artiste> getallMusique() {
//
//        if(musicCursor!=null && musicCursor.moveToFirst()){
//            //get columns
//            int titleColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.TITLE);
//            int idColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media._ID);
//            int artistColumn = musicCursor.getColumnIndex
//                    (android.provider.MediaStore.Audio.Media.ARTIST);
//            int duree = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//
//            int album =  musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
//            int genre =  musicCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);
//
//
//
//        String selection = null;
//        cursor = context.getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null,
//                selection,
//                null,
//                MediaStore.Images.Media.DATE_TAKEN+" DESC");
//        imageItems = new ArrayList<ImageItem>();
//        ImageItem imageItem;
//        while (cursor.moveToNext()) {
//            imageItem = new ImageItem();
//            imageItem.setDATA(cursor.getString(1));
//            imageItem.setCREATED(getCreatedDate(cursor.getString(1)));
//            imageItems.add(imageItem);
//        }
//        return imageItems;
//    }
//
//
//    public static String getCreatedDate(String filePath){
//        File file =new File(filePath);
//        long currentTime=System.currentTimeMillis();
//        long lastmodified=file.lastModified();
//        String createdTime;
//        SimpleDateFormat formatter = new SimpleDateFormat("HH:MM dd-MM-yyyy");
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(lastmodified);
//        return formatter.format(calendar.getTime());
//    }
//    public static String getFileName(String path) {
//        if (path == null || path.length() == 0) {
//            return "";
//        }
//        int query = path.lastIndexOf('?');
//        if (query > 0) {
//            path = path.substring(0, query);
//        }
//        int filenamePos = path.lastIndexOf(File.separatorChar);
//        return (filenamePos >= 0) ? path.substring(filenamePos + 1) : path;
//    }
//

}
