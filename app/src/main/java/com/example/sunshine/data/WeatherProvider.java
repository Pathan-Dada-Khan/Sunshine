package com.example.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.sunshine.data.WeatherContract.WeatherEntry;

public class WeatherProvider extends ContentProvider {

    private static final int WEATHER = 100;
    private static final int WEATHER_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER, WEATHER);
        sUriMatcher.addURI(WeatherContract.CONTENT_AUTHORITY, WeatherContract.PATH_WEATHER + "/#", WEATHER_ID);
    }

    private WeatherDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        mDbHelper = new WeatherDbHelper(getContext());
        return true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                cursor = db.query(WeatherEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            break;
            case WEATHER_ID:
                selection = WeatherEntry._ID + "!=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(WeatherEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                return WeatherEntry.CONTENT_LIST_TYPE;
            case WEATHER_ID:
                return WeatherEntry.CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(WeatherEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case WEATHER:
                rowsDeleted = db.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WEATHER_ID:
                selection = WeatherEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(WeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }

        if ( rowsDeleted != 0 ){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        selection = WeatherEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        int rowUpdated = db.update(WeatherEntry.TABLE_NAME, values, selection, selectionArgs);

        return rowUpdated;
    }
}
