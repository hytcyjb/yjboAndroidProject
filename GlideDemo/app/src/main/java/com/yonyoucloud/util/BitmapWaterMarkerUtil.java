package com.yonyoucloud.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zeven
 * @ClassName: BitmapWaterMarkerUtil
 * @Description: bitmap 处理类
 * @date 2015年6月3日 下午1:05:18
 */
public class BitmapWaterMarkerUtil {
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
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, String[] title, float density) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
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
                    cv.drawText(title[0].trim().substring(i * countPer, t0length-1),
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
            // 这里是自动换行的
//			StaticLayout layout = new StaticLayout(title, textPaint, w, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//			layout.draw(cv);
            // 文字就加左上角算了
            // cv.drawText(title,0,40,paint);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }


    /**
     * 该方法和上方法区别为不带时间
     *
     * @param src
     * @param watermark
     * @param title
     * @param density
     * @return
     */
    public static Bitmap watermarkBitmapWithoutTime(Bitmap src, Bitmap watermark, String[] title, float density) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
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
            textPaint.setColor(0xFFD98411);
            textPaint.setTypeface(font);
            textPaint.setTextSize(18 * density);
            textPaint.setTextAlign(Paint.Align.RIGHT);

            cv.drawText(title[0].trim(), w - 8 * density, h - 28 * density, textPaint);
            cv.drawText(title[1].trim(), w - 8 * density, h - 8 * density, textPaint);
            textPaint.setTextSize(16 * density);

            // 这里是自动换行的
//       StaticLayout layout = new StaticLayout(title, textPaint, w, Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//       layout.draw(cv);
            // 文字就加左上角算了
            // cv.drawText(title,0,40,paint);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
}
