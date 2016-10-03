package com.mycompany.webtest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zoomout on 10/4/16.
 */
public class TimeUtil {
    public static String getTimestamp() {
        Date date = new java.util.Date();
        return String.valueOf(date.getTime());
    }

    public static String getFormattedDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM_dd_yyyy_h_mm_ss");
        return sdf.format(date);
    }
}
