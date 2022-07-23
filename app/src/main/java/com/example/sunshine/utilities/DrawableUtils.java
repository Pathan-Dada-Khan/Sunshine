package com.example.sunshine.utilities;

import com.example.sunshine.R;

public class DrawableUtils {

    public static int getMainBackgroundDrawable(String icon) {

        if ( icon.equals("01d") ) {
            return R.drawable.main_background_sun_day;
        } else if ( icon.equals("01n") ) {
            return R.drawable.main_background_sun_night;
        } else if ( icon.equals("02d") ) {
            return R.drawable.main_background_shade_day;
        } else if ( icon.equals("02n") ) {
            return R.drawable.main_background_shade_night;
        } else if ( icon.equals("03d") ) {
            return R.drawable.main_background_cloudy_day;
        } else if ( icon.equals("03n") ) {
            return R.drawable.main_background_cloudy_night;
        } else if ( icon.equals("04d") ) {
            return R.drawable.main_background_cloudy_day;
        } else if ( icon.equals("04n") ) {
            return R.drawable.main_background_cloudy_night;
        } else if ( icon.equals("09d") ) {
            return R.drawable.main_background_rain_day;
        } else if ( icon.equals("09n") ) {
            return R.drawable.main_background_rain_night;
        } else if ( icon.equals("10d") ) {
            return R.drawable.main_background_rain_day;
        } else if ( icon.equals("10n") ) {
            return R.drawable.main_background_rain_night;
        } else if ( icon.equals("11d") ) {
            return R.drawable.main_background_thunderrain_day;
        } else if ( icon.equals("11n") ) {
            return R.drawable.main_background_thunderrain_night;
        } else if ( icon.equals("13d") ) {
            return R.drawable.main_background_snow_day;
        } else if ( icon.equals("13n") ) {
            return R.drawable.main_background_snow_night;
        } else if ( icon.equals("50d") ) {
            return R.drawable.main_background_fog_day;
        } else if ( icon.equals("50n") ) {
            return R.drawable.main_background_fog_night;
        } else {
            return R.drawable.main_background_default_bg;
        }

    }

    public static int getCityBackgroundDrawable(String icon) {

        if ( icon.equals("01d") ) {
            return R.drawable.citylist_sun_day;
        } else if ( icon.equals("01n") ) {
            return R.drawable.citylist_sun_night;
        } else if ( icon.equals("02d") ) {
            return R.drawable.citylist_shade_day;
        } else if ( icon.equals("02n") ) {
            return R.drawable.citylist_shade_night;
        } else if ( icon.equals("03d") ) {
            return R.drawable.citylist_cloudy_day;
        } else if ( icon.equals("03n") ) {
            return R.drawable.citylist_cloudy_night;
        } else if ( icon.equals("04d") ) {
            return R.drawable.citylist_cloudy_day;
        } else if ( icon.equals("04n") ) {
            return R.drawable.citylist_cloudy_night;
        } else if ( icon.equals("09d") ) {
            return R.drawable.citylist_rain_day;
        } else if ( icon.equals("09n") ) {
            return R.drawable.citylist_rain_night;
        } else if ( icon.equals("10d") ) {
            return R.drawable.citylist_rain_day;
        } else if ( icon.equals("10n") ) {
            return R.drawable.citylist_rain_night;
        } else if ( icon.equals("11d") ) {
            return R.drawable.citylist_thunderrain_day;
        } else if ( icon.equals("11n") ) {
            return R.drawable.citylist_thunderrain_night;
        } else if ( icon.equals("13d") ) {
            return R.drawable.citylist_snow_day;
        } else if ( icon.equals("13n") ) {
            return R.drawable.citylist_snow_night;
        } else if ( icon.equals("50d") ) {
            return R.drawable.citylist_fog_day;
        } else if ( icon.equals("50n") ) {
            return R.drawable.citylist_fog_night;
        } else {
            return R.drawable.citylist_sun_day;
        }

    }

    public static int getWeatherIcon(String icon) {

        if ( icon.equals("01d") ) {
            return R.drawable.weather_sun;
        } else if ( icon.equals("01n") ) {
            return R.drawable.weather_moon;
        } else if ( icon.equals("02d") ) {
            return R.drawable.weather_cloud_sun;
        } else if ( icon.equals("02n") ) {
            return R.drawable.weather_cloud_moon;
        } else if ( icon.equals("03d") ) {
            return R.drawable.weather_cloud;
        } else if ( icon.equals("03n") ) {
            return R.drawable.weather_cloud;
        } else if ( icon.equals("04d") ) {
            return R.drawable.weather_clouds;
        } else if ( icon.equals("04n") ) {
            return R.drawable.weather_clouds;
        } else if ( icon.equals("09d") ) {
            return R.drawable.weather_rainy;
        } else if ( icon.equals("09n") ) {
            return R.drawable.weather_rainy;
        } else if ( icon.equals("10d") ) {
            return R.drawable.weather_rainy_sun;
        } else if ( icon.equals("10n") ) {
            return R.drawable.weather_rainy_moon;
        } else if ( icon.equals("11d") ) {
            return R.drawable.weather_thunder;
        } else if ( icon.equals("11n") ) {
            return R.drawable.weather_thunder;
        } else if ( icon.equals("13d") ) {
            return R.drawable.weather_snow;
        } else if ( icon.equals("13n") ) {
            return R.drawable.weather_snow;
        } else if ( icon.equals("50d") ) {
            return R.drawable.weather_fog;
        } else if ( icon.equals("50n") ) {
            return R.drawable.weather_fog;
        } else {
            return R.drawable.weather_sun;
        }

    }
}
