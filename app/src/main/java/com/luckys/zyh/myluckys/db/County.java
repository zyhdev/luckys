package com.luckys.zyh.myluckys.db;

import org.litepal.crud.DataSupport;

/**
 * Created by v_zhaoyanhu01 on 2017/6/29.
 */

public class County extends DataSupport {
    private int id;
    // 县名称
    private String countyName;
    // 县所对应的天气id
    private String weatherId;
    // 所属市的id
    private int cityId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public int getCityId() {
        return cityId;
    }
}
