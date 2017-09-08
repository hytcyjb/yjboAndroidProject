package com.yonyoucloud.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.yonyoucloud.glidedemo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zeven
 * @ClassName: BitmapWaterMarkerUtil
 * @Description: bitmap 处理类
 * @date 2015年6月3日 下午1:05:18
 */
public class BitmapWaterUtil extends BitmapTransformation {
    /**
     * @param src       原始bitmap
     * @param watermark 水印图像
     * @param title     水印文字
     * @return Bitmap    添加水印后的bitmap
     * @Title: watermarkBitmap
     * @Description: 给bitmap 添加水印
     * @time 2015年6月3日 下午1:05:49
     * @author zeven
     */
    public  Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String[] title, float density) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        Canvas cv = new Canvas(src);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();
        // 加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
//            paint.setAlpha(50);
            paint.setStyle(Paint.Style.FILL);
            cv.drawRect(w - ww - 18, h - wh, w, h, paint);
            cv.drawBitmap(watermark, w - ww -9, h - wh, null);// 在src的右下角画入水印
        }
        // 加入文字
        if (title != null) {
            String familyName = "宋体";
            Typeface font = Typeface.create(familyName, Typeface.NORMAL);

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(0xFFD98411);
            textPaint.setTypeface(font);
            textPaint.setTextSize(15 * density);
            textPaint.setTextAlign(Paint.Align.RIGHT);
            int t0length = title[0].length();
            int countPer = 18;//每行的个数
            int per = t0length / countPer;//行数（不准确的）
            if (t0length % countPer != 0) {//准确的行数
                per = per + 1;
            }
            for (int i = per - 1; i >= 0; i--) {//这相当于文字分行
                if (i == per - 1) {//最后一行
                    cv.drawText(title[0].trim().substring(i * countPer, t0length - 1),
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


    private Paint paint;
    private Context mContext;
    private String[] mWaterContent;

    public BitmapWaterUtil(Context context, String[] waterContent) {
        super(context);
        mContext = context;
        mWaterContent = waterContent;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#469de6"));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.error_water);
        return watermarkBitmap(toTransform, bitmap, mWaterContent,
                toTransform.getWidth() / ((float) 720) * mContext.getResources()
                        .getDisplayMetrics().density);
    }

    @Override
    public String getId() {
        return "com.yonyoucloud.util.BitmapWaterUtil";
    }
}
