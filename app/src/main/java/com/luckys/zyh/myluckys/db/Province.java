package com.luckys.zyh.myluckys.db;

import org.litepal.crud.DataSupport;

/**
 * Created by v_zhaoyanhu01 on 2017/6/29.
 * LitePal 数据开源库表对应实体类
 *
 * province 省份
 */

public class Province extends DataSupport{
    private int id;
    // 省名称
    private String provinceName;
    // 省code
    private int provinceCode;

    public int getId() {
        return id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
