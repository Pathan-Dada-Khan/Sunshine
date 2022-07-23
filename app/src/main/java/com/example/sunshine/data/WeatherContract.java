package com.example.sunshine.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.sun_shine";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_WEATHER);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + '/' + CONTENT_AUTHORITY + '/' + PATH_WEATHER ;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + '/' + CONTENT_AUTHORITY + '/' + PATH_WEATHER ;

        public final static String TABLE_NAME = "weather";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LOCALITY = "locality";
        public final static String COLUMN_COUNTRY = "country";
        public final static String COLUMN_LATITUDE = "latitude";
        public final static String COLUMN_LONGITUDE = "longitude";
        public final static String COLUMN_MAX = "max";
        public final static String COLUMN_MIN = "min";
        public final static String COLUMN_ICON = "icon";
    }

}
