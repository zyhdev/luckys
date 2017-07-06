package com.luckys.zyh.myluckys.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by v_zhaoyanhu01 on 2017/7/3.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    public class More{
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("cond")
    public More more;

}
