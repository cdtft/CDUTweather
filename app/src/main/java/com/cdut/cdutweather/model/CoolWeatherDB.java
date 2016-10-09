package com.cdut.cdutweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cdut.cdutweather.db.WeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 城 on 2016/10/9.
        */

public class CoolWeatherDB {
     /*
     *数据库名
     */
    public static final String DB_NAME = "cdut_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    //单例模式
    private CoolWeatherDB(Context context){
        WeatherOpenHelper weatherOpenHelper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = weatherOpenHelper.getWritableDatabase();
    }

    private synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //将province储存到数据库
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    //遍历获取数据库中的Province对象
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                list.add(province);
            } while(cursor.moveToNext());
        }
        return list;
    }

    //将City储存到数据库中
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //遍历获取数据库中的City对象
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{ String.valueOf(provinceId)}, null, null, null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            } while(cursor.moveToNext());
        }
        return list;
    }

    //将county储存到数据库
    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getConntyCode());
            values.put("city_id", county.getCityId());
            db.insert("City", null, values);
        }
    }

    //遍历获取数据库中的County对象
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null,null);
        if(cursor.moveToFirst()){
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setConntyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                list.add(county);
            } while(cursor.moveToNext());
        }
        return list;
    }
}
