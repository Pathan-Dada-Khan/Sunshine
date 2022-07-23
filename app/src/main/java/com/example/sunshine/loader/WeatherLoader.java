package com.example.sunshine.loader;

import android.content.Context;
import android.content.AsyncTaskLoader;

import com.example.sunshine.helper.WeatherHelper;
import com.example.sunshine.utilities.OpenWeatherUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherLoader extends AsyncTaskLoader<HashMap<Integer, WeatherHelper>> {

    private Context context;
    private final long id;
    private final String mUrl;
    private HashMap<Integer, WeatherHelper> weatherHelperArrayList;

    public WeatherLoader(Context context, long id, String url, HashMap<Integer, WeatherHelper> weatherHelperArrayList) {
        super(context);
        this.context = context;
        this.id = id;
        this.weatherHelperArrayList = weatherHelperArrayList;
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public HashMap<Integer, WeatherHelper> loadInBackground() {
        if(mUrl == null) {
            return null;
        }
        OpenWeatherUtils openWeatherUtils = new OpenWeatherUtils(context, id);
        weatherHelperArrayList = openWeatherUtils.fetchOpenWeatherData(mUrl, weatherHelperArrayList);
        return weatherHelperArrayList;
    }
}
