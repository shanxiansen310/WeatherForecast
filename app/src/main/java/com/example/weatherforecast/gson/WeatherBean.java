package com.example.weatherforecast.gson;

import com.example.weatherforecast.database.Weather;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherBean{

    @SerializedName("daily")
    private List<Weather> mWeatherList;

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        mWeatherList = weatherList;
    }
}
