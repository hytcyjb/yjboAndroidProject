package com.yonyoucloud.glidedemo.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.yonyoucloud.glidedemo.R;

import java.io.File;

/**
 * 图片加载的工具类
 * 中文api参考：http://www.jianshu.com/p/7610bdbbad17
 * adb shell setprop log.tag.GenericRequest DEBUG
 * <p>
 * Created by yjbo on 17/8/31.
 */

public class GlideUtil {

    /**
     * api：1.1 极简显示-网络图片
     *
     * @param imageView      图片控件
     * @param imageUrlString 图片路径
     */
    public static void showImage(Context context, ImageView imageView, String imageUrlString) {
        showImage(context, imageView, imageUrlString, 0, 0, 1, null);
    }

    /**
     * api：1.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile) {
        showImage(context, imageView, imageUrlFile, 0, 0, 0, null);
    }

    /**
     * api：1.3 极简显示-本地图片-String形式
     *
     * @param imageView       图片控件
     * @param imageUrlFileStr 图片路径
     */
    public static void showImageByFileStr(Context context, ImageView imageView, String imageUrlFileStr) {
        File imageUrlFile = new File(imageUrlFileStr);
        showImage(context, imageView, imageUrlFile, 0, 0, 0, null);
    }

    /**
     * api：2.1 极简显示-网络图片
     *
     * @param imageView      图片控件
     * @param imageUrlString 图片路径
     */
    public static void showImage(Context context, ImageView imageView, String imageUrlString,
                                 RequestListener requestListener) {
        showImage(context, imageView, imageUrlString, 0, 0, 0, requestListener);
    }

    /**
     * api：2.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile,
                                       RequestListener requestListener) {
        showImage(context, imageView, imageUrlFile, 0, 0, 0, requestListener);
    }

    /**
     * api：2.3 极简显示-本地图片-String形式
     *
     * @param imageView       图片控件
     * @param imageUrlFileStr 图片路径
     */
    public static void showImageByFileStr(Context context, ImageView imageView, String imageUrlFileStr,
                                          RequestListener requestListener) {
        File imageUrlFile = new File(imageUrlFileStr);
        showImage(context, imageView, imageUrlFile, 0, 0, 0, requestListener);
    }

    /**
     * api:显示数据
     *
     * @param imageUrlObject 显示的图片的地址
     * @param imageDefult    显示默认的图片：请求图片地址时，首先显示该图片；占位图
     * @param imageerror     当显示的图片的地址错误时，显示该图片
     * @param shape          0: 默认
     *                       1：圆形
     * @aouto yjbo
     * @time 17/8/31 下午2:48
     */
    public static void showImage(Context mContext, ImageView imageView, Object imageUrlObject, int imageDefult, int imageerror,
                                 int shape, RequestListener requestListener) {
//        Context mContext = context;
        int mImageDefult;//加载完成前图片
        int mImageerror;//加载结束错误图片
        Object mImageUrlObject;//显示的url等图片类型
        int mShape;//mShape :1圆形图片，...
        RequestListener mRequestListener;//图片加载的监听
        DrawableTypeRequest<String> mLoadString;

        mImageUrlObject = imageUrlObject;
        mShape = shape;
        mRequestListener = requestListener;
        mImageDefult = imageDefult;
        mImageerror = imageerror;

        //给默认图片赋值
        if (mImageDefult == 0) {
            mImageDefult = R.drawable.progress;
        }
        if (mImageerror == 0) {
            mImageerror = R.drawable.error;
        }


        String imageUrlStr;
        int imageUrlInt;
        Byte imageUrlByte;
        Uri imageUrlUri;
        File imageUrlFile;

        if (mImageUrlObject instanceof String) {//String
            imageUrlStr = mImageUrlObject + "";
            mLoadString = Glide.with(mContext).load(imageUrlStr);
            if (mImageDefult != -1) {
                mLoadString.placeholder(mImageDefult);
            }
            if (mImageerror != -1) {
                mLoadString.error(mImageerror);
            }
            if (mShape == 1) {
                mLoadString.transform(new GlideCircleTransform(mContext));//加载圆角图片 http://www.jb51.net/article/98572.htm
            }
            mLoadString.diskCacheStrategy(DiskCacheStrategy.SOURCE);//只缓存一个尺寸的图片
            mLoadString.listener(mRequestListener);
            mLoadString.into(imageView);

        } else if (mImageUrlObject instanceof Integer) {//Integer
            imageUrlInt = Integer.parseInt(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlInt)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof Byte) {//Byte
            imageUrlByte = Byte.valueOf(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlByte)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof Uri) {//Uri
            imageUrlUri = Uri.parse(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlUri)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof File) {//File
            imageUrlFile = new File(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlFile)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else {//其它类型
            Glide.with(mContext).load(mImageUrlObject)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        }

    }

}
