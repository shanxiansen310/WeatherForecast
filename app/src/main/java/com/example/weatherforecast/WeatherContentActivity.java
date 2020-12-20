package com.example.weatherforecast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.weatherforecast.database.Weather;

import java.util.List;

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

        /*获取启动该activity时传来的Weather对象*/
        Weather weather=(Weather) getIntent().getSerializableExtra("weather");
        WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
                getSupportFragmentManager().findFragmentById(R.id.weather_content_fragment);

        assert weatherContentFragment != null;
        weatherContentFragment.refresh(weather);

//        /*得到标题和内容信息,并通过FragmentManager得到news_content_fragment对内容进行更新*/
//        String newsTitle = getIntent().getStringExtra("news_title"); // 获取传入的新闻标题
//        String newsContent = getIntent().getStringExtra("news_content"); // 获取传入的新闻内容
//        WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
//                getSupportFragmentManager().findFragmentById(R.id.weather_content_fragment);
//        assert weatherContentFragment != null;
//
//        weatherContentFragment.refresh(newsTitle, newsContent); // 刷新NewsContentFragment界面
    }

    /*返回上一个activity的按钮*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
            case R.id.mapLocation:
                // Toast.makeText(this,"成功点击了，Map Location",Toast.LENGTH_SHORT).show();
//                Intent in = new Intent(MainActivity.this,MapActivity.class);
//                startActivity(in);
                StringBuffer mStringBuffer = new StringBuffer();
                List<PackageInfo> pageString = getPackageManager().getInstalledPackages(0);
                for (PackageInfo p:pageString){
                    String  pageName = p.packageName;
                    Log.i("aaa","包名有"+pageName+"\r\n");
                    mStringBuffer.append(pageName+",");
                }
                if (mStringBuffer.toString().contains("com.baidu.BaiduMap")){
//                    Uri mUri = Uri.parse("geo:28.158483,112.931409?q=中南大学");
                    Uri mUri = Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=长沙市");

                    Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                    startActivity(mIntent);
                }else {
                    Toast.makeText(this,"请安装地图软件,否则无法使用该软件",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.setting://启动设置页面
                Intent intent = new Intent(WeatherContentActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*这里是方便于MainActivity启动该应用编写的方法,使得启动该activity所需要的参数体现出来*/
    public static void actionStart(Context context, Weather weather) {
        Intent intent = new Intent(context, WeatherContentActivity.class);
        intent.putExtra("weather",weather);
//        intent.putExtra("news_title", newsTitle);
//        intent.putExtra("news_content", newsContent);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //使用菜单填充器获取menu下的菜单资源文件
        getMenuInflater().inflate(R.menu.menu_share,menu);
        //获取分享的菜单子组件
        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        //通过setShareIntent调用getDefaultIntent()获取所有具有分享功能的App
        shareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }
    //设置可以调用手机内所有可以分享图片的应用
    private Intent getDefaultIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        //这里的类型可以按需求设置
        intent.setType("image/*");
        return intent;
    }

    /**
     * 根据包名判断某个app是否安装
     * @param packageName
     * @return
     */
//    public static boolean isAppInstalled(String packageName){
//        PackageInfo packageInfo;
//        try {
//
//            packageInfo = Application.getAppContext().getPackageManager().getPackageInfo(packageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            packageInfo=null;
//            e.printStackTrace();
//        }
//
//        if(packageInfo==null){
//            return false;
//        }else {
//            return true;
//        }
//    }
}