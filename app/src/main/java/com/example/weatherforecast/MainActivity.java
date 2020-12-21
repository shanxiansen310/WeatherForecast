package com.example.weatherforecast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.util.ToActivityListener;
import com.example.weatherforecast.util.ToFragmentListener;

import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ToActivityListener {

    private final String TAG="shanxiansen";
    private String mCityName ="changsha";     /*默认为长沙*/

    private boolean mIsCentigrade=true;       /*存储设置中的配置*/
    private boolean mNeedNotification=false;
    private String mTempUnit="Centigrade";

    private static final int REQUEST_CODE_SETTINGS =0;

    private WeatherTitleFragment mWeatherTitleFragment;
    private ToFragmentListener mToFragmentListener;

    private Weather mWeatherToday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_main);

        /*只在单页模式下消除标题栏阴影*/
        if (this.findViewById(R.id.weather_content_layout) == null) {
            getSupportActionBar().setElevation(0.0f); // 找不到weather_content_layout布局时，为单页模式
        }

        /*创建数据库*/
        Connector.getDatabase();
//        Weather weather=new Weather();
//        weather.save();

//        /*从设置中启动时更新城市名*/
//        if (getIntent()!=null&&getIntent().getStringExtra("cityName")!=null){
//            Log.d(TAG,"getIntent() is not null");
//            cityName=getIntent().getStringExtra("cityName");
//        }

        Log.d(TAG,this.toString()+": onCreated!");
//        /*隐藏标题栏*/
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar!=null){
//            actionBar.hide();
//        }

//        ImageView imageView=(ImageView)findViewById(R.id.image);

//        Button button =(Button) findViewById(R.id.button);
//        button.setOnClickListener(this);

    }

//
//    public static Intent newIntent(Context context){
//        return new Intent(context,MainActivity.class);
//    }

    /**
     * 创建菜单
     */
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

    /**
     * 菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*返回*/
            case android.R.id.home:
                this.finish(); // back button
                return true;
            /*地图*/
            case R.id.mapLocation:
                // Toast.makeText(this,"成功点击了，Map Location",Toast.LENGTH_SHORT).show();
//                Intent in = new Intent(MainActivity.this,MapActivity.class);
//                startActivity(in);
                StringBuilder mStringBuffer = new StringBuilder();
                List<PackageInfo> pageString = getPackageManager().getInstalledPackages(0);
                for (PackageInfo p:pageString){
                    String  pageName = p.packageName;
//                    Log.i("aaa","包名有"+pageName+"\r\n");
                    mStringBuffer.append(pageName).append(",");
                }
                if (mStringBuffer.toString().contains("com.baidu.BaiduMap")){
//                    Uri mUri = Uri.parse("geo:28.158483,112.931409?q=中南大学");
                    Uri mUri = Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address="+ mCityName);

                    /*跳转到百度地图app*/
                    Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                    startActivity(mIntent);
                }else {
                    Toast.makeText(this,"请安装地图软件,否则无法使用该软件",Toast.LENGTH_SHORT).show();
                }
                break;
            /*设置*/
            case R.id.setting://启动设置页面
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("cityName", mCityName);
                intent.putExtra("tempUnit",mTempUnit);
                intent.putExtra("needNotification",mNeedNotification);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                break;
            default:
                break;
        }
        return true;
    }

    /*获取手机内所有可以分享图片的应用*/
    private Intent getDefaultIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        //这里的类型可以按需求设置
        intent.setType("image/*");
        return intent;
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.button:
//                replaceFragment(new AnotherRightFragment());
//                break;
//            default:
//                break;
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, this.toString() + ": onStart!");
        /*从设置中启动时更新城市名*/
        if (getIntent()!=null&&getIntent().getStringExtra("cityName")!=null){
            Log.d(TAG,"getIntent() is not null");
            mCityName =getIntent().getStringExtra("cityName");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,this.toString()+": onDestroy!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,this.toString()+": onResume!");
        if (mNeedNotification)
            sendNotification();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult!!!");

        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG,"onActivityResult: not ok!!!");
            return;
        }

        if (requestCode == REQUEST_CODE_SETTINGS){
            if (data==null){
                Log.d(TAG,"onActivityResult: data is null!!!");
                return;
            }

            //TODO 摄氏度和华氏度切换
            if (! mTempUnit.equals(data.getStringExtra("tempUnit"))){
                FragmentManager fm=getSupportFragmentManager();
                mWeatherTitleFragment=(WeatherTitleFragment)fm.findFragmentById(R.id.weather_title_fragment);

                mToFragmentListener=mWeatherTitleFragment;
                boolean isCentigrade;
                /*旧的是摄氏度,则新的是华氏度*/
                isCentigrade = !mTempUnit.equals("Centigrade");
                /*更新*/

                mToFragmentListener.modifyTempFormat(isCentigrade);
            }
            mCityName=data.getStringExtra("cityName");
            mTempUnit=data.getStringExtra("tempUnit");
            mNeedNotification=data.getBooleanExtra("needNotification",false);

            boolean isEqual=(getIntent()==data);
            Log.d(TAG,"成功获取返回数据!!!"+mTempUnit+" $$$"+isEqual+"##"+getIntent().getStringExtra("cityName"));

        }
    }

    public void sendNotification(){
        Log.d(TAG,"sendNotification!!!");

        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 26)
        {
            //当sdk版本大于26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);
//                     channel.enableLights(true);
//                     channel.enableVibration(true);//
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(MainActivity.this, id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle("Forecast: "+mCityName)
                    .setContentText(mWeatherToday.getTextDay()+"  high:"+mWeatherToday.getTempMax()+"°  low:"+mWeatherToday.getTempMin()+"°")
                    .setTicker("Weather Notification")
                    .setSmallIcon(R.drawable.cloudy)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        }
        else
        {
            //当sdk版本小于26
            Notification notification = new NotificationCompat.Builder(MainActivity.this)
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            manager.notify(1,notification);
        }

//        Intent intent=new Intent(MainActivity.this,MainActivity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,intent,0);
//
//        //设置图片,通知标题,发送时间,提示方式等属性
//        Notification.Builder mBuilder = new Notification.Builder(this);
//        mBuilder.setContentTitle("叶良辰")                        //标题
//                .setContentText("我有一百种方法让你呆不下去~")      //内容
//                .setSubText("——记住我叫叶良辰")                    //内容下面的一小段文字
//                .setTicker("收到叶良辰发送过来的信息~")             //收到信息后状态栏显示的文字信息
//                .setWhen(System.currentTimeMillis())           //设置通知时间
//                .setSmallIcon(R.drawable.cloudy)            //设置小图标
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
//                .setAutoCancel(true)                           //设置点击后取消Notification
//                .setContentIntent(pendingIntent);                        //设置PendingIntent
//        Notification notify1 = mBuilder.build();
//        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
//            mChannel.setDescription("notication channel");
//            mChannel.setShowBadge(false);
//            manager.createNotificationChannel(mChannel);
//        }
//        manager.notify(0, notify1);
    }


    /*接口回调,用于Fragment向Activity传递信息*/
    /*MainActivity中进行该方法的设计,Fragment中对该接口进行传值并调用,该方法根据传值进行相应的操作*/
    @Override
    public void getTodayWeather(Weather weather) {
        mWeatherToday=weather;
    }

    private void replaceFragment(Fragment fragment){

//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.replace(R.id.right_layout,fragment);
//        /*将一个事务添加到返回栈中,这样返回就会返回到替换前的fragment而不是退出应用*/
//        transaction.addToBackStack(null);
//        transaction.commit();

    }
}