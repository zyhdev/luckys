package com.luckys.zyh.myluckys.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v_zhaoyanhu01 on 2017/7/3.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }

}
