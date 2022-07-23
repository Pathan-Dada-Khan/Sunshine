package com.example.sunshine.helper;

public class DailyHelper {

    private long date;
    private String icon;
    private int maxTemperature;
    private int minTemperature;

    public DailyHelper(long date, String icon, int maxTemperature, int minTemperature) {
        this.date = date;
        this.icon = icon;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    public long getDate() {
        return date;
    }

    public String getIcon(){return icon;}

    public int getMaxTemperature() {
        return maxTemperature - 273;
    }

    public int getMinTemperature() {
        return minTemperature - 273;
    }
}
