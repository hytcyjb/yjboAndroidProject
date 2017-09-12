package com.yonyoucloud.glidedemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.yonyoucloud.AMap.activity.ShowMapActivity;
import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.glidedemo.util.GlideCacheUtil;
import com.yonyoucloud.glidedemo.util.GlideUtil;
import com.yonyoucloud.util.BitmapWaterMarkerUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * glide的demo，封装api
 *
 * @author yjbo
 * @data 17/8/31 上午11:11
 */
public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mImageViewSec;
    private String imageUrlStr = "https://cdn.pixabay.com/photo/2013/04/03/12/05/tree-99852__480.jpg";
    private String imageUrlStr2 = "https://cdn.pixabay.com/photo/2015/11/19/20/40/leaves-1051937__480.jpg";

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

        showImageOne(6);

        //显示图片二
        GlideUtil
                .showImageByUrl(MainActivity.this, mImageViewSec, imageUrlStr2, new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Toast.makeText(MainActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

    }

    //显示图片一
    private void showImageOne(int i) {
        String mCachePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/myImage/20170911160033.jpg";
        switch (i) {
            case 0://url api 1.1.1
                GlideUtil
                        .showImageByUrl(MainActivity.this, mImageView, imageUrlStr);
                break;
            case 1://File api 1.1.2

                GlideUtil
                        .showImageByFile(MainActivity.this, mImageView, new File(mCachePath));
                break;
            case 2://File String api 1.1.3
                GlideUtil
                        .showImageByFileStr(MainActivity.this, mImageView, mCachePath);
                break;
            case 3://1.1.4 Byte字节流显示图片
                byte[] bitmapByte = getBitmapByte(BitmapFactory.decodeResource(getResources(),
                        R.drawable.iptcnpqc));
                GlideUtil
                        .showImageByByte(MainActivity.this, mImageView, bitmapByte);
                break;
            case 4://1.1.5 Integer类型显示图片（比如drawable下的图片）
                GlideUtil
                        .showImageByInt(MainActivity.this, mImageView, R.drawable.progress);
                break;
            case 5://1.1.6 Uri 本地图片
                GlideUtil
                        .showImageByUri(MainActivity.this, mImageView, resourceIdToUri(MainActivity.this
                                ,R.drawable.iptcnpqc));
                break;
            case 6://2.1.1 给文件添加水印,并显示
                GlideUtil
                        .watermarkBitmap(new File(mCachePath),null,new String[]{"海定区","yjbo"},
                        MainActivity.this.getResources().getDisplayMetrics().density);

                GlideUtil
                        .showImageByFile(MainActivity.this, mImageView, new File(mCachePath));
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            default:
                break;
        }

    }

    public byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //参数1转换类型，参数2压缩质量，参数3字节流资源
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

}