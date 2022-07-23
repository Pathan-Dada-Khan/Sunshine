package com.example.sunshine.utilities;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.sunshine.data.WeatherContract;
import com.example.sunshine.helper.DailyHelper;
import com.example.sunshine.helper.HourlyHelper;
import com.example.sunshine.helper.WeatherHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

public class OpenWeatherUtils {

    private Context context;
    private static long id;
    private static String currentIcon = "";
    private static int min = 0;
    private static int max = 0;
    public OpenWeatherUtils(Context context, long id) {
        this.context = context;
        this.id = id;
    }

    public HashMap<Integer, WeatherHelper> fetchOpenWeatherData(String mUrl, HashMap<Integer, WeatherHelper> weatherHelperArrayList) {

        URL url = createUrl(mUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        extractHourlyForecast(jsonResponse,weatherHelperArrayList);

        return weatherHelperArrayList;
    }

    private static URL createUrl(String mUrl) {
        URL url = null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("OpenWeatherUtils","Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static HashMap<Integer, WeatherHelper> extractHourlyForecast(String jsonResponse, HashMap<Integer, WeatherHelper> weatherHelperArrayList) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        ArrayList<HourlyHelper> hourlyHelperArrayList = new ArrayList<>();
        ArrayList<DailyHelper> dailyHelperArrayList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            JSONObject current = root.optJSONObject("current");

            long sunrise = current.getLong("sunrise");
            long sunset = current.getLong("sunset");
            int currentTemp = current.getInt("temp");
            int feels_like = current.getInt("feels_like");
            int pressure = current.getInt("pressure");
            int humidity = current.getInt("humidity");
            double wind_speed = current.getDouble("wind_speed");

            JSONArray currentWeatherArray = current.optJSONArray("weather");

            JSONObject currentWeather = currentWeatherArray.getJSONObject(0);
            String currentMain = currentWeather.getString("main");
            currentIcon = currentWeather.getString("icon");

            JSONArray daily = root.optJSONArray("daily");

            JSONObject today = daily.optJSONObject(0);

            long dt = today.getLong("dt");

            JSONObject temp = today.optJSONObject("temp");
            max = temp.getInt("max");
            min = temp.getInt("min");

            JSONArray hourly = root.optJSONArray("hourly");

            for(int i=0;i<24;i++){

                JSONObject hour = hourly.optJSONObject(i);

                long hourlyDate = hour.getLong("dt");
                int hourlyTemp = hour.getInt("temp") - 273;

                JSONArray weatherArray = hour.optJSONArray("weather");
                JSONObject weather = weatherArray.getJSONObject(0);

                String hourlyMain = weather.getString("main");
                String hourlyIcon = weather.getString("icon");

                hourlyHelperArrayList.add(new HourlyHelper(hourlyDate,hourlyTemp,hourlyMain,hourlyIcon));

            }


            for(int i=1;i<daily.length();i++){
                JSONObject day = daily.optJSONObject(i);

                long dailyDate = day.getLong("dt");

                JSONObject dailyTemp = day.optJSONObject("temp");
                int dailyMin = dailyTemp.getInt("min");
                int dailyMax = dailyTemp.getInt("max");

                JSONArray weather = day.optJSONArray("weather");
                JSONObject weatherObject = weather.getJSONObject(0);
                String dailyIcon = weatherObject.getString("icon");

                dailyHelperArrayList.add(new DailyHelper(dailyDate,dailyIcon,dailyMax,dailyMin));
            }

            weatherHelperArrayList.put((int)id, new WeatherHelper(hourlyHelperArrayList, dailyHelperArrayList, sunrise, sunset, currentTemp, feels_like, pressure, humidity, wind_speed, currentMain, currentIcon, dt, max, min));

        } catch (JSONException e) {
            Log.e("OpenWeatherUtils","Problem parsing the weather information");
        }

        return weatherHelperArrayList;
    }

}
