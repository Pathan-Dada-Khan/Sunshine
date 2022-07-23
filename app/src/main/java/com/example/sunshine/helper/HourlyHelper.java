package com.example.sunshine.helper;

public class HourlyHelper {

    private int temperature;
    private long time;
    private String main;
    private String icon;

    public HourlyHelper(long time, int temperature, String main, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.main = main;
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getMain() {
        return main;
    }

    public String getIcon(){
        return icon;
    }

}
