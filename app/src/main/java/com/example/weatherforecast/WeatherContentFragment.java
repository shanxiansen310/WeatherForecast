package com.example.weatherforecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.util.CalendarUtil;
import com.example.weatherforecast.util.FetchData;

public class WeatherContentFragment extends Fragment {
    private View view;
    private TextView mTextViewToday;
    private TextView mTextViewDate;
    private TextView mTextViewTempMax;
    private TextView mTextViewTempMin;
    private ImageView mImageViewImage;
    private TextView mTextViewSituation;
    private TextView mTextViewHumidity;
    private TextView mTextViewPressure;
    private TextView mTextViewWind;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.weather_content_frag, container, false);

        mTextViewToday=view.findViewById(R.id.weather_content_frag_today);
        mTextViewDate=view.findViewById(R.id.weather_content_frag_date);
        mTextViewTempMax=view.findViewById(R.id.weather_content_frag_tempMax);
        mTextViewTempMin=view.findViewById(R.id.weather_content_frag_tempMin);
        mImageViewImage=view.findViewById(R.id.weather_content_frag_image);
        mTextViewSituation=view.findViewById(R.id.weather_content_frag_situation);
        mTextViewHumidity=view.findViewById(R.id.weather_content_frag_humidity);
        mTextViewPressure=view.findViewById(R.id.weather_content_frag_pressure);
        mTextViewWind=view.findViewById(R.id.weather_content_frag_wind);

        return view;
    }

    /*刷新标题和内容*/
    public void refresh(Weather weather) {
        String date=CalendarUtil.getMonth(weather.getDate().split("-")[1])
                +" "+weather.getDate().split("-")[2];
        mTextViewToday.setText(CalendarUtil.getContentDate(weather.getPosition(),weather.getDate()));
        mTextViewDate.setText(date);
        mTextViewTempMax.setText(weather.getTempMax()+"°");
        mTextViewTempMin.setText(weather.getTempMin()+"°");
        mImageViewImage.setImageResource(CalendarUtil.getWeatherImage(weather.getIconDay()));
        mTextViewSituation.setText(weather.getTextDay());
        mTextViewHumidity.setText("Humidity: "+weather.getHumidity()+"%");
        mTextViewPressure.setText("Pressure: "+weather.getPressure()+"hPa");
        mTextViewWind.setText("Wind: "+weather.getWindSpeed()+"km/h "+weather.getWindDirection());

//        View visibilityLayout = view.findViewById(R.id.visibility_layout);
//        visibilityLayout.setVisibility(View.VISIBLE);
//        TextView newsTitleText = (TextView) view.findViewById (R.id.weather_content_date);
//        TextView newsContentText = (TextView) view.findViewById(R.id.news_content);
//        newsTitleText.setText(weather.getDate()); // 刷新新闻的标题
//        newsContentText.setText(newsContent); // 刷新新闻的内容

    }
}
