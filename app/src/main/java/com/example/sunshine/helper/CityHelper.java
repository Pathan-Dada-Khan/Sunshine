package com.example.sunshine.helper;

public class CityHelper {

    private String cityName;
    private String countryName;
    private double latitude;
    private double longitude;

    public CityHelper(String cityName, String countryName, double latitude, double longitude) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryName(){
        return countryName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
