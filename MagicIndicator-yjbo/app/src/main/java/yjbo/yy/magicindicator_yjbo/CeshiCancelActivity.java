package yjbo.yy.magicindicator_yjbo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class CeshiCancelActivity extends AppCompatActivity {

    int imageSize, radius;

    ImageView imageView;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image);
        imageView2 = (ImageView) findViewById(R.id.image2);

        imageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        radius = getResources().getDimensionPixelSize(R.dimen.radius);

        new LoadTask1().execute();
        new LoadTask2().execute();
    }

    class LoadTask1 extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            Bitmap result = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.morendengdai);

            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(0, 0, imageSize, imageSize);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(20);

            paint.setXfermode(null);
            canvas.drawRoundRect(rectF, radius, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(bitmap, rect, rectF, paint);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    class LoadTask2 extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap result = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
//
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.morendengdai);
//
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(10, 10, imageSize-10, imageSize-10);
//
            Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setFilterBitmap(true);
//            paint.setColor(Color.RED);
//            paint.setStrokeWidth(20);
//
            Path path = new Path();
            path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
//            canvas.clipPath(path, Region.Op.INTERSECT);
//
//            canvas.drawBitmap(bitmap, rect, rectF, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);                       //设置画笔为无锯齿
            paint.setColor(Color.RED);                    //设置画笔颜色
            paint.setStrokeWidth((float) 3.0);              //线宽
            paint.setTextSize(50);
            canvas.drawColor(Color.BLACK);                  //白色背景

//            Bitmap bitmap=null;                         //Bitmap对象
//            bitmap=((BitmapDrawable)getResources().getDrawable(R.mipmap.morendengdai)).getBitmap();
            canvas.clipPath(path, Region.Op.INTERSECT);
//            canvas.drawBitmap(bitmap, 5, 5, paint);        //绘制图像
            canvas.drawBitmap(bitmap, rect, rectF, paint);        //绘制图像
            canvas.drawText("n", 50, 50, paint);// 画文本
//            canvas.drawRect(0, 0, 280, 280, paint);// 正方形
            //画图片，就是贴图
             bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            canvas.drawBitmap(bitmap, 100,50, paint);
//            bitmap=((BitmapDrawable)getResources().getDrawable(R.mipmap.morendengdai)).getBitmap();
//            canvas.drawBitmap(bitmap, 50, 150, null);       //绘制图像
//            bitmap=((BitmapDrawable)getResources().getDrawable(R.mipmap.morendengdai)).getBitmap();
//            canvas.drawBitmap(bitmap, 50, 450, null);       //绘制图像
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView2.setImageBitmap(bitmap);
        }
    }

}
