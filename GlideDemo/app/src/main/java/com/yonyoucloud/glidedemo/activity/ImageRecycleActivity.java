package com.yonyoucloud.glidedemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.glidedemo.adapter.ImageRecycleAdapt;

/**
 * 图片列表展示
 *
 * @aouto yjbo
 * @time 17/9/4 上午11:13
 */
public class ImageRecycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recycle);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(mRecyclerView.getContext(), 2, GridLayoutManager.VERTICAL, false));

        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        String[] stringArray = getResources().getStringArray(R.array.pic_arr);
        ImageRecycleAdapt mAdapter = new ImageRecycleAdapt(ImageRecycleActivity.this, stringArray);
        mRecyclerView.setAdapter(mAdapter);
    }

}
