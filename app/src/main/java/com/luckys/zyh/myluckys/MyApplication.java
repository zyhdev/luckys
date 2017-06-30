package com.luckys.zyh.myluckys;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by v_zhaoyanhu01 on 2017/6/29.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }
}
