package com.yy.yjbo.yjboimagecanvas.freeview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yjbo.yjboimagecanvas.R;
import com.yy.yjbo.yjboimagecanvas.freeview.bean.FreeItemviewVo;

/**
 * Created by Administrator on 2017/9/17.
 */

public class FFTextView extends LinearLayout {
    private TextView titleTxt;
    private TextView valueTxt;
    private View view;
    private FreeItemviewVo mfreeItemviewVo;

    public FFTextView(Context context) {
        super(context);
        initData(context);
    }

    public FFTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }


    public FFTextView(Context context, FreeItemviewVo freeItemviewVo) {
        super(context);
        mfreeItemviewVo = freeItemviewVo;
        initData(context);

    }

    private void initData(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.body_item, this);
        titleTxt = (TextView) findViewById(R.id.title_txt);
        valueTxt = (TextView) findViewById(R.id.value_txt);
        if (mfreeItemviewVo != null) {
            titleTxt.setText(mfreeItemviewVo.getItemname());
        }
//        addView(view);
    }

}
