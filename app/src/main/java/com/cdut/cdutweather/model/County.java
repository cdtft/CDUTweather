package com.cdut.cdutweather.model;

/**
 * Created by 城 on 2016/10/8.
 */

public class County {
    private int id;
    private String countyName;
    private String conntyCode;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConntyCode() {
        return conntyCode;
    }

    public void setConntyCode(String conntyCode) {
        this.conntyCode = conntyCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
