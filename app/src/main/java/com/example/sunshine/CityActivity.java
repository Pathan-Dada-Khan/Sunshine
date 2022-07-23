package com.example.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sunshine.adapters.CityAdapter;
import com.example.sunshine.data.WeatherContract.WeatherEntry;

public class CityActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CityAdapter.ItemClickListener, CityAdapter.ItemLongClickListener {

    private static final int WEATHER_LOADER = -3;

    RecyclerView mCityList;

    public CityAdapter mCityAdapter;

    Cursor mCursor;

    Toast deleteToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        mCityList = (RecyclerView) findViewById(R.id.cities_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCityList.setLayoutManager(linearLayoutManager);
        mCityList.setHasFixedSize(true);
        mCityAdapter = new CityAdapter(this);
        mCityList.setAdapter(mCityAdapter);

        getLoaderManager().initLoader(WEATHER_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.add_city:
                if(mCityAdapter.getItemCount() < 10) {
                    startActivity(new Intent(this, SearchActivity.class));
                } else {
                    Toast.makeText(this, "Please Delete some cities.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mCursor.getCount() == 0 && ContextCompat.checkSelfPermission(CityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            finishAffinity();
        }
    }

    @Override
    public void setOnItemClickListener(int position) {
        Intent result = new Intent();
        result.putExtra("position", position+1);
        setResult(RESULT_OK, result);
        onBackPressed();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String [] projection = {
                WeatherEntry._ID,
                WeatherEntry.COLUMN_LOCALITY,
                WeatherEntry.COLUMN_COUNTRY,
                WeatherEntry.COLUMN_LATITUDE,
                WeatherEntry.COLUMN_LONGITUDE,
                WeatherEntry.COLUMN_MIN,
                WeatherEntry.COLUMN_MAX,
                WeatherEntry.COLUMN_ICON
        };

        Uri uri = ContentUris.withAppendedId(WeatherEntry.CONTENT_URI, 1);

        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mCityAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCityAdapter.swapCursor(null);
    }

    @Override
    public boolean setOnItemLongClickListener(int position, long id) {

        mCursor.moveToPosition(position);

        Integer localityColumn = mCursor.getColumnIndex(WeatherEntry.COLUMN_LOCALITY);
        String locality = mCursor.getString(localityColumn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Delete city " + locality + " ?");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Uri currentUri = ContentUris.withAppendedId(WeatherEntry.CONTENT_URI, id);
                int rowsDeleted = getContentResolver().delete(currentUri, null, null);

                if(rowsDeleted == 0) {
                    Toast.makeText(CityActivity.this, "Error with deleting city", Toast.LENGTH_SHORT).show();
                } else {
                    if (deleteToast != null) {
                        deleteToast.cancel();
                    }
                    deleteToast = Toast.makeText(CityActivity.this, "City deleted", Toast.LENGTH_SHORT);
                    deleteToast.show();
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }
}