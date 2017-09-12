package com.yonyoucloud.glidedemo.util;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * 参考http://blog.csdn.net/kakaxi1o1/article/details/52531128
 * 修改图片缓存的路径
 *
 * @aouto yjbo
 * @time 17/9/3 下午11:19
 */
public class CustomCachingGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        String cachePath = context.getExternalCacheDir() + "/yjboCache";
        builder.setDiskCache(new DiskLruCacheFactory(cachePath, 250 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}