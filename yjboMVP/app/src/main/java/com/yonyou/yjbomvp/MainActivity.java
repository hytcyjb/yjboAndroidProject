package com.yonyou.yjbomvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * mvp模式的demo
 *
 * @aouto yjbo
 * @time 17/9/29 上午8:54
 */
public class MainActivity extends AppCompatActivity implements Contract.view {

    private TextView showTxt;
    private Contract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showTxt = (TextView) findViewById(R.id.show_txt);
        new GetPresenter(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addText(MainActivity.this,"你好");
            }
        });
    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUpdateView(String str) {
        showTxt.setText("MVP来了"+str);

    }
}
