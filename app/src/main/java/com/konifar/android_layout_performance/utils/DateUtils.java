package com.konifar.android_layout_performance.utils;

import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();
    private static final String DATE_FORMAT_TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    private static Date convertToDate(String twitterDate) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT_TWITTER, Locale.ENGLISH);
        try {
            return sf.parse(twitterDate);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    private static String getRelativeTime(Date date) {
        if (date != null) {
            PrettyTime p = new PrettyTime();
            return p.format(date);
        } else {
            return "";
        }
    }

    public static String getTwitterRelativeTime(String twitterDate) {
        return getRelativeTime(convertToDate(twitterDate));
    }

}
