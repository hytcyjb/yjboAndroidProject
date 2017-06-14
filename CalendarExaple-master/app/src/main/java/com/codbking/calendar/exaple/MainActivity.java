package com.codbking.calendar.exaple;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 原作者：https://github.com/codbking/CalendarExaple
 * @author yjbo
 * @time 2017/6/14 10:25
 * @qq 1457521527
*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.text1, R.id.text2, R.id.text3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text1:
                startActivity(new Intent(MainActivity.this,XiaomiActivity.class));
                break;
            case R.id.text2:
                startActivity(new Intent(MainActivity.this,DingdingActivity.class));
                break;
            case R.id.text3:
                startActivity(new Intent(MainActivity.this,MonthActivity.class));
                break;
        }
    }
}
