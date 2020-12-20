package com.example.weatherforecast.database;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

public class District extends DataSupport {
    private String name;
    private String id;
    @SerializedName("lat")
    private String latitude;   /*纬度*/
    @SerializedName("lon")
    private String longitude;  /*经度*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}