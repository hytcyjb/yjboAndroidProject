package com.yy.yjbo.yjbocalendar.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yy.yjbo.yjbocalendar.R;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.yy.yjbo.yjbocalendar.calendar.CalendarFactory.getMonthOfDayList;

/**
 * Created by codbking on 2016/12/18.
 * email:codbking@gmail.com
 * github:https://github.com/codbking
 * blog:http://www.jianshu.com/users/49d47538a2dd/latest_articles
 */

public class CalendarDateView extends ViewPager implements CalendarTopView {

    HashMap<Integer, CalendarView> views = new HashMap<>();
    public CaledarTopViewChangeListener mCaledarLayoutChangeListener;
    private CaledarOnItemListener mCaledarOnItemLis;
    private CalendarView.OnItemClickListener onItemClickListener;

    private LinkedList<CalendarView> cache = new LinkedList();

    private int MAXCOUNT = 6;


    private int row = 6;

    private CaledarAdapter mAdapter;
    private int calendarItemHeight = 0;
    private CalendarBean mSelectBean;
    CalendarView view;
    private int currentSelectPage = -1;
    private boolean isClick = false;//是否是点击跳转，false就是不是点击---即滑动

    public void setAdapter(CaledarAdapter adapter) {
        mAdapter = adapter;
        initData();
    }

    public void notifyData() {
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        row = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 6);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                calendarItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    //滑动时先走onpageEnd,然后再走这里初始化
    private void init() {
        final int[] dateArr = CalendarUtil.getYMD(new Date());

        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //http://lovelease.iteye.com/blog/2107296
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {


                if (!cache.isEmpty()) {
                    view = cache.removeFirst();
                } else {
                    view = new CalendarView(container.getContext(), row);
                }
                //正常的点击事件
                if (onItemClickListener != null) {
                    view.setOnItemClickListener(onItemClickListener);
                }
                view.setAdapter(mAdapter);
//                if (mSelectBean != null) {
//                    Log.e("yjbo====00", "当前点击了=0==" + mSelectBean.day);
//                }else {
//                    Log.e("yjbo====00", "当前点击了=1==" + mSelectBean);
//                }
                Log.e("yjbo----", "初始化==页面==");
//                Toast.makeText(getContext(), "你好----", Toast.LENGTH_SHORT).show();
                if (currentSelectPage == -1) {
                    currentSelectPage = position;
                }
                view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2, mSelectBean, currentSelectPage, position);

                container.addView(view);
                views.put(position, view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                cache.addLast((CalendarView) object);
                views.remove(position);
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.e("yjbo----", "初始化==滑动结束==");

                if (onItemClickListener != null) {//初始化也走这边，所以进行判断
                    if (!isClick) {//如果不是点击跳转，而是滑动跳转的话，直接显示标题，
//                        isClick = true;
                        final int[] dateArr = CalendarUtil.getYMD(new Date());
                        CalendarBean calendarBean = new CalendarBean();
                        calendarBean.year = dateArr[0];
                        calendarBean.moth = dateArr[1] + position - Integer.MAX_VALUE / 2;
                        if (position == Integer.MAX_VALUE / 2) {
                            calendarBean.day = dateArr[2];
                        }else {
                            calendarBean.day = 1;//有问题，不能显示上个时间节点遗留的日期
                        }
                        onItemClickListener.onItemClickShow(null, -1, calendarBean);
                    }
                }
                isClick = false;
            }
        });
    }

    Handler mHandler = new Handler();

    private void initData() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();
    }

    public void updateData() {
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public int[] getCurrentSelectPositon() {
        CalendarView view = views.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectPostion();
        }
        return new int[4];
    }

    @Override
    public int getItemHeight() {
        return calendarItemHeight;
    }

    @Override
    public void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener) {
        mCaledarLayoutChangeListener = listener;
    }

    public void setCaledarOnItemListener() {
        if (mCaledarLayoutChangeListener != null)
            mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
    }

    /**
     * i:往上一页还是下一页翻转
     * selectBean:当前选中的日期
     *
     * @author yjbo
     * @time 2017/6/13 14:13
     * @qq 1457521527
     */
    public void setPageNo(int i, CalendarBean selectBean) {
        Log.e("yjbo----", "初始化==点击事件==");
        mSelectBean = selectBean;
        Log.e("==yjbo==", "当前的选中时间==" + mSelectBean.toString());
        if (i == -1) {//上一页
            isClick = true;
            currentSelectPage = getCurrentItem() - 1;
            setCurrentItem(getCurrentItem() - 1);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
            updateData();
//                }
//            }, 2 * 1000);
        } else if (i == 0) {//当前页
//            setCurrentItem(getCurrentItem());
            currentSelectPage = getCurrentItem();
            updateData();
        } else if (i == 1) {//下一页
            isClick = true;
            currentSelectPage = getCurrentItem() + 1;
            setCurrentItem(getCurrentItem() + 1);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
            updateData();
//                }
//            }, 2 * 1000);
        }
        if (mCaledarLayoutChangeListener != null)
            mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
    }

//    @Override
//    public void setCurrentItem(int item) {
//        super.setCurrentItem(item);
//        if (mCaledarLayoutChangeListener != null)
//            mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
//    }
}
