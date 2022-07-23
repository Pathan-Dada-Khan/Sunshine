package com.example.sunshine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.content.Loader;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sunshine.adapters.ViewPager2Adapter;
import com.example.sunshine.data.WeatherContract.WeatherEntry;
import com.example.sunshine.helper.WeatherHelper;
import com.example.sunshine.utilities.DrawableUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, ViewPager2Adapter.LoaderInterface, ViewPager2Adapter.UpdateBackgroundImage {

    private static final int WEATHER_LOADER = -2;
    private static final int LOCATION_LOADER = -1;
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int SET_CURRENT_CITY = 200;
    FusedLocationProviderClient fusedLocationProviderClient;

    public String locality;
    public String country;
    public double latitude;
    public double longitude;

    public ViewPager2 viewPager2;
    public TabLayout tabLayout;
    public HashMap<Integer, WeatherHelper> weatherHelperArrayList;

    public ViewPager2Adapter viewPager2Adapter;
    public LoaderManager loaderManager;

    public Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();
        setContentView(R.layout.activity_main);

        loaderManager = getLoaderManager();
        weatherHelperArrayList = new HashMap<>();

        Cursor cursor = getContentResolver().query(WeatherEntry.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();

            values.put(WeatherEntry.COLUMN_LOCALITY, "");
            values.put(WeatherEntry.COLUMN_COUNTRY, "");
            values.put(WeatherEntry.COLUMN_LATITUDE, 0);
            values.put(WeatherEntry.COLUMN_LONGITUDE, 0);
            values.put(WeatherEntry.COLUMN_ICON, "");

            getContentResolver().insert(WeatherEntry.CONTENT_URI, values);
        }


        viewPager2 = findViewById(R.id.view_pager_2);
        viewPager2Adapter = new ViewPager2Adapter(this, weatherHelperArrayList);
        viewPager2.setAdapter(viewPager2Adapter);

        tabLayout = findViewById(R.id.tab_indicator);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setIcon(R.drawable.menu_tab_indicator)
        ).attach();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mCursor = getContentResolver().query(WeatherEntry.CONTENT_URI, null, null, null);
                }

                if (mCursor != null) {
                    if (mCursor.getCount() > 0) {

                        mCursor.moveToPosition(position);
                        int columnLocality = mCursor.getColumnIndex(WeatherEntry.COLUMN_LOCALITY);
                        int columnIcon = mCursor.getColumnIndex(WeatherEntry.COLUMN_ICON);

                        String title = mCursor.getString(columnLocality);
                        if (title != null) {
                            if (position == 0) {
                                setTitle("" + mCursor.getString(columnLocality));
                            } else {
                                setTitle(mCursor.getString(columnLocality));
                            }
                        } else {
                            setTitle(getString(R.string.app_name));
                        }
                        int icon = DrawableUtils.getMainBackgroundDrawable(mCursor.getString(columnIcon));
                        getWindow().setBackgroundDrawableResource(icon);
                    } else if (mCursor.getCount() == 0) {
                        startActivity(new Intent(MainActivity.this, CityActivity.class));
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        getLoaderManager().initLoader(LOCATION_LOADER, null, this);

    }

    private void checkGpsStatus() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );

                            locality = addresses.get(0).getLocality();
                            country = addresses.get(0).getCountryName();
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();

                            addCurrentLocation(locality, country, latitude, longitude);

                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Error in getting Location", Toast.LENGTH_SHORT).show();
                            Cursor cursorLocation = getContentResolver().query(WeatherEntry.CONTENT_URI, null, null, null, null);
                            if (cursorLocation.getCount() == 0) {
                                startActivity(new Intent(MainActivity.this, CityActivity.class));
                            }

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Turn on GPS and restart the App", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SET_CURRENT_CITY) {
            if (resultCode == RESULT_OK && data != null) {
                int position = data.getIntExtra("position", 0);
                viewPager2.setCurrentItem(position);
            }
        }
    }

    private void addCurrentLocation(String locality, String country, double latitude, double longitude) {

        if (latitude != 0 && longitude != 0) {

            ContentValues values = new ContentValues();

            values.put(WeatherEntry.COLUMN_LOCALITY, locality);
            values.put(WeatherEntry.COLUMN_COUNTRY, country);
            values.put(WeatherEntry.COLUMN_LATITUDE, latitude);
            values.put(WeatherEntry.COLUMN_LONGITUDE, longitude);
            values.put(WeatherEntry.COLUMN_ICON, "");

            Uri uri = ContentUris.withAppendedId(WeatherEntry.CONTENT_URI, 1);

            int rowsAffected = getContentResolver().update(uri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "Error in updating location", Toast.LENGTH_SHORT).show();
            } else {
                getLoaderManager().initLoader(WEATHER_LOADER, null, this);
                setTitle(locality);
            }

        }

    }

    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkGpsStatus();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkGpsStatus();
            } else {

                if (mCursor.getCount() == 0) {
                    startActivity(new Intent(MainActivity.this, CityActivity.class));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.city:
                startActivityForResult(new Intent(MainActivity.this, CityActivity.class), SET_CURRENT_CITY);
                break;
            case R.id.share:
                if (viewPager2Adapter.scrollUp()) shareSunshineWeather();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void shareSunshineWeather() {

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View screenView = rootView.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);

        File dir = new File(getCacheDir(), "images");
        if (!dir.exists())
            dir.mkdir();
        File file = new File(dir, "sunshine_weather.png");
        try {
            FileOutputStream output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = FileProvider.getUriForFile(this, "com.example.sunshine.fileProvider", file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TEXT, "From Sunshine Weather");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Sunshine Weather"));
        } catch (Exception e) {
            Toast.makeText(this, "No Applications to Share", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                WeatherEntry._ID,
                WeatherEntry.COLUMN_LOCALITY,
                WeatherEntry.COLUMN_COUNTRY,
                WeatherEntry.COLUMN_LATITUDE,
                WeatherEntry.COLUMN_LONGITUDE,
                WeatherEntry.COLUMN_ICON
        };

        return new CursorLoader(this, WeatherEntry.CONTENT_URI, projection, WeatherEntry.COLUMN_LATITUDE + " !=0 " + " AND " + WeatherEntry.COLUMN_LONGITUDE + " !=0 ", null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        viewPager2Adapter.swapCursor(data);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(mCursor.getCount() == 0) {
                startActivityForResult(new Intent(MainActivity.this, CityActivity.class), SET_CURRENT_CITY);
            }
        }

        if (mCursor.getCount() < 2) {
            tabLayout.setVisibility(View.INVISIBLE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        viewPager2Adapter.swapCursor(mCursor);
    }

    @Override
    public LoaderManager getLoader() {
        return getLoaderManager();
    }

    @Override
    public void updateBackground(String icon, int position) {
        if (viewPager2.getCurrentItem() == position)
            getWindow().setBackgroundDrawableResource(DrawableUtils.getMainBackgroundDrawable(icon));
    }
}