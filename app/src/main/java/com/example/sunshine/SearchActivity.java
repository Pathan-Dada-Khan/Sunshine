package com.example.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sunshine.adapters.TopCitiesAdapter;
import com.example.sunshine.data.WeatherContract.WeatherEntry;
import com.example.sunshine.helper.CityHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements TopCitiesAdapter.ListItemClickListener{

    RecyclerView topCities;
    RecyclerView topCitiesWorld;

    TopCitiesAdapter topCitiesAdapter;

    LinearLayout linearLayout;
    Button searchResult;
    SearchView searchView;

    String addressLine;
    String locality;
    String country;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        linearLayout = findViewById(R.id.top_cities_layout);

        ArrayList<CityHelper> cityList = new ArrayList<>();
        cityList.add(new CityHelper("Delhi", "India", 28.7041,77.1025));
        cityList.add(new CityHelper("Kolkata","India",22.5726,88.3639));
        cityList.add(new CityHelper("Mumbai","India",19.0760,72.8777));
        cityList.add(new CityHelper("Hyderabad","India",17.3850,78.4867));
        cityList.add(new CityHelper("Bengaluru","India",12.9716,77.5946));
        cityList.add(new CityHelper("Chennai","India",13.0827,80.2707));


        topCities = (RecyclerView) findViewById(R.id.top_cities);
        StaggeredGridLayoutManager staggeredCities = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        topCities.setLayoutManager(staggeredCities);
        topCities.setHasFixedSize(true);
        topCitiesAdapter = new TopCitiesAdapter(cityList, SearchActivity.this);
        topCities.setAdapter(topCitiesAdapter);

        ArrayList<CityHelper> topCitiesWorldList = new ArrayList<>();

        topCitiesWorldList.add(new CityHelper("London", "England",51.5072,0.1276));
        topCitiesWorldList.add(new CityHelper("Paris","France",48.8566,2.3522));
        topCitiesWorldList.add(new CityHelper("New York", "USA",40.7128,74.0060));
        topCitiesWorldList.add(new CityHelper("United States","USA",37.0902,95.7129));
        topCitiesWorldList.add(new CityHelper("Sydney","Australia",33.8688,151.2093));
        topCitiesWorldList.add(new CityHelper("Singapore","Singapore",1.3521,103.8198));

        topCitiesWorld = (RecyclerView) findViewById(R.id.top_cities_world);
        StaggeredGridLayoutManager staggeredCitiesWorld = new StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL);
        topCitiesWorld.setLayoutManager(staggeredCitiesWorld);
        topCitiesWorld.setHasFixedSize(true);
        topCitiesAdapter = new TopCitiesAdapter(topCitiesWorldList, SearchActivity.this);
        topCitiesWorld.setAdapter(topCitiesAdapter);

        searchResult = (Button) findViewById(R.id.search_result);

        searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchResult.setVisibility(View.GONE);
                Geocoder geocoder = new Geocoder(SearchActivity.this);
                List<Address> addressList;
                try {
                    linearLayout.setVisibility(View.GONE);
                    searchResult.setVisibility(View.VISIBLE);
                    addressList = geocoder.getFromLocationName(query,1);
                    if(addressList != null && addressList.size() > 0){
                        addressLine = addressList.get(0).getAddressLine(0);
                        locality = addressList.get(0).getLocality();
                        country = addressList.get(0).getCountryName();
                        latitude = addressList.get(0).getLatitude();
                        longitude = addressList.get(0).getLongitude();
                        searchResult.setText(addressLine);
                    } else {
                        searchResult.setText("No results!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    linearLayout.setVisibility(View.VISIBLE);
                    searchResult.setVisibility(View.GONE);
                }
                return true;
            }
        });

        searchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchResult.getText().equals("No results!")) return;
                if(locality == null)locality = addressLine;
                addCity(locality, country, latitude, longitude);
            }
        });
    }
    private void addCity(String locality, String country, Double latitude, Double longitude) {

        String selection = WeatherEntry.COLUMN_LATITUDE + "=?" + " AND " + WeatherEntry.COLUMN_LONGITUDE + "=?";

        String[] selectionArgs = {
                String.valueOf(latitude),
                String.valueOf(longitude)
        };

        String[] projection = {
                WeatherEntry._ID,
        };

        Cursor cursor = getContentResolver().query(WeatherEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        if (cursor.getCount() > 0) {
            Toast.makeText(this, "City already added", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(WeatherEntry.COLUMN_LOCALITY, locality);
        values.put(WeatherEntry.COLUMN_COUNTRY, country);
        values.put(WeatherEntry.COLUMN_LATITUDE, latitude);
        values.put(WeatherEntry.COLUMN_LONGITUDE, longitude);
        values.put(WeatherEntry.COLUMN_ICON,"");

        Uri newUri = getContentResolver().insert(WeatherEntry.CONTENT_URI, values);

        if (newUri == null){
            Toast.makeText(this,"Error in adding City",Toast.LENGTH_SHORT).show();
        }

        onBackPressed();

    }

    @Override
    public void setOnCityClickListener(int position, ArrayList<CityHelper> topCitiesList) {
        CityHelper current = topCitiesList.get(position);
        addCity(current.getCityName(), current.getCountryName(), current.getLatitude(), current.getLongitude());
    }
}