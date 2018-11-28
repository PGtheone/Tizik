package com.shif.peterson.tizik.utilis;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public  class Utils {



    public static String longToDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("mm:ss", cal).toString();

        return String.valueOf(date);
    }

}
