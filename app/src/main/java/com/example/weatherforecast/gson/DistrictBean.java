package com.example.weatherforecast.gson;

import com.example.weatherforecast.database.District;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistrictBean {

    private String code;
    @SerializedName("location")
    private List<District> mDistrictList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<District> getDistrictList() {
        return mDistrictList;
    }

    public void setDistrictList(List<District> districtList) {
        mDistrictList = districtList;
    }
}
