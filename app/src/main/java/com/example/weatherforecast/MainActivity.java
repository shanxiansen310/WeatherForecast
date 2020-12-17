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
import android.widget.Button;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.util.FetchData;
import com.qweather.plugin.view.QWeatherConfig;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchItemsTask().execute();

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

    /*AsyncTask虽然deprecated,但对于我这种初学者还是挺友好...*/
    private class FetchItemsTask extends AsyncTask<Void,Void, List<Weather>>{
        @Override
        protected List<Weather> doInBackground(Void... voids) {
            return new FetchData().fetchItems();
        }
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