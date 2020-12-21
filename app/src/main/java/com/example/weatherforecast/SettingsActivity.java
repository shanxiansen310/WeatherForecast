package com.example.weatherforecast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.util.LocationDialog;
import com.suke.widget.SwitchButton;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG="SettingsActivity";
    private View mLocationView;
    private View mTempView;
    private View mNotificationView;
    private com.suke.widget.SwitchButton mSwitchButton;

    private TextView mLocationNameTextView;
    private TextView mTempUnitTextView;

    private String mCityName ="changsha";         /*默认为长沙*/
    private String  mTempUnit="Centigrade";       /*存储设置中的配置*/
    private boolean mNeedNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLocationView =findViewById(R.id.setting_location);
        mTempView =findViewById(R.id.setting_temp);
        mNotificationView =findViewById(R.id.setting_notifications);
        mSwitchButton=(com.suke.widget.SwitchButton)findViewById(R.id.switchButton);

        mLocationNameTextView =findViewById(R.id.setting_location_name);
        mTempUnitTextView =findViewById(R.id.setting_temp_ways);

        /*通知开关*/
        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Toast t;
                if (isChecked){
                    t = Toast.makeText(SettingsActivity.this, "Notification is on!", Toast.LENGTH_SHORT);
                }
                else{
                    t = Toast.makeText(SettingsActivity.this, "Notification is off", Toast.LENGTH_SHORT);
                }
                t.setGravity(Gravity.CENTER,0,0);
                t.show();
                mNeedNotification=isChecked;
                respond();


            }
        });

        /*获取启动时Intent传过来的信息,并设置状态*/
        mCityName=getIntent().getStringExtra("cityName");
        mTempUnit=getIntent().getStringExtra("tempUnit");
        mNeedNotification=getIntent().getBooleanExtra("needNotification",false);

        mLocationNameTextView.setText(mCityName);
        mTempUnitTextView.setText(mTempUnit);
        mSwitchButton.setChecked(mNeedNotification);

        /*标题栏返回按钮*/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, LocationDialog.class);
                startActivity(intent);
                respond();
            }
        });
        mTempView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tempWay=findViewById(R.id.setting_temp_ways);
                if (tempWay.getText().equals("Centigrade")) {
                    mTempUnit="Fahrenheit";
                }else {
                    mTempUnit="Centigrade";
                }
                tempWay.setText(mTempUnit);
                respond();
            }
        });

    }

    private void respond(){
        Intent intent=new Intent();
        intent.putExtra("cityName",mCityName);
        intent.putExtra("tempUnit",mTempUnit);
        intent.putExtra("needNotification",mNeedNotification);
        setResult(RESULT_OK,intent);
    }


    /*返回上一个activity的按钮,这里是控制菜单栏的点击事件!!!*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}