package yjbo.yy.yjbodownmanager;

import android.app.Application;
import android.content.Context;

import yjbo.yy.yjbodownmanager.downmanager.DownloadManager;


/**
 * ================================================
 * 作    者：廖子尧   github 地址  https://github.com/jeasonlzy/
 * 版    本：1.0
 * 创建日期：2015/9/23
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class DownApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.init(DownApplication.this);
    }

    public Context getInstan() {
        return this;
    }
}