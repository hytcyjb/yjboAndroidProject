package com.yy.yjbo.yjboimagecanvas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 基于此做矩形的四周圆角加边框的功能；
 * @author yjbo
 * @time 2017/6/1 20:50
 * @qq 1457521527
*/
/**
 * 圆形图标，原作者，https://github.com/hdodenhof/CircleImageView
 * @author gongshoudao
 * @Description TODO
 * @date 2014-9-16 下午4:00:01
 */
@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mRectFourSide = 0;
    private int mShape = 0;//默认是圆形，1是矩形四边有圆角

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;
    private String iconUrl;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
        mRectFourSide = a.getDimensionPixelSize(R.styleable.CircleImageView_rect_four_side, DEFAULT_BORDER_WIDTH);
        mShape = a.getInt(R.styleable.CircleImageView_shape, DEFAULT_BORDER_WIDTH);

        a.recycle();

        mReady = true;

        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        if (mShape == 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        } else if (mShape == 1) {
            //234====156====98.0====99.0
            Log.e("---k-h--", mBitmap.getWidth() + "====" + mBitmap.getHeight() + "====" + mDrawableRadius + "====" + mBorderRadius
                    + "====" + getWidth() + "====" + getHeight());
            RectF rectF2 = new RectF(mBorderWidth, mBorderWidth, getWidth() - mBorderWidth * 2, getHeight() - mBorderWidth * 2);
            canvas.drawRoundRect(rectF2, mRectFourSide, mRectFourSide, mBitmapPaint);//图像
            canvas.drawRoundRect(mBorderRect, mRectFourSide, mRectFourSide, mBorderPaint);//边框
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }

        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }

        mBorderWidth = borderWidth;
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (mBitmap == null) {
            return;
        }

        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        if (mShape == 0) {
            mBorderRect.set(0, 0, getWidth(), getHeight());
            mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
            mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
            mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
        } else if (mShape == 1) {
            mBorderRect.set(mBorderWidth, mBorderWidth, getWidth() - mBorderWidth * 2, getHeight() - mBorderWidth * 2);
        }


        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale = 0;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);

        if (mShape == 0) {
            if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
                scale = mDrawableRect.height() / (float) mBitmapHeight;
                dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = mDrawableRect.width() / (float) mBitmapWidth;
                dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
            }
        } else if (mShape == 1) {
            if (mBitmapWidth * getHeight() > getWidth() * mBitmapHeight) {
                scale = getHeight() / (float) mBitmapHeight;
                dx = (getWidth() - mBitmapWidth * scale) * 0.5f;
            } else {
                scale = getWidth() / (float) mBitmapWidth;
                dy = (getHeight() - mBitmapHeight * scale) * 0.5f;
            }
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
