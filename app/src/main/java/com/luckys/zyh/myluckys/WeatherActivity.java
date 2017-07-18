package com.luckys.zyh.myluckys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.LoginFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luckys.zyh.myluckys.gson.Forecast;
import com.luckys.zyh.myluckys.gson.Weather;
import com.luckys.zyh.myluckys.service.AutoUpdateService;
import com.luckys.zyh.myluckys.util.HttpUtil;
import com.luckys.zyh.myluckys.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by v_zhaoyanhu01 on 2017/7/4.
 */

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView nowDegreeText;
    private TextView nowWeatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView aqiPm25;

    private TextView sugComfortText;
    private TextView sugCarWashText;
    private TextView sugSportText;

    private ImageView weatherImageView;
    public SwipeRefreshLayout swipeRefresh;
    private String weatherId;

    public DrawerLayout drawerLayout;
    private Button navButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //背景图和状态栏（显示移动信号，电池，时间,最上层的状态栏）融合
        //5.0才支持的功能
        // 效果时，头布局和系统状态栏紧贴在一起了，借助android:fitsSystemWindows 为系统状态栏留出空间
//        if(Build.VERSION.SDK_INT >= 21){
//            View decorView=getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_weather);
        // 初始化控件
        weatherLayout = (ScrollView)findViewById(R.id.lucky_weather_scrollview);
        titleCity = (TextView)findViewById(R.id.lucky_title_textview1_city);
        titleUpdateTime = (TextView)findViewById(R.id.lucky_title_textview2_update_time);

        nowDegreeText = (TextView)findViewById(R.id.lucky_now_textview1_degree);
        nowWeatherInfoText = (TextView)findViewById(R.id.lucky_now_textview2_info);

        forecastLayout = (LinearLayout)findViewById(R.id.lucky_forecast_layout);

        aqiText = (TextView)findViewById(R.id.lucky_aqi_textview1);
        aqiPm25 = (TextView)findViewById(R.id.lucky_pm25_textview1);

        sugComfortText = (TextView)findViewById(R.id.lucky_suggestion_textview2_comfort);
        sugCarWashText = (TextView)findViewById(R.id.lucky_suggestion_textview3_car_wash);
        sugSportText = (TextView)findViewById(R.id.lucky_suggestion_textview4_sport);

        weatherImageView = (ImageView)findViewById(R.id.lucky_weather_imageview);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.lucky_weather_swipe);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        drawerLayout = (DrawerLayout)findViewById(R.id.lucky_weather_drawerlayout);
        navButton = (Button)findViewById(R.id.lucky_title_button);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String image=prefs.getString("bing_image",null);
        if(image != null){
            Glide.with(this).load(image).into(weatherImageView);
        }else{
            loadBingImage();
        }
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather=Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            // 无缓存去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("lucky","下啦刷新天气数据!!!");
                requestWeather(weatherId);
            }
        });

    }

    /**
     * 加载背景图*/
    private void loadBingImage() {
        String requestUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequet(requestUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingImge=response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_image",bingImge);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingImge).into(weatherImageView);
                    }
                });

            }
        });
    }

    /**
     * 根据天气id请求城市天气信息*/
    public void requestWeather(String weatherId) {
        // Log.i("lucky","城市天气数据id:"+weatherId);// CN101010200
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=6fdfb053cfe945dfbe7caa97b1478df3";
        // Log.i("lucky","URL:"+weatherUrl);
        HttpUtil.sendOkHttpRequet(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                // Log.i("lucky","服务器返回的天气数据:"+responseText.toString());
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        });
        loadBingImage();
    }

    /**
     * 处理并展示Weather实体类中的数据*/
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree=weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        nowDegreeText.setText(degree);
        nowWeatherInfoText.setText(weatherInfo);

        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forcast_itme,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.lucky_forecast_item_textview1_date);
            TextView infoText = (TextView)view.findViewById(R.id.lucky_forecast_item_textview2_info);
            TextView maxText = (TextView)view.findViewById(R.id.lucky_forecast_item_textview3_max);
            TextView minText = (TextView)view.findViewById(R.id.lucky_forecast_item_textview4_min);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            aqiPm25.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:"+weather.suggestion.carWash.info;
        String sport = "运动建议:"+weather.suggestion.sport.info;
        sugComfortText.setText(comfort);
        sugCarWashText.setText(carWash);
        sugSportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);
    }

    public void setWeatherId(String weatherId){
        this.weatherId = weatherId;
    }
}
