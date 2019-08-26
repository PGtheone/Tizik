package com.shif.peterson.tizik.utilis;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.shif.peterson.tizik.model.Categorie;

import java.util.Arrays;
import java.util.List;

public class UtilPreferences {

    public static final String mypreference = "mypref";
    public static final String nom_categorie = "nomcategoriekey";
    static SharedPreferences sharedpreferences;
    static SharedPreferences.Editor editor;
    String TAG = UtilPreferences.class.getSimpleName();

    public static void saveUserTendance(Context context, List<Categorie> listTensance){
        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        setList(nom_categorie, listTensance);
    }

    public static void setList(String key, List<Categorie> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }

    public static void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }


    public static List<Categorie> getUserTendance(Context context){

        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonText = sharedpreferences.getString(mypreference, null);
        Categorie[] text = gson.fromJson(jsonText, Categorie[].class);


        return Arrays.asList(text);
    }
}
