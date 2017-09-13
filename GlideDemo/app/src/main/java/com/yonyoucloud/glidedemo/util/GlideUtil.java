package com.yonyoucloud.glidedemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextPaint;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.yonyoucloud.glidedemo.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 图片加载的工具类
 * 中文api参考：http://www.jianshu.com/p/7610bdbbad17
 * adb shell setprop log.tag.GenericRequest DEBUG
 * <p>
 * Created by yjbo on 17/8/31.
 */

public class GlideUtil {
    //默认图片宽度最大值
    private static int wigthDef = 720;
    /*-------------------------------------一.图片显示工具类-----------------------------------*/
    /*----------------------------------------1.极简的----------------------------------------*/

    /**
     * api：1.1.1 极简显示-网络图片
     *
     * @param imageView      图片控件
     * @param imageUrlString 图片路径
     */
    public static void showImageByUrl(Context context, ImageView imageView, String imageUrlString) {
        showImage(context, imageView, imageUrlString, 0, 0, null);
    }

    /**
     * api：1.1.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile) {
        showImage(context, imageView, imageUrlFile, 0, 0, null);
    }

    /**
     * api：1.1.3 极简显示-本地图片-String形式
     *
     * @param imageView       图片控件
     * @param imageUrlFileStr 图片路径
     */
    public static void showImageByFileStr(Context context, ImageView imageView, String imageUrlFileStr) {
        File imageUrlFile = new File(imageUrlFileStr);
        showImage(context, imageView, imageUrlFile, 0, 0, null);
    }

    /**
     * api：1.1.4 Byte 本地图片
     *
     * @param imageView    图片控件
     * @param imageUrlByte 图片的2进制流
     */
    public static void showImageByByte(Context context, ImageView imageView, byte[] imageUrlByte) {
        Glide.with(context).load(imageUrlByte)
                .placeholder(R.drawable.moren)
                .error(R.drawable.error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * api：1.1.5 Integer 本地图片
     *
     * @param imageView   图片控件
     * @param imageUrlInt 图片的int型，比如drawable，mipable中的图片
     */
    public static void showImageByInt(Context context, ImageView imageView, int imageUrlInt) {
        showImage(context, imageView, imageUrlInt, 0, 0, null);
    }

    /**
     * api：1.1.6 Uri 本地图片
     *
     * @param imageView   图片控件
     * @param imageUrlInt 图片的int型，比如drawable，mipable中的图片
     */
    public static void showImageByUri(Context context, ImageView imageView, Uri imageUrlInt) {
        showImage(context, imageView, imageUrlInt, 0, 0, null);
    }



    /*-----------------------2.极简的，多一个回调（只有网络图片和本地图片加了此功能）--------------------*/

    /**
     * api：1.2.1 极简显示-网络图片
     *
     * @param imageView      图片控件
     * @param imageUrlString 图片路径
     */
    public static void showImageByUrl(Context context, ImageView imageView, String imageUrlString,
                                      RequestListener requestListener) {
        showImage(context, imageView, imageUrlString, 0, 0, requestListener);
    }

    /**
     * api：1.2.2 极简显示-本地图片-file形式
     *
     * @param imageView    图片控件
     * @param imageUrlFile 图片路径
     */
    public static void showImageByFile(Context context, ImageView imageView, File imageUrlFile,
                                       RequestListener requestListener) {
        showImage(context, imageView, imageUrlFile, 0, 0, requestListener);
    }

    /**
     * api：1.2.3 极简显示-本地图片-String形式
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
    private static void showImage(Context mContext, ImageView imageView, Object imageUrlObject,
                                  int imageDefult, int imageerror, RequestListener requestListener) {
        int mImageDefult = imageDefult;//加载完成前图片
        int mImageerror = imageerror;//加载结束错误图片
        Object mImageUrlObject = imageUrlObject;//显示的url等图片类型
        RequestListener mRequestListener = requestListener;//图片加载的监听
        DrawableTypeRequest<String> mLoadString;

        //给默认图片赋值
        if (mImageDefult == 0) {
            mImageDefult = R.drawable.moren;
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
            mLoadString.centerCrop();
            mLoadString.diskCacheStrategy(DiskCacheStrategy.SOURCE);//只缓存一个尺寸的图片
            mLoadString.listener(mRequestListener);
            mLoadString.into(imageView);

        } else if (mImageUrlObject instanceof Integer) {//Integer
            int imageUrlInt = Integer.parseInt(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlInt)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } else if (mImageUrlObject instanceof Uri) {//Uri
            Uri imageUrlUri = Uri.parse(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlUri)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } else if (mImageUrlObject instanceof File) {//File
            File imageUrlFile = new File(String.valueOf(mImageUrlObject));
            Glide.with(mContext).load(imageUrlFile)
                    .placeholder(mImageDefult)
                    .error(mImageerror)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } else {//其它类型，不支持的类型
            imageView.setBackgroundResource(R.drawable.moren);
        }

    }

    /*------------------------------------二.图片其他工具类-------------------------------------------*/
    /*------------------------------------1.给文件添加水印-------------------------------------------*/

    /**
     * api：2.1.1 给文件添加水印
     *
     * @param src       原始bitmap
     * @param watermark 水印图像
     * @param title     水印文字
     * @param density   屏幕密度
     * @return File    添加水印后保存的文件名称
     * @Description: 此处是传入File，Bitmap图片流进行显示水印的
     * 水印的适用范围：拍照，选择相册时
     */
    public static File watermarkBitmap(File src, Bitmap watermark, String[] title, float density) {
        return watermarkBitmap(src, watermark, title, density, false);
    }

    /**
     * api：2.1.2 给图片添加水印
     *
     * @param newName 是否给加了水印的图片重新命名,暂未处理
     * @return File    添加水印后保存的文件名称
     * @Description: 此处是传入File，Bitmap图片流进行显示水印的
     * 水印的适用范围：拍照，选择相册时
     */
    public static File watermarkBitmap(File src, Bitmap watermark, String[] title, float density, boolean newName) {
        if (src == null || !src.exists()) {
            return null;
        }
        //先将文件转为Bitmap文件流
        String filePath = src.getParent() + "/";
        String fileName = src.getName();

        CompressBitmap(filePath + fileName, fileName, 720);

        // 如果添加水印就将图片旋转到正常的角度，添加水印后，要再次压缩图片。
        int angle = getBitmapDegree(filePath + fileName);
        Bitmap bitmap = getBitmapFormPath(filePath + fileName, angle);
        // Bitmap bitmap =BitmapFactory.decodeFile(filePath+fileName);
        Bitmap bitmapNew = watermarkBitmap(bitmap, watermark, title, density);
        //也可以给加了水印的图片重新命名
        File file = saveFile(bitmapNew, filePath, fileName);
        return file;
    }

    /**
     * api：2.1.3 给图片流添加水印
     * <p>
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
//        if (w > wigthDef) {//处理照片的长和宽，否则会出现大小溢出
//            h = w * wigthDef / w;
//            w = wigthDef;
//        }
        density  =    w * density / wigthDef;
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);// 创建一个新的和SRC长度宽度一样的位图
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
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
        return newb;
    }

    /*------------------------------------2.将Bitmap保存为文件，并压缩-------------------------------------------*/

    /**
     * api:2.2.1 将Bitmap保存为文件，并压缩
     *
     * @param bm       需要压缩的图片流
     * @param fileName 压缩后的文件所叫的名称
     * @param path     压缩之后放入哪个文件夹内
     * @deprecated 将Bitmap转换成文件
     * 保存文件,并压缩了
     * 参考：http://www.360doc.com/content/14/0428/17/11800748_372972179.shtml
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
            //压缩
            CompressBitmap(path + fileName, fileName, 720);
            return myCaptureFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /*------------------------------------3.压缩图片-------------------------------------------*/

    /**
     * api:2.3.1 将File文件压缩，并保存为文件
     *
     * @param filePath 文件的目录
     * @param filename 文件的名称
     * @param width    设置压缩的宽度
     * @aouto yjbo  17/9/12 上午11:16
     */
    public static void CompressBitmap(String filePath, String filename, int width) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //获取图片的宽高，这里要设置Options.inJustDecodeBounds=true,这时候decode的bitmap为null,只是把图片的宽高放在Options里
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            Bitmap bitmap;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            File file = new File(filePath);
            //先将图片改成默认的长宽大小（此处设置宽度720）
            if (file.length() > 204800) {
                options.inSampleSize = calculateInSampleSize(options, width);
            } else {
                options.inSampleSize = 1;
            }

            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filePath, options);
            if (bitmap == null){
            }
            if (filename.toLowerCase(Locale.US).contains(".png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            }

            if (baos.size() > 204800) {
                int quality = 90;
                while (baos.size() > 204800) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    baos = rateCompress(bitmap, quality);
                    if (quality <= 20)
                        break;
                    quality = quality - 40;
                }
            }
            byte[] b = baos.toByteArray();
            //写入文件
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(b);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * api:2.3.2 计算图片的长宽，设置需求的宽度，计算出压缩比例options.inSampleSize
     * 图片尺寸修改方法
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        final int reqHeight = reqWidth / 2 * height / width;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) (reqHeight / 2));
            final int widthRatio = Math.round((float) width / (float) (reqWidth / 2));
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * PicCompressUtil.java [V1.00] classpath:wa.android.common.utils MethodName
     * : rateCompress Return : ByteArrayOutputStream guowla create at
     * 2014-8-8下午4:39:13 像素比例压缩方法
     */
    public static ByteArrayOutputStream rateCompress(Bitmap bitmap, int rate) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, rate, baos);
        return baos;
    }


    /*------------------------------------4.获取图片的角度，并旋转-------------------------------------------*/

    /**
     * api：2.4.1 根据文件，旋转角度，旋转图片文件,获取图片流
     *
     * @param filePath 文件路径（ps：如路径错误 返回null）
     * @param angle    旋转角度 （getBitmapDegree方法获取）
     * @return
     */
    public static Bitmap getBitmapFormPath(String filePath, int angle) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap == null) {
            return null;
        }
        bitmap = getBitmapWithAngle(bitmap, angle);
        return bitmap;
    }

    /**
     * api：2.4.2 根据图片流，旋转角度 旋转图片,获取图片流
     *
     * @param bitmap 源图片
     * @param angle  旋转角度
     * @return
     */
    public static Bitmap getBitmapWithAngle(Bitmap bitmap, int angle) {
        if (angle != 0) {
            Matrix m = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            m.setRotate(angle); // 旋转angle度
            try {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m,
                        true);// 从新生成图片
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * api：2.4.3 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
