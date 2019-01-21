package com.smailnet.demo;

import android.app.Application;

import com.smailnet.microkv.MicroKV;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MicroKV组件带参数key的初始化
        MicroKV.initialize(this, "2018Year12Mon21Day");
    }
}
