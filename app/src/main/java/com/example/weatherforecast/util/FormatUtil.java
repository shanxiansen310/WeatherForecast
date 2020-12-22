package com.example.weatherforecast.util;

public class FormatUtil {

    /*
    华氏度 = 32°F+ 摄氏度 × 1.8
    摄氏度 = (华氏度 - 32°F) ÷ 1.8
    */
    public String getCentigrade(String degree){
        if (degree.endsWith("°")){
            degree=degree.substring(0,degree.length()-1);
        }
        int temp=Integer.parseInt(degree);
        temp=(int) ((temp-32)/1.8);
        return temp +"";
    }

    public String getFahrenheit(String degree){
        if (degree.endsWith("°")){
            degree=degree.substring(0,degree.length()-1);
        }
        int temp=Integer.parseInt(degree);
        temp=(int)(32+temp*1.8);
        return temp+"";
    }

}
