package com.yonyoucloud.glidedemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by yjbo on 17/9/4.
 */

public class MyApp extends Application {
    //引入的方法数超过65k，进行分包的
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
