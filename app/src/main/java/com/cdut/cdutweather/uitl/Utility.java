package com.cdut.cdutweather.uitl;

import android.text.TextUtils;

import com.cdut.cdutweather.model.City;
import com.cdut.cdutweather.model.CoolWeatherDB;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 城 on 2016/10/9.
 * 解析服务器返回的数据
 */

public class Utility {
    //解析市级数据
    public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,
                                                          String response) {
        if (!TextUtils.isEmpty(response)) {
            /*City city = new Gson().fromJson(response, City.class);
            coolWeatherDB.saveCity(city);
            return true;*/
            try {

                //城市信息JSON比较简单，这里不做详细的解析分析
                JSONArray jsonArray = new JSONObject(response).getJSONArray("city_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject city_info = jsonArray.getJSONObject(i);
                    City city = new City();
                    String city_name_ch = city_info.getString("city");
                    String city_name_en = "";
                    String city_code = city_info.getString("id");
                    city.setCityCode(city_code);
                    city.setCityName_EN(city_name_en);
                    city.setCityName_CH(city_name_ch);
                    coolWeatherDB.saveCity(city);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

}
