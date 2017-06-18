package com.codbking.calendar;

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

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.codbking.calendar.CalendarFactory.getMonthOfDayList;

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
                if (currentSelectPage == -1){
                    currentSelectPage = position;
                }
                view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2, mSelectBean,currentSelectPage,position);

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
//                mSelectBean = null;
//                setCurrentItem(position);
                if (currentSelectPage == -1){//第一次的时候
                    currentSelectPage = Integer.MAX_VALUE / 2;
                }//点击的时候在点击那边
//                else {
//                    currentSelectPage = position;
//                }

                Log.e("yjbo----", "初始化==滑动结束==");
                if (1 == 1){
                    return;
                }
//                updateData();
////                //滑页时在顶部显示当前选中时间的日期
                if (onItemClickListener != null) {
                    CalendarView view = views.get(position);
                    //因为有每次viewpage都加载3个页面，这样的话相当于我获取的getSelect中的位置是下个页面的数据
                    final Object[] obs = (Object[]) view.getTag();
//                    final Object[] obs = view.getViewContent();
                    if (obs == null) {
                        return;
                    }
//                    onItemClickListener.onItemClickShow((View) obs[0], (int) obs[1], mSelectBean);
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
                    if (mSelectBean != null) {//getChildAt(mSelectBean.)  mSelectBean.day
                        onItemClickListener.onItemClickShow(null, 0, mSelectBean);
                        Log.e("yjbo----", "初始化==0=="+mSelectBean.toString());
                    } else {
                        onItemClickListener.onItemClickShow((View) obs[0], (int) obs[1], (CalendarBean) obs[2]);
                        Log.e("yjbo----", "初始化==1=="+((CalendarBean) obs[2]).toString());
                    }
//                        }
//                    },1*1000);

                }
                Log.e("===yjbo==", "您到我这里了=onPageSelected===" + position);
//                Toast.makeText(getContext(), "您滑动了..." + position, Toast.LENGTH_SHORT).show();
//                mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
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

        mSelectBean = selectBean;
        Log.e("==yjbo==", "当前的选中时间==" + mSelectBean.toString());
        if (i == -1) {//上一页
            currentSelectPage = getCurrentItem() -1;
            setCurrentItem(getCurrentItem() - 1);
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
            updateData();
//                }
//            }, 2 * 1000);
        } else if (i == 0) {//当前页
//            setCurrentItem(getCurrentItem());
            currentSelectPage = getCurrentItem() ;
            updateData();
        } else if (i == 1) {//下一页
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
