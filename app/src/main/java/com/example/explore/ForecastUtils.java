package com.example.explore;


import java.text.SimpleDateFormat;
import java.util.Locale;

public class ForecastUtils {
    private static final int DAY_TO_MILLIS = 24* 60* 60 * 1000;
    public static String getDay(int position){
        String pattern = "dd MMMM";
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(pattern, Locale.ENGLISH);

        String date = simpleDateFormat.format(System.currentTimeMillis()+ position*DAY_TO_MILLIS);
        return date;
    }

    public static String getCelsiusTempFromKelvin(double kelvins){
        return (int)(kelvins - 273.1) + " °C";
    }
}
