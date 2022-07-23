package com.example.sunshine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sunshine.data.WeatherContract.WeatherEntry;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " ( "
                + WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WeatherEntry.COLUMN_LOCALITY + " TEXT, "
                + WeatherEntry.COLUMN_COUNTRY + " TEXT, "
                + WeatherEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, "
                + WeatherEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, "
                + WeatherEntry.COLUMN_MAX + " INT, "
                + WeatherEntry.COLUMN_MIN + " INT, "
                + WeatherEntry.COLUMN_ICON + " TEXT );";

        db.execSQL(SQL_CREATE_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
