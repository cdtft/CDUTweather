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

    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }


    //将City储存到数据库中
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("CITY_NAME_EN", city.getCityName_EN());
            values.put("CITY_NAME_CH", city.getCityName_CH());
            System.out.println("+++"+city.getCityName_CH());
            values.put("CITY_CODE", city.getCityCode());
            db.insert("City", null, values);
        }
    }

    //遍历获取数据库中的City对象
    public List<City> loadCity(){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("CITY", null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                city.setCityName_CH(cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")));
                city.setCityName_EN(cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("CITY_CODE")));
                list.add(city);
            } while(cursor.moveToNext());
        }
        return list;
    }

    //根据名称获取某一个或多个匹配的城市
    public List<City> loadCitiesByName(String name) {

        List<City> cities = new ArrayList<>();
        Cursor cursor = db.query("City", null, "CITY_NAME_CH like ?", new String[]{name + "%"}, null, null, "CITY_CODE");
        while (cursor.moveToNext()) {
            City city = new City();
            city.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            city.setCityName_CH(cursor.getString(cursor.getColumnIndex("CITY_NAME_CH")));
            city.setCityName_EN(cursor.getString(cursor.getColumnIndex("CITY_NAME_EN")));
            city.setCityCode(cursor.getString(cursor.getColumnIndex("CITY_CODE")));
            cities.add(city);
        }
        if (cursor != null)
            cursor.close();
        return cities;
    }

    //检查是否是第一次安装（0-是 1-否）
    public int checkDataState() {
        int data_state = -1;
        Cursor cursor = db.query("data_state", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                data_state = cursor.getInt(cursor.getColumnIndex("STATE"));
            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();

        return data_state;
    }

    //更新状态为已有数据
    public void updateDataState() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", 1);
        db.update("data_state", contentValues, null, null);
    }

}
