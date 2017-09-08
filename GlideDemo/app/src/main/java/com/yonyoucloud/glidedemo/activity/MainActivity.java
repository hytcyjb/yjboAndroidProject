package com.yonyoucloud.glidedemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.yonyoucloud.AMap.activity.ShowMapActivity;
import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.glidedemo.util.GlideCacheUtil;
import com.yonyoucloud.glidedemo.util.GlideUtil;
import com.yonyoucloud.util.BitmapWaterMarkerUtil;
import com.yonyoucloud.util.BitmapWaterUtil;

/**
 * glide的demo，封装api
 *
 * @author yjbo
 * @data 17/8/31 上午11:11
 */
public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mImageViewSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    //初始化控件
    private void initView() {
        mImageView = (ImageView) findViewById(R.id.my_image_view);
        mImageViewSec = (ImageView) findViewById(R.id.my_image_second);

        findViewById(R.id.show_image_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ImageRecycleActivity.class));
            }
        });
        findViewById(R.id.show_map_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ShowMapActivity.class));
            }
        });
        findViewById(R.id.cache_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //缓存
                String cachePath = getExternalCacheDir() + "/yjboCache";
                GlideCacheUtil glideCacheUtil = new GlideCacheUtil();
                String cacheSize = glideCacheUtil.getCacheSize(MainActivity.this, cachePath);
                Toast.makeText(MainActivity.this, "内存大小=" + cacheSize, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.clear_cache_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cachePath = getExternalCacheDir() + "/yjboCache";
                GlideCacheUtil glideCacheUtil = new GlideCacheUtil();
                glideCacheUtil.clearImageMemoryCache(MainActivity.this);
                glideCacheUtil.clearImageDiskCache(MainActivity.this);
                boolean b = glideCacheUtil.clearImageAllCache(MainActivity.this, cachePath);
                if (b) {
                    Toast.makeText(MainActivity.this, "清除完成", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String imageUrlStr = "https://cdn.pixabay.com/photo/2013/04/03/12/05/tree-99852__480.jpg";
        //  imageUrlStr = "http://p1.pstatp.com/large/166200019850062839d3";//gif图片

        //显示图片一
        GlideUtil
                .showImage(MainActivity.this, mImageView, imageUrlStr);

//        //显示图片二
//        GlideUtil
//                .showImage(MainActivity.this, mImageViewSec, imageUrlStr, new RequestListener() {
//                    @Override
//                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
//                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                });

//        SimpleTarget target = new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//                // do something with the bitmap
//                // for demonstration purposes, let's just set it to an ImageView
////                Bitmap resource = BitmapFactory.decodeResource(getResources(), R.drawable.progress);
//                Bitmap resource;
//                resource = BitmapWaterMarkerUtil.watermarkBitmap(bitmap, null,
//                        new String[]{" 2015年10月的一天傍晚,中年男子张某在路上看到王123456abcdefghigklmnopqrstuvwxyz1111111111111112222222222222222222a", "明天早上好"},
//                        bitmap.getWidth() / ((float) 720) * getResources()
//                                .getDisplayMetrics().density);
//                mImageViewSec.setImageBitmap(resource);
//            }
//        };
//        Glide.with(this).load(imageUrlStr).asBitmap().into(target);
        Glide.with(this).load(imageUrlStr)
                .transform(new BitmapWaterUtil(this, new String[]{"你好呀杨建波", "yjbo"}))
                .into(mImageViewSec);
    }
}
