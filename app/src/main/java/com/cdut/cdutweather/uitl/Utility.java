package com.cdut.cdutweather.uitl;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cdut.cdutweather.model.City;
import com.cdut.cdutweather.model.CoolWeatherDB;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
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

    public synchronized static boolean handWeatherResponse(SharedPreferences.Editor editor, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                /*"HeWeather data service 3.0": [
                 * [说明json数据是一数组的形式存放的，先把数据加载成数组*/
                JSONArray jsonArray = new JSONObject(response).getJSONArray("HeWeather data service 3.0");
                //数组中只有一个元素，所以我们只去第一个元素为JSONObject
                JSONObject weather_info_all = jsonArray.getJSONObject(0);
                /*"basic": {
                        "city": "大连",
                        "cnty": "中国",
                        "id": "CN101070201",
                        "lat": "38.944000",
                        "lon": "121.576000",
                        "update": {
                            "loc": "2015-07-15 10:43",
                            "utc": "2015-07-15 02:46:14"
                        }
                    }
                    在basic可以取得city、id和loc数据*/
                JSONObject weather_info_basic = weather_info_all.getJSONObject("basic");
                editor.putString("city_name_ch", weather_info_basic.getString("city"));
                editor.putString("city_code", weather_info_basic.getString("id"));
                JSONObject weather_info_basic_updata = weather_info_basic.getJSONObject("update");
                editor.putString("update_time", weather_info_basic_updata.getString("loc"));

                /*"daily_forecast": [
                        {
                            "date": "2015-07-15",
                            "astro": {
                                "sr": "04:40",
                                "ss": "19:19"
                            },
                            "cond": {
                                "code_d": "100",
                                "code_n": "101",
                                "txt_d": "晴",
                                "txt_n": "多云"
                            },
                            "hum": "48",
                            "pcpn": "0.0",
                            "pop": "0",
                            "pres": "1005",
                            "tmp": {
                                "max": "33",
                                "min": "24"
                            },
                            "vis": "10",
                            "wind": {
                                "deg": "192",
                                "dir": "东南风",
                                "sc": "4-5",
                                "spd": "11"
                            }
                        },*/
                JSONArray weather_info_daily_forecast = weather_info_all.getJSONArray("daily_forecast");

                JSONObject weather_info_now_forecast = weather_info_daily_forecast.getJSONObject(0);
                editor.putString("date_now", weather_info_now_forecast.getString("date"));

                JSONObject weather_info_now_forecast_tmp = weather_info_now_forecast.getJSONObject("tmp");
                editor.putString("tmp_max", weather_info_now_forecast_tmp.getString("max"));
                editor.putString("tmp_min", weather_info_now_forecast_tmp.getString("min"));

                JSONObject weather_info_now_forecast_cond = weather_info_now_forecast.getJSONObject("cond");
                editor.putString("txt_b", weather_info_now_forecast_cond.getString("txt_d"));
                editor.putString("txt_a", weather_info_now_forecast_cond.getString("txt_n"));
                editor.commit();
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
