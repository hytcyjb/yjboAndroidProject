package com.codbking.calendar.exaple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zcw.widget.MonthAdapter;
import com.zcw.widget.MonthView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 参考：https://github.com/zcweng/MonthView
 *
 * @author yjbo
 * @time 2017/6/14 14:58
 * @qq 1457521527
 */
public class MonthActivity extends AppCompatActivity {

    @BindView(R.id.monthView1)
    MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);
        ButterKnife.bind(this);

        monthView.setAdapter(new MonthAdapter() {
            @Override
            public View createCellView(ViewGroup viewGroup, int position) {
                TextView textView  = new TextView(MonthActivity.this);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setGravity(Gravity.CENTER);
                return textView;
            }
            @Override
            public void bindCellView(ViewGroup viewGroup, View child, int position, Calendar calendar) {
                TextView textView  = (TextView) child;
                textView.setText(""+calendar.get(Calendar.DAY_OF_MONTH));
            }
        });

    }
}
