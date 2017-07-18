package com.luckys.zyh.myluckys.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.luckys.zyh.myluckys.WeatherActivity;
import com.luckys.zyh.myluckys.gson.Weather;
import com.luckys.zyh.myluckys.util.HttpUtil;
import com.luckys.zyh.myluckys.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by v_zhaoyanhu01 on 2017/7/17.
 * 后台服务，主要是时时更新天气
 */

public class AutoUpdateService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /***
     * 将更新数据保存到SharedPreferences中*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("lucky","AutoUpdateService服务已经启动");
//        updateWeather();
//        updateBingPic();
        // 定时
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //int anHour = 8 * 60 * 60 * 1000;//8个小时的毫秒数
        int anHour = 5000;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lucky","AutoUpdateService服务已经销毁!!!");
    }

    /**
     * 更新天气信息*/
    private void updateWeather(){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);
        if(weatherString != null){
            // 有缓存时直接解析天气数据获取id
            Weather weather=Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=6fdfb053cfe945dfbe7caa97b1478df3";
            HttpUtil.sendOkHttpRequet(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText=response.body().string();
                    // Log.i("lucky","服务器返回的天气数据:"+responseText.toString());
                    final Weather weather=Utility.handleWeatherResponse(responseText);
                    if(weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
    /**
     * 更新必应每日一图*/
    private void updateBingPic(){
        String requestUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequet(requestUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingImge=response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_image",bingImge);
                editor.apply();
            }
        });
    }
}
