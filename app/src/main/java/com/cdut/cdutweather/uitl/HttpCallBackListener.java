package com.cdut.cdutweather.uitl;

/**
 * Created by 城 on 2016/10/9.
 */

public interface HttpCallBackListener {
    void onFinish(String response);
    void onError(Exception e);
}
