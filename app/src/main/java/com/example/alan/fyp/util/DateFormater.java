package com.example.alan.fyp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wadealanchan on 2/1/2018.
 */

public class DateFormater {
    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
