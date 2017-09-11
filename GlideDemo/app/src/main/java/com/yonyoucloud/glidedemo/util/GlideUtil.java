package com.yonyoucloud.glidedemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextPaint;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.yonyoucloud.glidedemo.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static void showImagebyUrl(Context context, ImageView imageView, String imageUrlString) {
        showImage(context, imageView, imageUrlString, 0, 0, null);
    }

    /**
     * api：1.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile) {
        showImage(context, imageView, imageUrlFile, 0, 0, null);
    }

    /**
     * api：1.3 极简显示-本地图片-String形式
     *
     * @param imageView       图片控件
     * @param imageUrlFileStr 图片路径
     */
    public static void showImageByFileStr(Context context, ImageView imageView, String imageUrlFileStr) {
        File imageUrlFile = new File(imageUrlFileStr);
        showImage(context, imageView, imageUrlFile, 0, 0, null);
    }

    /**
     * api：2.1 极简显示-网络图片
     *
     * @param imageView      图片控件
     * @param imageUrlString 图片路径
     */
    public static void showImagebyUrl(Context context, ImageView imageView, String imageUrlString,
                                      RequestListener requestListener) {
        showImage(context, imageView, imageUrlString, 0, 0, requestListener);
    }

    /**
     * api：2.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile,
                                       RequestListener requestListener) {
        showImage(context, imageView, imageUrlFile, 0, 0, requestListener);
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
        showImage(context, imageView, imageUrlFile, 0, 0, requestListener);
    }

    /**
     * api:显示数据；不对外提供
     *
     * @param imageUrlObject 显示的图片的地址
     * @param imageDefult    显示默认的图片：请求图片地址时，首先显示该图片；占位图
     * @param imageerror     当显示的图片的地址错误时，显示该图片
     * @aouto yjbo
     * @time 17/8/31 下午2:48
     */
    private static void showImage(Context mContext, ImageView imageView, Object imageUrlObject, int imageDefult, int imageerror,
                                  RequestListener requestListener) {
        int mImageDefult = imageDefult;//加载完成前图片
        int mImageerror = imageerror;//加载结束错误图片
        Object mImageUrlObject = imageUrlObject;//显示的url等图片类型
        RequestListener mRequestListener = requestListener;//图片加载的监听
        DrawableTypeRequest<String> mLoadString;

        //给默认图片赋值
        if (mImageDefult == 0) {
            mImageDefult = R.drawable.progress;
        }
        if (mImageerror == 0) {
            mImageerror = R.drawable.error;
        }

        if (mImageUrlObject instanceof String) {//String
            String imageUrlStr = mImageUrlObject + "";
            mLoadString = Glide.with(mContext).load(imageUrlStr);
            if (mImageDefult != -1) {
                mLoadString.placeholder(mImageDefult);
            }
            if (mImageerror != -1) {
                mLoadString.error(mImageerror);
            }
            //mLoadString.transform(new GlideCircleTransform(mContext));//加载圆角图片 http://www.jb51.net/article/98572.htm

            mLoadString.diskCacheStrategy(DiskCacheStrategy.SOURCE);//只缓存一个尺寸的图片
            mLoadString.listener(mRequestListener);
            mLoadString.into(imageView);

        } else if (mImageUrlObject instanceof Integer) {//Integer
            int imageUrlInt = Integer.parseInt(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlInt)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof Byte) {//Byte
            Byte imageUrlByte = Byte.valueOf(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlByte)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof Uri) {//Uri
            Uri imageUrlUri = Uri.parse(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlUri)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .into(imageView);
        } else if (mImageUrlObject instanceof File) {//File
            File imageUrlFile = new File(String.valueOf(mImageUrlObject));
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

    /**
     * 此处是传入Bitmap图片流进行显示水印的
     *  水印的适用范围：拍照，选择相册时
     *
     * @param src       原始bitmap
     * @param watermark 水印图像
     * @param title     水印文字
     * @return Bitmap    添加水印后的bitmap
     * @Title: watermarkBitmap
     * @Description: 给bitmap 添加水印
     */
    public static File watermarkBitmap(File src, Bitmap watermark, String[] title, float density) {
        if (src == null || !src.exists()) {
            return null;
        }
        //压缩 http://blog.csdn.net/baidu_29835301/article/details/50394516
        Bitmap bitmap = BitmapFactory.decodeFile(src.getAbsolutePath());

        Bitmap bitmapNew = watermarkBitmap(bitmap, watermark, title, density);
        File file = saveFile(bitmapNew, src.getPath(), src.getName());
        return file;
    }

    /**
     * 此处是传入Bitmap图片流进行显示水印的
     * 使用的应该不频繁
     *
     * @param src       原始bitmap
     * @param watermark 水印图像
     * @param title     水印文字
     * @return Bitmap    添加水印后的bitmap
     * @Title: watermarkBitmap
     * @Description: 给bitmap 添加水印
     */
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String[] title, float density) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Canvas cv = new Canvas(src);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();
        // 加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
        }
        // 加入文字
        if (title != null) {
            String familyName = "宋体";
            Typeface font = Typeface.create(familyName, Typeface.NORMAL);

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.parseColor("#000000"));
            textPaint.setTypeface(font);
            textPaint.setTextSize(15 * density);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            int t0length = title[0].length();
            int countPer = 18;//每行的个数
            int per = t0length / countPer;//行数（不准确的）
            if (t0length % countPer != 0) {//准确的行数
                per = per + 1;
            }
            // 这里是自动换行的
            for (int i = per - 1; i >= 0; i--) {//这相当于文字分行
                if (i == per - 1) {//最后一行
                    cv.drawText(title[0].trim().substring(i * countPer, t0length),
                            w - 8 * density, h - ((per - 1 - i) * 20 + 28 + 20) * density, textPaint);
                } else {
                    cv.drawText(title[0].trim().substring(0 + i * countPer, 0 + i * countPer + countPer),
                            w - 8 * density, h - ((per - 1 - i) * 20 + 28 + 20) * density, textPaint);
                }
            }

            cv.drawText(title[1].trim(), w - 8 * density, h - 28 * density, textPaint);
            textPaint.setTextSize(16 * density);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = new Date();
            String dateString = format.format(date);

            cv.drawText(dateString.trim(), w - 8 * density, h - 8 * density, textPaint);

        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return src;
    }

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *      参考：http://www.360doc.com/content/14/0428/17/11800748_372972179.shtml
     * @param bm
     * @param fileName
     */
    public static File saveFile(Bitmap bm, String path, String fileName) {
        try {
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myCaptureFile = new File(path, fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return myCaptureFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
