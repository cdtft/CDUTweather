package com.cdut.cdutweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cdut.cdutweather.R;
import com.cdut.cdutweather.model.City;
/**
 * Created by 城 on 2016/10/12.
 */

public class Weather extends AppCompatActivity {

    private Button btn_home;
    private TextView tv_cityName;
    private Button btn_refresh;
    private TextView tv_publishTime;
    private TextView tv_currentTime;
    public TextView tv_weatherDesp;
    private TextView tv_tmpMax;
    private TextView tv_tmpMin;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final int REQUEST_CODE = 1;
    private City mcity_current = new City();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.weather_layout);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        tv_cityName = (TextView) findViewById(R.id.tv_city_name);
        tv_publishTime = (TextView) findViewById(R.id.tv_publishTime);
        tv_currentTime = (TextView) findViewById(R.id.tv_currentTime);
        tv_tmpMax = (TextView) findViewById(R.id.tv_temp2);
        tv_tmpMin = (TextView) findViewById(R.id.tv_temp1);
        tv_weatherDesp = (TextView) findViewById(R.id.tv_weather_desp);

        //home点击事件
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Weather.this, ChooseAreaActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //refresh点击事件
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //在ChooseAreaActivity调用了finish方法后将会调用onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                downLoadWeatherData(sharedPreferences.getString("city_code", null), sharedPreferences.getString("city_name_ch", null),
                        sharedPreferences.getString("update_time", null), sharedPreferences.getString("date_now", null),
                        sharedPreferences.getString("tmp_max", null), sharedPreferences.getString("tmp_min", null),
                        sharedPreferences.getString("txt_a", null), sharedPreferences.getString("txt_b", null));
            }
        }
    }

    //将数据显示到组件上
    private void downLoadWeatherData(String city_code, String city_name, String publish_time,
                                     String current_time, String tmp_max, String tmp_min, String txt_a, String txt_b) {
        tv_cityName.setText(city_name);
        tv_publishTime.setText(publish_time);
        tv_currentTime.setText(current_time);
        tv_tmpMin.setText(tmp_min+"℃");
        System.out.println("-----"+tmp_min);
        tv_tmpMax.setText(tmp_max+"℃");
        if (txt_a.equals(txt_b)) {
            tv_weatherDesp.setText(txt_a);
        } else {
            tv_weatherDesp.setText(txt_b+"转"+txt_a);
        }

        mcity_current.setCityCode(city_code);
        mcity_current.setCityName_CH(city_name);
    }
}
