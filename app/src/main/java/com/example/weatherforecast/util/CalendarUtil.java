package com.example.weatherforecast.util;

import com.example.weatherforecast.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarUtil {

    /*yyyy-mm-dd格式的字符串日期获取星期数*/
    public static String getWeek(String pTime) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "Sunday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "Monday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "Tuesday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "Wednesday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "Thursday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "Friday";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "Saturday";
        }

        return Week;
    }
    /*01:Jan,由数字月份得到英文简写*/
    public static String getMonth(String month){
        if (month.equals("01")){
            return "Jan";
        }else if (month.equals("02"))
            return "Feb";
        else if (month.equals("03"))
            return "Mar";
        else if (month.equals("04"))
            return "Apr";
        else if (month.equals("05"))
            return "May";
        else if (month.equals("06"))
            return "Jun";
        else if (month.equals("07"))
            return "Jul";
        else if (month.equals("08"))
            return "Aug";
        else if (month.equals("09"))
            return "Sep";
        else if (month.equals("10"))
            return "Oct";
        else if (month.equals("11"))
            return "Nov";
        else if (month.equals("12"))
            return "Dec";
        else
            return "error";
    }

    /*通过出现位置给出Today,Tomorrow和星期等信息*/
    public static String getDate(int position,String date){
        String myDate;
        String myMonth=date.split("-")[1];
        String myDay=date.split("-")[2];
        if (position==0){
            myDate="Today";
            return myDate;
        }

        else if (position==1){
            myDate="Tomorrow";
            return myDate;
        }
        else if (position<7)
            return getWeek(date);
        else
            return getWeek(date).substring(0,3)+" "+getMonth(myMonth)+" "+myDay;

    }

    public static String getContentDate(int position,String date){
        String myDate;
        String myMonth=date.split("-")[1];
        String myDay=date.split("-")[2];
        if (position==0){
            myDate="Today";
            return myDate;
        }

        else if (position==1){
            myDate="Tomorrow";
            return myDate;
        }
        else
            return getWeek(date);

    }

    /*天气图片在我这里是保存在本地的, 通过传过来的iconDay来确定调用哪张天气图片*/
    public static int getWeatherImage(String iconDay){
        int icon=Integer.parseInt(iconDay);

        if (icon<200) {
            if (icon==100)
                return R.drawable.sunny;
            else
                return R.drawable.cloudy;
        }
        else if (icon<400)
            return R.drawable.rainy;
        else if (icon<500)
            return R.drawable.snowy;
        else
            return R.drawable.foggy;

    }

    public static void main(String[] args) {
//        System.out.println(CalendarUtil.getMonth("01"));
        Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(formatter.format(calendar.getTime()));
    }
}
