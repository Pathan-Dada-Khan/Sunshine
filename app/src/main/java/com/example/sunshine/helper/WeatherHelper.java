package com.example.sunshine.helper;

import java.util.ArrayList;

public class WeatherHelper {

    private ArrayList<HourlyHelper> hourlyHelperArrayList;
    private ArrayList<DailyHelper> dailyHelperArrayList;

    private long sunrise;
    private long sunset;
    private int currentTemp;
    private int feelsLike;
    private int pressure;
    private int humidity;
    private double windSpeed;
    private String currentMain;
    private String icon;
    private long date;
    private int max;
    private int min;

    public WeatherHelper(ArrayList<HourlyHelper> hourlyHelperArrayList, ArrayList<DailyHelper> dailyHelperArrayList, long sunrise, long sunset, int currentTemp, int feelsLike, int pressure, int humidity, double windSpeed, String currentMain, String icon, long date, int max, int min) {
        this.hourlyHelperArrayList = hourlyHelperArrayList;
        this.dailyHelperArrayList = dailyHelperArrayList;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.currentTemp = currentTemp;
        this.feelsLike = feelsLike;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.currentMain = currentMain;
        this.icon = icon;
        this.date = date;
        this.max = max - 273;
        this.min = min - 273;
    }

    public ArrayList<HourlyHelper> getHourlyHelperArrayList(){
        return hourlyHelperArrayList;
    }
    public ArrayList<DailyHelper> getDailyHelperArrayList(){
        return dailyHelperArrayList;
    }

    public long getSunrise() {
        return sunrise;
    }
    public long getSunset() {
        return sunset;
    }
    public int getCurrentTemp() {
        return currentTemp;
    }
    public int getFeelsLike() {
        return feelsLike;
    }
    public int getPressure() {
        return pressure;
    }
    public int getHumidity() {
        return humidity;
    }
    public double getWindSpeed() {
        return windSpeed;
    }
    public String getCurrentMain() {
        return currentMain;
    }
    public String getIcon() {
        return icon;
    }
    public long getDate() {
        return date;
    }
    public int getMax() {
        return max;
    }
    public int getMin() {
        return min;
    }

}
