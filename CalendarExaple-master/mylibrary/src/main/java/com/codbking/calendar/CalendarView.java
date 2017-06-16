package com.codbking.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by codbking on 2016/12/18.
 * email:codbking@gmail.com
 * github:https://github.com/codbking
 * blog:http://www.jianshu.com/users/49d47538a2dd/latest_articles
 */

public class CalendarView extends ViewGroup {

    private static final String TAG = "CalendarView";

    private int selectPostion = -1;

    private CaledarAdapter adapter;
    private List<CalendarBean> data;
    private OnItemClickListener onItemClickListener;

    private int row = 6;
    private int column = 7;
    private int itemWidth;
    private int itemHeight;

    private boolean isToday;
    private CalendarBean mSelectBean;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, CalendarBean bean);

        void onItemClickShow(View view, int postion, CalendarBean bean);
    }

    public CalendarView(Context context, int row) {
        super(context);
        this.row = row;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setAdapter(CaledarAdapter adapter) {
        this.adapter = adapter;
    }

    public void setData(List<CalendarBean> data, boolean isToday, CalendarBean bean) {
        this.data = data;
        this.isToday = isToday;
        this.mSelectBean = bean;
        setItem();
        requestLayout();
    }

    private void setItem() {

        selectPostion = -1;
        if (adapter == null) {
            throw new RuntimeException("adapter is null,please setadapter");
        }

        int selectDay = 1;
        if (mSelectBean != null) {
            selectDay = mSelectBean.day;
        }
        int selectMoth = 1;
        if (mSelectBean != null) {
            selectMoth = mSelectBean.moth;
        }
        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            View chidView = adapter.getView(view, this, bean);

            if (view == null || view != chidView) {
                addViewInLayout(chidView, i, chidView.getLayoutParams(), true);
            }
            Log.e("yjbo====00", "当前点击了=7==" + bean.toString() + "===" + selectPostion + "====" + selectDay);
            if (isToday && selectPostion == -1) {
                int[] date = CalendarUtil.getYMD(new Date());
                if (bean.year == date[0] && bean.moth == date[1] && bean.day == date[2]) {
                    selectPostion = i;
                }
            } else {
                Log.e("yjbo====00", "当前点击了=4==" + bean.toString() + "===" + selectPostion + "====" + selectDay);
                if (selectPostion == -1) {
                    if (bean.day == selectDay && bean.moth == selectMoth) {
                        selectPostion = i;
                        if (mSelectBean == null){
                            Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + + selectPostion);
                        }else {
                            Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + mSelectBean.toString()+"===" + selectPostion);
                        }
                    }
                }
            }
            chidView.setSelected(selectPostion == i);
            setItemClick(chidView, i, bean);
        }
    }
    //更新
    public void notifyData() {
    }



    public Object[] getSelect() {
//        Log.e("yjbo====00", "当前点击了=3==" + selectPostion);
//        if (selectPostion == -1 ){
//            selectPostion = 0;
//        }
        return new Object[]{getChildAt(selectPostion), selectPostion, data.get(selectPostion)};
    }

    /**
     * @author yjbo
     * @time 2017/6/13 14:30
     * @qq 1457521527
     */
    public void setClickTime(CalendarBean selectBean) {
        mSelectBean = selectBean;
    }

    public void setItemClick(final View view, final int potsion, final CalendarBean bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.mothFlag == 0) {//不保存除了当月的点击数据，
                    if (selectPostion != -1) {
                        getChildAt(selectPostion).setSelected(false);
                        getChildAt(potsion).setSelected(true);
                    }
                    selectPostion = potsion;
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, potsion, bean);
                }
            }
        });
    }

    public int[] getSelectPostion() {
        Rect rect = new Rect();
        try {
            getChildAt(selectPostion).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{rect.left, rect.top, rect.right, rect.top};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));

        itemWidth = parentWidth / column;
        itemHeight = itemWidth;

        View view = getChildAt(0);
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            itemHeight = params.height;
        }
        setMeasuredDimension(parentWidth, itemHeight * row);


        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
        }

        Log.i(TAG, "onMeasure() called with: itemHeight = [" + itemHeight + "], itemWidth = [" + itemWidth + "]");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            layoutChild(getChildAt(i), i, l, t, r, b);
        }
    }

    private void layoutChild(View view, int postion, int l, int t, int r, int b) {

        int cc = postion % column;
        int cr = postion / column;

        int itemWidth = view.getMeasuredWidth();
        int itemHeight = view.getMeasuredHeight();

        l = cc * itemWidth;
        t = cr * itemHeight;
        r = l + itemWidth;
        b = t + itemHeight;
        view.layout(l, t, r, b);

    }
}
