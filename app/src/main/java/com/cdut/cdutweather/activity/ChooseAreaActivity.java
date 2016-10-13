package com.cdut.cdutweather.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cdut.cdutweather.R;
import com.cdut.cdutweather.model.City;
import com.cdut.cdutweather.model.CoolWeatherDB;
import com.cdut.cdutweather.uitl.HttpCallBackListener;
import com.cdut.cdutweather.uitl.HttpUtil;
import com.cdut.cdutweather.uitl.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 城 on 2016/10/9.
 */

public class ChooseAreaActivity extends AppCompatActivity {

    public static final String KEY = "bd7dacd7684947ddb1817d4e60a59305";

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private EditText editText;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();
    /*市级列表*/
    private List<City> cityList;
    /*选中的市*/
    private City selectedCity;
    private static final int NONE_DATA = 0;//标识是否有初始化城市数据

    private SharedPreferences mSharedPreferences;//本地存储
    private SharedPreferences.Editor mEditor;//本地存储


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choose_area);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        if(coolWeatherDB.checkDataState() == NONE_DATA){
            queryCitiesFromServer();
        }

        cityList = queryCitiesFromLocal("");

        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        editText = (EditText) findViewById(R.id.edit_text);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityList = queryCitiesFromLocal(s.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = cityList.get(position);
                titleText.setText(selectedCity.getCityName_CH());
                queryWeatherFromServer();//根据点击的城市获取天气数据
            }
        });
    }

    //从服务器中获取天气数据
    private void queryWeatherFromServer() {
        String address = "https://api.heweather.com/x3/weather?cityid="+selectedCity.getCityCode()+ "&key=" + KEY;
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if(Utility.handWeatherResponse(mEditor, response)){
                    closeProgressDialog();
                    //进度条消失说明请求的天气数据已经出入在本地
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /*重数据库中查询所有的城市信息*/
    private List<City> queryCitiesFromLocal(String name) {
        List<City> cities = coolWeatherDB.loadCitiesByName(name);
        dataList.clear();
        for (City c : cities) {
            dataList.add(c.getCityName_CH());
            System.out.println(">>>>>" + c.getCityName_CH());
        }
        return cities;
    }

    /*从服务器上获取数据*/
    private void queryCitiesFromServer() {
        String address = " https://api.heweather.com/x3/citylist?search=allchina&key=" + KEY;
        //当向服务器发送请求时（耗时操作）弹出
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                if (Utility.handleCityResponse(coolWeatherDB, response)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            coolWeatherDB.updateDataState();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    /*关闭进度*/
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /*显示进度*/
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

}
