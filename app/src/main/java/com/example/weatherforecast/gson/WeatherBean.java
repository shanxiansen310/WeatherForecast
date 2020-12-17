package com.example.weatherforecast.gson;

import com.example.weatherforecast.database.Weather;

import java.util.List;

public class WeatherBean{

    private List<Weather> mWeatherList;

    public List<Weather> getWeatherList() {
        return mWeatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        mWeatherList = weatherList;
    }
}
