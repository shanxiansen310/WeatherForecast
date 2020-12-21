package com.example.weatherforecast;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.database.Weather;
import com.example.weatherforecast.util.CalendarUtil;
import com.example.weatherforecast.util.FetchData;
import com.example.weatherforecast.util.FormatUtil;
import com.example.weatherforecast.util.ToActivityListener;
import com.example.weatherforecast.util.ToFragmentListener;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/* 展示天气列表 */
public class WeatherTitleFragment extends Fragment implements ToFragmentListener {

    private final String TAG = "WeatherTitleFragment";
    private boolean isTwoPane;
    private List<Weather> mItems = new ArrayList<>();
    private RecyclerView mWeatherTitleRecyclerView;
    private LinearLayout mLinearLayoutView;
    private RelativeLayout mRelativeLayoutView;   /*单页模式下显示today*/

    /*today模块的View*/
    private TextView mTextViewTodayDate;
    private TextView mTextViewTodayTempMax;
    private TextView mTextViewTodayTempMin;
    private ImageView mTextViewTodayImage;
    private TextView mTextViewTodaySituation;

    /*平板下获取上一个点击的item,便于修改颜色*/
    private View lastView;
    private Boolean isInitial=true;

    /*需要获取天气的城市名*/
    private String mCityName ="changsha";     //初始设置为changsha

    /*回调接口,向activity传递数据*/
    private ToActivityListener mToActivityListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);           //决定activity销毁后是否保留fragment


        /*如果是通过intent启动的,我们就要对显示天气的城市进行修改!!!*/
        if (Objects.requireNonNull(getActivity()).getIntent()!=null&&getActivity().getIntent().getStringExtra("cityName")!=null){
            mCityName=getActivity().getIntent().getStringExtra("cityName");
        }

        new FetchItemsTask().execute();    //执行该方法会读取后台从api获得的天气信息
        Log.d(TAG, "onCreate() is called!!");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*Instantiates a layout XML file into its corresponding View objects. */
        View view = inflater.inflate(R.layout.weather_title_frag, container, false);

        mLinearLayoutView = view.findViewById(R.id.weather_title_frag_linear_layout);
        mWeatherTitleRecyclerView = (RecyclerView) view.findViewById(R.id.weather_title_recycler_view);
        mRelativeLayoutView = view.findViewById(R.id.weather_title_frag_relative_layout);

        /*today模块初始化赋值*/
        mTextViewTodayDate = view.findViewById(R.id.weather_title_frag_today);
        mTextViewTodayTempMax = view.findViewById(R.id.weather_title_frag_tempMax);
        mTextViewTodayTempMin = view.findViewById(R.id.weather_title_frag_tempMin);
        mTextViewTodayImage = view.findViewById(R.id.weather_title_frag_image);
        mTextViewTodaySituation = view.findViewById(R.id.weather_title_frag_situation);

        /*配置布局管理器*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mWeatherTitleRecyclerView.setLayoutManager(layoutManager);

        /*从数据库初始化数据*/
        mItems= DataSupport.findAll(Weather.class);
        updateToday(mItems.get(0));

        /*传递今日的天气信息给activity,方便其发送通知*/
        mToActivityListener.getTodayWeather(mItems.get(0));

        /*配置适配器adapter*/
        setupAdapter();

        Log.d(TAG, "onCreateView() is called!!");

//        if (isTwoPane) {
//            view.findViewById(R.id.weather_title_frag_relative_layout).setVisibility(View.INVISIBLE);
//        }
        return view;
    }

    /*适配器用于在RecyclerVIew中绑定数据和更新视图, 单独写一个方法方便后台线程
     * 获取到新的数据后调用*/
    private void setupAdapter() {
        if (isAdded()) {
            mWeatherTitleRecyclerView.setAdapter(new WeatherAdapter(mItems));
        }
    }


    /*用于测试的*/
    private List<Weather> getWeather() {
        List<Weather> weatherList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Weather weather = new Weather();
            weather.setDate("2020-12-1" + i);
            weather.setTempMax(String.valueOf(i));
            weatherList.add(weather);
        }
        return weatherList;
    }

    /*该生命周期函数会在托管activity的onCreate执行后被调用*/
    /* 判断是否能在调用该fragment的activity中找到weather_content_layout
     * 如果能找到则是双页pad模式 ，否则是手机（因为这个fragment会进行复用！）*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.weather_content_layout) != null) {
            isTwoPane = true; // 可以找到weather_content_layout布局时，为双页模式

            /*双页平板模式下需要移除该view*/
            mLinearLayoutView.removeView(mRelativeLayoutView);
        } else {
            isTwoPane = false; // 找不到weather_content_layout布局时，为单页模式
        }
        Log.d(TAG, "onActivityCreated is called!!!");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() is called!!");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mToActivityListener=(MainActivity)getActivity();     /*得到托管的activity*/
        Log.d(TAG, "onAttach() is called!!");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() is called!!");
    }

    /*Adapters provide a binding from an app-specific data set to views
                    that are displayed within a RecyclerView.*/
    class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
        private List<Weather> mWeatherList;

        /*A ViewHolder describes an item view and metadata about its place within the RecyclerView.*/
        class ViewHolder extends RecyclerView.ViewHolder {
            //            TextView weatherTitleText;
            private ImageView mWeatherImageView;
            private TextView mDateTextView;
            private TextView mSituationTextView;
            private TextView mTempMaxTextView;
            private TextView mTempMinTextView;

            public ViewHolder(View view) {
                super(view);
//                weatherTitleText=(TextView)view.findViewById(R.id.weather_title);
                mWeatherImageView = (ImageView) view.findViewById(R.id.list_item_weather_image);
                mDateTextView = (TextView) view.findViewById(R.id.list_item_weather_date);
                mSituationTextView = (TextView) view.findViewById(R.id.list_item_weather_situation);
                mTempMaxTextView = (TextView) view.findViewById(R.id.list_item_weather_tempMax);
                mTempMinTextView = (TextView) view.findViewById(R.id.list_item_weather_tempMin);
            }
        }

        /*构造函数，用于传入需要的数据List*/
        public WeatherAdapter(List<Weather> weatherList) {
            mWeatherList = weatherList;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            /*创建ViewHolder以及ViewHolder要显示的视图*/
            /*获取到点击项的实例*/
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_weather, parent, false);
            final ViewHolder holder = new ViewHolder(view);

            /*对每一个显示的view设置点击监听事件*/
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Weather weather = mWeatherList.get(holder.getAdapterPosition());
                    Log.d(TAG, "onClick weather" + weather.getDate());

                    if (isTwoPane) {
                        /*双页平板模式，则刷新WeatherContentFragment中的内容*/

                        view.setBackgroundColor(getResources().getColor(R.color.blue1));
                        WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
                                getFragmentManager().findFragmentById(R.id.weather_content_fragment);

                        weatherContentFragment.refresh(weather);

                        /*防止重复点击时始终为白色*/
                        if (lastView!=view) {
                            lastView.setBackgroundColor(getResources().getColor(R.color.white));
                            lastView = view;
                        }
                    } else {
                        /*单页手机模式则直接启动WeatherContentActivity*/
                        /*注意一下这里不能直接设置WeatherContentFragment,因为Fragment
                         * 依赖于activity存在,而此时contentActivity还未启动*/
                        WeatherContentActivity.actionStart(getActivity(), weather);
                    }
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Weather weather = mWeatherList.get(position);
//
            weather.setPosition(position);
            Log.d(TAG, "Holder position:" + position);
            holder.mWeatherImageView.setImageResource(CalendarUtil.getWeatherImage(weather.getIconDay()));
            holder.mDateTextView.setText(getDate(position, weather.getDate()));
            holder.mSituationTextView.setText(weather.getTextDay());
            holder.mTempMaxTextView.setText(weather.getTempMax() + "°");
            holder.mTempMinTextView.setText(weather.getTempMin() + "°");

            /*强制让每一次setAdapter时都显示today,这样好处是改变摄氏度和华氏度时不会出现在
            * 平板模式下RecyclerView更新了格式而右边的content fragment中没有更新格式*/
            if (position == 0&&isInitial&&isTwoPane) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.blue1));
                lastView=holder.itemView;
                WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
                        getFragmentManager().findFragmentById(R.id.weather_content_fragment);

                weatherContentFragment.refresh(weather);

//                isInitial=false;
            }
//            Log.d(TAG,weather.getTextDay());
//            }
            }

            @Override
            public int getItemCount () {
                return mWeatherList.size();
            }

        }

        /*通过出现位置给出Today,Tomorrow和星期等信息*/
        public String getDate(int position, String date) {
            String myDate;
            String myMonth = date.split("-")[1];
            String myDay = date.split("-")[2];
            if (position == 0) {
                myDate = "Today";
                return myDate;
            } else if (position == 1) {
                myDate = "Tomorrow";
                return myDate;
            } else if (position < 7)
                return getWeek(date);
            else
                return getWeek(date).substring(0, 3) + " " + getMonth(myMonth) + " " + myDay;

        }

        /*天气图片在我这里是保存在本地的, 通过传过来的iconDay来确定调用哪张天气图片*/
        private int getWeatherImage(String iconDay) {
            int icon = Integer.parseInt(iconDay);

            if (icon < 200) {
                if (icon == 100)
                    return R.drawable.sunny;
                else
                    return R.drawable.cloudy;
            } else if (icon < 400)
                return R.drawable.rainy;
            else if (icon < 500)
                return R.drawable.snowy;
            else
                return R.drawable.foggy;

        }

        /*AsyncTask虽然deprecated,但对于我这种初学者还是挺友好...*/
        private class FetchItemsTask extends AsyncTask<Void, Void, List<Weather>> {

            /*doInBackground获取数据传递给onPostExecute*/
            /*doInBackground在onCreate中被调用*/
            @Override
            protected List<Weather> doInBackground(Void... voids) {
                Log.d(TAG, "doInBackGround() is called!!!");
                return new FetchData().fetchItems(mCityName);
            }


            /*在doInBackground之后执行,并且还是在主线程Resume后执行,所以安全可以更新UI*/
            @Override
            protected void onPostExecute(List<Weather> weathers) {
                mItems = weathers;
                /*获取到数据后更新UI*/
                setupAdapter();
                /*更新Today*/
                updateToday(weathers.get(0));
                mToActivityListener.getTodayWeather(weathers.get(0));


                /*平板模式下,启动默认(today)视图的更新*/
                if (isTwoPane) {
                    WeatherContentFragment weatherContentFragment = (WeatherContentFragment)
                            getFragmentManager().findFragmentById(R.id.weather_content_fragment);
                    //这里先假装date为title，最后再改
                    weatherContentFragment.refresh(weathers.get(0));

                }


                Log.d(TAG, "onPostExecute is called!!!");
            }
        }

        public String getWeek(String pTime) {
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

        public String getMonth(String month) {
            if (month.equals("01")) {
                return "Jan";
            } else if (month.equals("02"))
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

        public void updateToday(Weather weather) {
            String month = getMonth(weather.getDate().split("-")[1]);
            String day = weather.getDate().split("-")[2];
            String today = "Today " + month + " " + day;
            mTextViewTodayDate.setText(today);
            mTextViewTodayTempMax.setText(weather.getTempMax() + "°");
            mTextViewTodayTempMin.setText(weather.getTempMin() + "°");
            mTextViewTodayImage.setImageResource(getWeatherImage(weather.getIconDay()));
            mTextViewTodaySituation.setText(weather.getTextDay());
        }

        /*修改温度格式*/
        /*华氏度 = 32°F+ 摄氏度 × 1.8
          摄氏度 = (华氏度 - 32°F) ÷ 1.8*/
        @Override
        public void modifyTempFormat(boolean isCentigrade) {
            Log.d(TAG,"modifyFormat!!!");
            FormatUtil util=new FormatUtil();
            if (isCentigrade) {
                mTextViewTodayTempMax.setText(util.getCentigrade((String) mTextViewTodayTempMax.getText())+"°");
                mTextViewTodayTempMin.setText(util.getCentigrade((String) mTextViewTodayTempMin.getText())+"°");
                for (Weather weather : mItems) {
                    weather.setTempMax(util.getCentigrade(weather.getTempMax()));
                    weather.setTempMin(util.getCentigrade(weather.getTempMin()));
                }
            }else {
                mTextViewTodayTempMax.setText(util.getFahrenheit((String) mTextViewTodayTempMax.getText())+"°");
                mTextViewTodayTempMin.setText(util.getFahrenheit((String) mTextViewTodayTempMin.getText())+"°");
                for (Weather weather : mItems) {
//                    Log.d(TAG,weather.getTempMax());
//                    Log.d(TAG,util.getFahrenheit(weather.getTempMax()));
//                    Log.d(TAG,weather.getTempMin());
                    weather.setTempMax(util.getFahrenheit(weather.getTempMax()));
                    weather.setTempMin(util.getFahrenheit(weather.getTempMin()));
                }
            }
            setupAdapter();
        }

        public static void main(String[] args) {
    ////        System.out.println(getWeek("2020-12-25"));
    //        String str="2012-12-13";
    //        System.out.println(str.substring(3));
        }

}
