package com.codbking.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


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
    private int currentSelect = -1;//当前月份选中的位置
    private int mCurrentPage = -1;//当前月份选中的位置
    private int mCurrentSelectPage = -1;//当前月份选中的位置
//    private int count = 0;//viewpage 一共缓存了3个页面，那么也就是只显示一个页面
    private Object[] viewContent = new Object[]{};//视图内容

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
    public void setData(List<CalendarBean> data, boolean isToday, CalendarBean bean,int currentSelectPage,int currentPage) {
        this.data = data;
        this.isToday = isToday;
        this.mSelectBean = bean;
        this.mCurrentPage = currentPage;
        this.mCurrentSelectPage = currentSelectPage;
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
        int firstOfMonth = -1;//一个月中的第一天

        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            Log.e("yjbo====00", "当前点击了=7==" + bean.toString() + "===" + selectPostion + "====" + selectDay);

            if (isToday && selectPostion == -1 && bean.mothFlag == 0) {
                int[] date = CalendarUtil.getYMD(new Date());
                if (bean.year == date[0] && bean.moth == date[1] && bean.day == date[2]) {
                    selectPostion = i;
                }
            } else {
                Log.e("yjbo====00", "当前点击了=4==" + bean.toString() + "===" + selectPostion + "====" + selectDay);
                if (selectPostion == -1 && bean.mothFlag == 0) {
                    if (mSelectBean == null) {
                        if (bean.day == selectDay) {//每月的周一
                            selectPostion = i;
                        }
                        Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + +selectPostion);
                    } else {
                        if (bean.day == selectDay && bean.moth == selectMoth) {
                            selectPostion = i;
                        }
                        Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + mSelectBean.toString() + "===" + selectPostion);
                    }
                }
            }
            //取出一个月的第一天
            if (bean.day == 1 && bean.mothFlag == 0) {
                firstOfMonth = i;
            }
            //如果都到结尾了都没有数据，那就取一个月的第一天
            if (i == data.size() - 1) {//最后一个数字
                if (selectPostion == -1) {
//                    if (bean.day == 1) {//每月的周一
                    selectPostion = firstOfMonth;
//                    }
                }
            }
            if (bean.mothFlag == 0) {
                if (selectPostion != -1) {
                    Log.d("==yjbo==", "===当前月份选中的日期=" + selectPostion);
                    currentSelect = selectPostion;
                }
            }
        }
        for (int i = 0; i < data.size(); i++) {
            CalendarBean bean = data.get(i);
            View view = getChildAt(i);
            View chidView = adapter.getView(view, this, bean);

            if (view == null || view != chidView) {
                addViewInLayout(chidView, i, chidView.getLayoutParams(), true);
            }
            Log.e("yjbo====00", "当前点击了=7==" + bean.toString() + "===" + selectPostion + "====" + selectDay);

//            if (isToday && selectPostion == -1 && bean.mothFlag == 0) {
//                int[] date = CalendarUtil.getYMD(new Date());
//                if (bean.year == date[0] && bean.moth == date[1] && bean.day == date[2]) {
//                    selectPostion = i;
//                }
//            } else {
//                Log.e("yjbo====00", "当前点击了=4==" + bean.toString() + "===" + selectPostion + "====" + selectDay);
//                if (selectPostion == -1 && bean.mothFlag == 0) {
//                    if (mSelectBean == null){
//                        if (bean.day == selectDay) {//每月的周一
//                            selectPostion = i;
//                        }
//                        Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + + selectPostion);
//                    }else {
//                        if (bean.day == selectDay && bean.moth == selectMoth) {
//                            selectPostion = i;
//                        }
//                        Log.e("yjbo====00", "当前点击了=5==" + bean.toString() + "===" + mSelectBean.toString()+"===" + selectPostion);
//                    }
//                }
//            }
            //            //如果最终就没有一个选择项，那就获取每月的1号
//            if (selectPostion == -1){
//                if (bean.day == 1) {//每月的一号
//                    selectPostion = i;
//                }
//            }
//            if (i == data.size() -1){//最后一个数字
//                if (selectPostion == -1){
//                    if (bean.day == 1) {//每月的周一
//                        selectPostion = i;
//                    }
//                }
//            }
            if (selectPostion == i) {
//                setViewContent(getChildAt(i), i, data.get(i));
                setTag(new Object[]{getChildAt(i), i, data.get(i)});
                Log.e("yjbo----", "初始化==2=="+data.get(i).toString()+"===="+mCurrentPage +"===="+ mCurrentSelectPage);
                if (mCurrentPage == mCurrentSelectPage) {//getChildAt(mSelectBean.)  mSelectBean.day
                    onItemClickListener.onItemClickShow(getChildAt(i), selectPostion, data.get(i));
                }
//                if (bean.mothFlag == 0) {
//                    count++;
//                    if (count == 2) {
//                        Toast.makeText(getContext(), data.get(i).toString(), Toast.LENGTH_SHORT).show();
//                        onItemClickListener.onItemClickShow(getChildAt(i), i, data.get(i));
//                    }else  if (count == 3){
//                        count = 0;
//                    }
//                }
            }
            chidView.setSelected(selectPostion == i);
            setItemClick(chidView, i, bean);
        }
    }

    //更新
    public void notifyData() {
    }


    private void setViewContent(View view, int selectP, CalendarBean calendarBean) {

        this.viewContent = new Object[]{getChildAt(selectP), selectP, calendarBean};
    }

    public Object[] getViewContent() {
        return viewContent;
    }

    public Object[] getSelect() {
        Log.e("yjbo====00", "当前点击了=3==" + selectPostion);
        if (selectPostion == -1) {
//            Toast.makeText(getContext(), "-----怎么会出现这种情况----", Toast.LENGTH_SHORT).show();
            return null;
//           selectPostion = 0;
        }
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
