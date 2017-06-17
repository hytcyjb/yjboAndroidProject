package com.codbking.calendar.exaple;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CaledarOnItemListener;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class XiaomiActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView mTitle;
    //    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;
    @BindView(R.id.list)
    ListView mList;
    CalendarBean mSelectBean;
    View topView;
    Handler mHandler = new Handler();
    int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaomi);
        ButterKnife.bind(this);
        initView();
        initList();
    }

    private void initList() {
        mList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(XiaomiActivity.this).inflate(android.R.layout.simple_list_item_1, null);
                }

                TextView textView = (TextView) convertView;
                textView.setText("position:" + position);

                return convertView;
            }
        });
//        setListViewHeightBasedOnChildren(mList);


        mList.addHeaderView(topView);

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void initView() {
        topView = LayoutInflater.from(XiaomiActivity.this).inflate(R.layout.item_top, null);
        mCalendarDateView = (CalendarDateView) topView.findViewById(R.id.calendarDateView);
        final CaledarAdapter mCaledarAda = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_xiaomi, null);
                }

                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);

                text.setText("" + bean.day);
                if (bean.mothFlag != 0 || bean.week == 1 || bean.week == 7) {
                    text.setTextColor(0xffC7C7C7);
                    chinaText.setTextColor(0xffC7C7C7);
                } else {
                    text.setTextColor(0xff444444);
                    chinaText.setTextColor(0xff444444);
                }
                if (mSelectBean != null) {
                    if (bean.day == mSelectBean.day && bean.moth == mSelectBean.moth) {
                        convertView.setSelected(true);
                    } else {
                        convertView.setSelected(false);
                    }
                }
                Log.e("===yjbo==", "您到我这里了=000===");
//                Log.e("yjbo====00", "当前点击了=6===" + bean.year + "===" + bean.moth + "===" + bean.day);
                if (bean.year == 2017 && bean.moth == 6 && bean.day == 12 || bean.year == 2018 && bean.moth == 6 && bean.day == 12) {
                    chinaText.setText("@");
                } else {
                    chinaText.setText("");
                }
                if (bean.mothFlag == 0) {
                    if (bean.day == flag && (bean.moth == 6 ||  bean.moth == 10)) {
                        chinaText.setText("c");
                        Toast.makeText(XiaomiActivity.this, "====" + flag, Toast.LENGTH_SHORT).show();
                    }
                }
                return convertView;
            }

        };
        flag = 5;
        mCalendarDateView.setAdapter(mCaledarAda);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                flag = 13;
////                mCalendarDateView.getAdapter().getItemPosition(null);
////                mCalendarDateView.setAdapter(mCaledarAda);
//                Toast.makeText(XiaomiActivity.this, "====", Toast.LENGTH_SHORT).show();
//                mCalendarDateView.updateData();
//            }
//        }, 6 * 1000);
        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                mSelectBean = bean;
//                mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day + "===" + bean.mothFlag);
                if (bean.mothFlag == -1) {
                    mCalendarDateView.setPageNo(bean.mothFlag, bean);
                } else if (bean.mothFlag == 1) {
                    mCalendarDateView.setPageNo(bean.mothFlag, bean);
                }
            }

            @Override
            public void onItemClickShow(View view, int postion, CalendarBean bean) {
                mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day + "===" + bean.mothFlag);
//                if (bean.mothFlag == -1){
//                    mCalendarDateView.setPageNo(bean.mothFlag);
//                }else if (bean.mothFlag == 1){
//                    mCalendarDateView.setPageNo(bean.mothFlag);
//                }
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        mTitle.setText(data[0] + "/" + data[1] + "/" + data[2]);
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
}
