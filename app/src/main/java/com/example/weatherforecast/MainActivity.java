package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
//        /*隐藏标题栏*/
//        ActionBar actionBar=getSupportActionBar();
//        if (actionBar!=null){
//            actionBar.hide();
//        }

//        ImageView imageView=(ImageView)findViewById(R.id.image);

//        Button button =(Button) findViewById(R.id.button);
//        button.setOnClickListener(this);

    }


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
            case android.R.id.home:
                this.finish(); // back button
                return true;
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
                    Uri mUri = Uri.parse("baidumap://map/geocoder?src=andr.baidu.openAPIdemo&address=长沙市");

                    /*跳转到百度地图app*/
                    Intent mIntent = new Intent(Intent.ACTION_VIEW,mUri);
                    startActivity(mIntent);
                }else {
                    Toast.makeText(this,"请安装地图软件,否则无法使用该软件",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.setting://启动设置页面
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
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




    private void replaceFragment(Fragment fragment){

//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction transaction=fragmentManager.beginTransaction();
//        transaction.replace(R.id.right_layout,fragment);
//        /*将一个事务添加到返回栈中,这样返回就会返回到替换前的fragment而不是退出应用*/
//        transaction.addToBackStack(null);
//        transaction.commit();

    }
}