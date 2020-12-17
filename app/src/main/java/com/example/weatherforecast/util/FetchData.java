package com.example.weatherforecast.util;

import android.net.Uri;
import android.util.Log;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.gson.WeatherBean;
import com.google.gson.Gson;

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

    /*构建完整的Flickr api请求url*/  //加入的count是为了分页
    public List<Weather> fetchItems()  {
//        Log.d(TAG,"fetchItems!!!");

        List<Weather> items =new ArrayList<>();
        try {
            /*flickr默认返回XML格式的数据,要获取有效的json数据,需要同时指定format和nojsoncallback */
            String url = Uri.parse("https://devapi.qweather.com/v7/weather/15d?")
                    .buildUpon()
                    .appendQueryParameter("api_key", API_KEY)    //--已在和风官网获取到key    暂未获取到API_KEY
                    .appendQueryParameter("location","101250101")  //城市对应的weatherId
                    .build().toString();

            String jsonString = getUrlString(url);
            //请求一次只给你25张,我调了page参数仍然不行
            String newJsonString=getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            /*解析传入的FlickrJSON数据,会生成与原始JSON数据对应的对象树*/
//            JSONObject jsonBody=new JSONObject(jsonString);
            /*解析json数据,将其转化成对象*/
//            parseItems(items,jsonBody);
            /*采用GSON解析json数据*/
//            parseItemsGson(items,jsonString);

            parseGSON(items,jsonString);

        }catch (IOException ioe){
            Log.e(TAG,"Failed to fetch Items",ioe);
        }
        return items;
    }

    private void parseGSON(List <Weather> items,String jsonData){
        Gson gson=new Gson();
        WeatherBean bean=gson.fromJson(jsonData,WeatherBean.class);

        List<Weather> weatherList=bean.getWeatherList();
        items.addAll(weatherList);
    }

    public static void main(String[] args) {
        new FetchData().fetchItems();
    }


}
