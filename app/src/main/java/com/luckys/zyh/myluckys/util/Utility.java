package com.luckys.zyh.myluckys.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.luckys.zyh.myluckys.db.City;
import com.luckys.zyh.myluckys.db.County;
import com.luckys.zyh.myluckys.db.Province;
import com.luckys.zyh.myluckys.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by v_zhaoyanhu01 on 2017/6/30.
 * GSON:解析数据需要先将数据对应的实体类创建好
 *
 * 和风天气返回的数据格式：{"HeWeather":[
 *                           {
 *                              "status":"ok",
 *                              "basic":"",
 *                              "aqi":{},
 *                              "now":{},
 *                              "suggestion":{},
 *                              "daily_forecast":[]
 *                           }
 *                        ]
 *                      }
 *
 *                      "basic":{"city"："苏州",
 *                               "id":CN101190401,
 *                               "update":{
 *                                   "loc":"2017-07-03 21:58"
 *                                }
 *                       }
 *
 *                       "aqi":{
 *                           "city":{
 *                                "api":"44",
 *                                "pm25":"13"
 *                            }
 *                        }
 *
 *                        "now":{
 *                            "tmp":"29",
 *                            "cond":{
 *                               "txt":"阵雨"
 *                             }
 *                         }
 *
 *                         "suggestion":{
 *                              "comf":{
 *                                   "txt":"白天天气较热，虽然有雨，但仍然无法消弱高气温给人们带来的暑意"
 *                                }
 *                              "cw":{
 *                                   "txt":"未来24小时内有雨，如果在此期间洗车..."
 *                              }
 *                              "sport":{
 *                                   "txt":"有降水....."
 *                              }
 *                          }
 *
 *                          "daily_forecast":[
 *                            {
 *                                "date":"2017-07-03",
 *                                "cond":{
 *                                    "txt_d":"阵雨"
 *                                },
 *                                "tmp":{
 *                                    "max":"34",
 *                                    "min":"27"
 *                                }
 *                            },{
 *                                "date":"2017-07-04"
 *                                "cond":{
 *                                    "txt_d":"多云"
 *                                },
 *                                "tmp":{
 *                                    "max":"35",
 *                                    "min":"29"
 *                                }
 *                            },
 *                            ...
 *                          ]
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     * 存储到数据库中
     * 数据格式：[{"id":1,"name":"北京"},{"id":2,"name":"上海"},{"id":3,"name":"天津"},...]*/
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allProvinces = new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject=allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     * 存储到数据库中
     * 数据格式：[{"id":57,"name":"石家庄"},{"id":58,"name":"保定"},...]*/
    public static boolean handleCityResponse(String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     * 存储到数据库中
     * 数据格式：[{"id":541,"name":"邯郸","weather_id":"CN101091001"},{"id":542,"name":"峰峰","weather_id":"CN101091002"},...]*/
    public static boolean handleCountyResponse(String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject=allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 将返回的JSON数据解析程Weather实体类*/
    public static Weather handleWeatherResponse(String response){
        Log.i("lucky","JSON数据:"+response.toString());
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather5");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
