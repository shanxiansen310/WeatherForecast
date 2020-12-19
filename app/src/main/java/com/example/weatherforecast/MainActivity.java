package com.example.weatherforecast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.util.FetchData;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.activity_main);

        /*只在单页模式下消除标题栏阴影*/
        if (this.findViewById(R.id.weather_content_layout) == null) {
            getSupportActionBar().setElevation(0.0f); // 找不到weather_content_layout布局时，为单页模式
        }
        /*隐藏标题栏*/
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar!=null){
//            actionBar.hide();
//        }

//        ImageView imageView=(ImageView)findViewById(R.id.image);

//        Button button =(Button) findViewById(R.id.button);
//        button.setOnClickListener(this);

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




    private void replaceFragment(Fragment fragment){

//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.replace(R.id.right_layout,fragment);
//        /*将一个事务添加到返回栈中,这样返回就会返回到替换前的fragment而不是退出应用*/
//        transaction.addToBackStack(null);
//        transaction.commit();

    }
}