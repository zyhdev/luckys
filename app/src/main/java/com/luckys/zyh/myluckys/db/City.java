package com.luckys.zyh.myluckys.db;

import org.litepal.crud.DataSupport;

/**
 * Created by v_zhaoyanhu01 on 2017/6/29.
 */

public class City extends DataSupport {
    private int id;
    // 城市名
    private String cityName;
    // 城市code
    private String cityCode;
    // 当前市所属的省id
    private int provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }
}
