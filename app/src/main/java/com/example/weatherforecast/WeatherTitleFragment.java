package com.example.weatherforecast;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.database.Weather;

import java.util.ArrayList;
import java.util.List;

/* 展示天气列表 */
public class WeatherTitleFragment extends Fragment {

    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.weather_title_frag,container,false);

        RecyclerView weatherTitleRecyclerView=(RecyclerView) view.findViewById(R.id.weather_title_recycler_view);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        weatherTitleRecyclerView.setLayoutManager(layoutManager);
        WeatherAdapter weatherAdapter=new WeatherAdapter(getWeather());
        weatherTitleRecyclerView.setAdapter(weatherAdapter);

        return view;
    }

    private List<Weather> getWeather(){
        List<Weather> weatherList=new ArrayList<>();
        for (int i=1;i<=10;i++){
            Weather weather=new Weather();
            weather.setDate("2020-12-1"+i);
            weather.setTempMax(String.valueOf(i));
            weatherList.add(weather);
        }
        return weatherList;
    }

    /* 判断是否能在调用该fragment的activity中找到weather_content_layout
    * 如果能找到则是双页pad模式 ，否则是手机（因为这个fragment会进行复用！）*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.weather_content_layout) != null) {
            isTwoPane = true; // 可以找到weather_content_layout布局时，为双页模式
        } else {
            isTwoPane = false; // 找不到weather_content_layout布局时，为单页模式
        }
    }

    /*Adapters provide a binding from an app-specific data set to views
        that are displayed within a RecyclerView.*/
    class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
        private List<Weather> mWeatherList;

        /*A ViewHolder describes an item view and metadata
        about its place within the RecyclerView.*/
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView weatherTitleText;
            public ViewHolder(View view){
                super(view);
                weatherTitleText=(TextView)view.findViewById(R.id.weather_title);
            }
        }

        /*构造函数，用于传入需要的数据List*/
        public WeatherAdapter(List<Weather> weatherList){
            mWeatherList=weatherList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /*获取到点击项的实例*/
            View view=LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weather_item,parent,false);
            final ViewHolder holder =new ViewHolder(view);
            /*对每一个显示的view设置点击监听事件*/
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Weather weather=mWeatherList.get(holder.getAdapterPosition());
                    if (isTwoPane){
                        /*双页平板模式，则刷新WeatherContentFragment中的内容*/
                        WeatherContentFragment weatherContentFragment=(WeatherContentFragment)
                                getFragmentManager().findFragmentById(R.id.weather_content_fragment);
                        //这里先假装date为title，最后再改
                        weatherContentFragment.refresh(weather.getDate(),weather.getTempMax());
                    }else {
                        /*单页手机模式则直接启动WeatherContentActivity*/
                        WeatherContentActivity.actionStart(getActivity(),
                                weather.getDate(),weather.getTempMax());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Weather weather=mWeatherList.get(position);
            holder.weatherTitleText.setText(weather.getDate());
        }

        @Override
        public int getItemCount() {
            return mWeatherList.size();
        }


    }


}
