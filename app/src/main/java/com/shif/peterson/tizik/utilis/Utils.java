package com.shif.peterson.tizik.utilis;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;

import androidx.core.graphics.drawable.DrawableCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public  class Utils {



    public static String longToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("mm:ss", cal).toString();

        return date;
    }


    public static double calculerMoyenne(List<Double> notes){
        double moyenne = 0.0;
        double nume = 0.0;

        List<Double> note = new ArrayList<>();
        List<Integer> occurence = new ArrayList<>();

        if(notes.size() > 0){
            for (int i=0; i<=5; i++){
                for (int j=0; j<=9; j++){
                    if(notes.contains(Double.parseDouble(i+"."+j))){

                        occurence.add(Collections.frequency(notes, Double.parseDouble(i+"."+j)));
                        note.add(Double.parseDouble(i+"."+j));
                    }
                }
            }

            if(note.size() > 0){
                List<Double> mult = new ArrayList<>();
                for (int i = 0; i < note.size(); i++){
                    nume = (note.get(i) * occurence.get(i) + nume);
                }

                moyenne = nume / notes.size();
            }

        }

        return moyenne;
    }


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }





}
