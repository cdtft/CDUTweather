package com.cdut.cdutweather.model;

/**
 * Created by 城 on 2016/10/8.
 */

public class City {
    private int id;
    private String cityName_EN;
    private String cityName_CH;
    private String cityCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName_EN() {
        return cityName_EN;
    }

    public void setCityName_EN(String cityName_EN) {
        this.cityName_EN = cityName_EN;
    }

    public String getCityName_CH() {
        return cityName_CH;
    }

    public void setCityName_CH(String cityName_CH) {
        this.cityName_CH = cityName_CH;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
