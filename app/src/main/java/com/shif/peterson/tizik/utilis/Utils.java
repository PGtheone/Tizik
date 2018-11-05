package com.shif.peterson.tizik.utilis;

import android.content.Context;
import android.text.format.DateUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public  class Utils {



    public static String toYYYYMMDDHHMMSS(long time) {
        SimpleDateFormat format = new SimpleDateFormat("M??d??H?m??s??");
        return format.format(new Date(time));
    }

}
