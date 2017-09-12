package com.yonyoucloud.glidedemo.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yonyoucloud.glidedemo.R;

import java.io.File;

/**
 * Glide图片加载的工具类
 * <p>
 * Created by yjbo on 17/8/31.
 */

public class GlideUtilSimple {

    private static Context mContext;
    private DrawableTypeRequest<String> load;
    private DrawableTypeRequest<Integer> loadInt;
    private DrawableTypeRequest<Byte> loadByte;
    private DrawableTypeRequest<Uri> loadUri;
    private DrawableTypeRequest<File> loadFile;
    private String downloadDirectoryPath;//设置缓存的路径
    private int cacheSize100MegaBytes;//设置缓存的路径

    public GlideUtilSimple(Context context) {
        mContext = context;
    }

    /**
     * @param cachePath 缓存的路径
     * @param cacheMax  缓存的路径
     */
    public GlideUtilSimple(Context context, String cachePath, int cacheMax) {
        mContext = context;
        downloadDirectoryPath = cachePath;
        cacheSize100MegaBytes = cacheMax;

    }

    /**
     * 方法1：最简单方法
     */
    public void showImage(ImageView imageView, Object imageUrlObject) {
        showImage(imageView, imageUrlObject, 0, 0);
    }

    /**
     * 方法1.1：最简单方法，监听回调
     */
    public void showImage(ImageView imageView, Object imageUrlObject, RequestListener requestListener) {
        showImage(imageView, imageUrlObject, 0, 0, 0, 0, 0, requestListener, 0);
    }

    /**
     * 方法2：是否显示占位图，错误图片
     */
    public void showImage(ImageView imageView, Object imageUrlObject, int imageDefult, int imageerror) {
        showImage(imageView, imageUrlObject, imageDefult, imageerror, 0, 0);
    }

    /**
     * 方法3：是否指定图片的像素值（可以起到压缩效果）
     */
    public void showImage(ImageView imageView, Object imageUrlObject, int imageDefult, int imageerror,
                          int wigth, int hight) {
        showImage(imageView, imageUrlObject, imageDefult, imageerror, wigth, hight, 0, null, 0);
    }

    /**
     * @param imageUrlObject 显示的图片的地址
     * @param imageDefult    显示默认的图片：请求图片地址时，首先显示该图片；占位图
     *                       0：赋默认的占位图
     *                       -1：不显示占位图
     *                       其他：占位图
     * @param shape          显示的形状
     * @param imageerror     当显示的图片的地址错误时，显示该图片
     */
    public void showImage(ImageView imageView, Object imageUrlObject, int imageDefult, int imageerror,
                          int wigth, int hight, int shape, RequestListener requestListener, int t) {
        String imageUrlStr;
        int imageUrlInt;
        Byte imageUrlByte;
        Uri imageUrlUri;
        File imageUrlFile;
        if (imageDefult == 0) {
            imageDefult = R.drawable.moren;
        }
        if (imageerror == 0) {
            imageerror = R.drawable.error;
        }
        RequestManager gWith = Glide.with(mContext);

        if (imageUrlObject instanceof String) {//String
            imageUrlStr = String.valueOf(imageUrlObject);

            if (imageDefult == -1) {//是否有默认图片
                gWith.load(imageUrlStr).error(imageerror)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
            } else {
                if (wigth != 0) {//是否指定像素值
                    gWith.load(imageUrlStr).placeholder(imageDefult).error(imageerror)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(wigth, hight).into(imageView);
                } else {
                    //if (requestListener != null) {//监听判空与否不影响；此处不需要做判断
//                        gWith.load(imageUrlStr).placeholder(imageDefult).error(imageerror)
////                                .diskCacheStrategy(DiskCacheStrategy.NONE)
////                                .crossFade(5000)// 可设置时长，默认“300ms”
////                                .listener(requestListener)
////                                .fitCenter()
////                                .transform(new GlideCircleTransform(mContext))//加载圆角图片 http://www.jb51.net/article/98572.htm
//                                .into(imageView);
//                    Glide.with(mContext).load(imageUrlStr).into(imageView);
                    gWith.load(imageUrlStr).placeholder(imageDefult).error(imageerror).into(imageView);
//                    load = gWith.load(imageUrlStr + "");
//                    load.placeholder(imageDefult);
//                    load.error(imageerror);
////                    load.diskCacheStrategy(DiskCacheStrategy.SOURCE);
//                    load.into(imageView);

                }
            }
        } else if (imageUrlObject instanceof Integer) {//Integer
            imageUrlInt = Integer.parseInt(String.valueOf(imageUrlObject));
            gWith.load(imageUrlInt)
                    .placeholder(imageDefult)
                    .error(imageerror)
                    .into(imageView);
        } else if (imageUrlObject instanceof Byte) {//Byte
            imageUrlByte = Byte.valueOf(String.valueOf(imageUrlObject));
            gWith.load(imageUrlByte)
                    .placeholder(imageDefult)
                    .error(imageerror)
                    .into(imageView);
        } else if (imageUrlObject instanceof Uri) {//Uri
            imageUrlUri = Uri.parse(String.valueOf(imageUrlObject));
            gWith.load(imageUrlUri)
                    .placeholder(imageDefult)
                    .error(imageerror)
                    .into(imageView);
        } else if (imageUrlObject instanceof File) {//File
            imageUrlFile = new File(String.valueOf(imageUrlObject));
            gWith.load(imageUrlFile)
                    .placeholder(imageDefult)
                    .error(imageerror)
                    .into(imageView);
        } else {//其它类型
            gWith.load(imageDefult)
                    .placeholder(imageDefult)
                    .error(imageerror)
                    .listener(new RequestListener<Integer, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })

                    .into(imageView);
        }
    }


    //注意：
    // 1.asGIf()；如果将一个不是gif的图片这样强转成gif，则报错，显示errorImage。
    // 2.diskCacheStrategy(DiskCacheStrategy.NONE)不取缓存
    // 3.override(500, 500)取得是图片的像素点，可以写成（5，5）就可以看到效果了，像素很低；它不会影响imageview的长和宽
    //执行gif动画时不能仅显示一张图片
    //BitmapTypeRequest<String> stringBitmapTypeRequest = load.asBitmap();
    //DrawableRequestBuilder<String> placeholder = load.placeholder(imageDefult);
    //DrawableRequestBuilder<String> error = load.error(imageerror);
    //DrawableRequestBuilder<String> stringDrawableRequestBuilder = load.diskCacheStrategy(DiskCacheStrategy.NONE);
    //Target<GlideDrawable> into = load.into(imageView);
    // 4.Glide获取缓存大小并清除缓存图片 http://www.jianshu.com/p/468bd4621f6e
    // 5. 怎样更改Glide的缓存路径http://blog.csdn.net/liulong_/article/details/53678842  http://blog.csdn.net/kakaxi1o1/article/details/52531128
    // 6. Glide 的系列教程 http://www.jianshu.com/p/1e29a0c934a8
}
