package com.luckys.zyh.myluckys.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by v_zhaoyanhu01 on 2017/6/30.
 */

public class HttpUtil {

    /**
     * 请求服务器数据--OkHttp出色封装*/
    public static void sendOkHttpRequet(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request requet = new Request.Builder().url(address).build();
        client.newCall(requet).enqueue(callback);
    }
}
