package com.luckys.zyh.myluckys.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v_zhaoyanhu01 on 2017/7/3.
 */

public class AQI {
    @SerializedName("city")
    public AQICity city;
    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
