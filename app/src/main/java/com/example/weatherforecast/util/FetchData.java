package com.example.weatherforecast.util;

import android.net.Uri;
import android.util.Log;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.gson.DistrictBean;
import com.example.weatherforecast.gson.WeatherBean;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class FetchData {

    private static final String TAG="FetchData";
    private static final String API_KEY="13d6358408ee4688ba0c5b8a82e677e7";
    /*从指定URL获取原始数据并返回一个字节流数组*/
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setInstanceFollowRedirects(false);
        String redirect=connection.getHeaderField("Location");
        if (redirect!=null){
            connection=(HttpsURLConnection)new  URL(redirect).openConnection();
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /*将getUrlBytes返回的结果转化为String*/
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /*构建完整的天气 api请求url*/  //加入的count是为了分页
    public List<Weather> fetchItems(String cityName)  {
//        Log.d(TAG,"fetchItems!!!");

//        Log.d(TAG,"!!!"+getCityId("changsha"));
        String cityId=getCityId(cityName);
        List<Weather> items =new ArrayList<>();

        try {
            String url = Uri.parse("https://devapi.qweather.com/v7/weather/15d?")
                    .buildUpon()
                    .appendQueryParameter("key", API_KEY)    //--已在和风官网获取到key    暂未获取到API_KEY
                    .appendQueryParameter("location",cityId)  //城市对应的weatherId
                    .build().toString();

            String jsonString = getUrlString(url);

            Log.i(TAG, "Received JSON: " + jsonString);

            parseGSON(items,jsonString);

            Log.d(TAG,"Weather:"+items.get(1).getDate()+"\n"+
                    items.get(1).getHumidity()+"\n"+items.get(1).getIconDay()+"\n"+
                    items.get(1).getPressure()+"\n"+items.get(1).getTempMax()+"\n"+
                    items.get(1).getTempMin()+"\n"+items.get(1).getWindSpeed());

        }catch (IOException ioe){
            Log.e(TAG,"Failed to fetch Items",ioe);

            /*获取不到的话,就从数据库中获取*/
            items=DataSupport.findAll(Weather.class);
            return items;

        }


        /*删除整张表*/
//        DataSupport.deleteAll(Weather.class);

        List<Weather> queryWeather;
        /*将获取到的数据添加到数据库中*/
        for(Weather weather:items){
            queryWeather=DataSupport.where("date=?",weather.getDate()).find(Weather.class);

            if (queryWeather.size()==0){
                weather.save();
            }

        }



        return items;
    }

    /*获取城市ID*/
    public String getCityId(String city){
        String cityId=null;
        try {
            String url = Uri.parse("https://geoapi.qweather.com/v2/city/lookup?")
                    .buildUpon()
                    .appendQueryParameter("key", API_KEY)
                    .appendQueryParameter("location", city)
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

            Gson gson=new Gson();
            DistrictBean bean=gson.fromJson(jsonString,DistrictBean.class);
            cityId=bean.getDistrictList().get(0).getId();

        }catch (IOException ioe){
            Log.e(TAG,"Failed to fetch Items",ioe);
        }

        return cityId;
    }

    private void parseGSON(List <Weather> items,String jsonData){
        Gson gson=new Gson();
        WeatherBean bean=gson.fromJson(jsonData,WeatherBean.class);

        List<Weather> weatherList=bean.getWeatherList();
        items.addAll(weatherList);
    }



    public static void main(String[] args) {
    }


}
