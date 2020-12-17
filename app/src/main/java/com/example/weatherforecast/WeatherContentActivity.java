package com.example.weatherforecast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class WeatherContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_content);

        /*标题栏返回按钮*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*得到标题和内容信息,并通过FragmentManager得到news_content_fragment对内容进行更新*/
        String newsTitle = getIntent().getStringExtra("news_title"); // 获取传入的新闻标题
        String newsContent = getIntent().getStringExtra("news_content"); // 获取传入的新闻内容
        WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
                getSupportFragmentManager().findFragmentById(R.id.weather_content_fragment);
        weatherContentFragment.refresh(newsTitle, newsContent); // 刷新NewsContentFragment界面
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*这里是方便于MainActivity启动该应用编写的方法,使得启动该activity所需要的参数体现出来*/
    public static void actionStart(Context context, String newsTitle, String newsContent) {
        Intent intent = new Intent(context, WeatherContentActivity.class);
        intent.putExtra("news_title", newsTitle);
        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }
}