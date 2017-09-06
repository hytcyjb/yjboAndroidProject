package com.yy.yjbo.yjboimagecanvas.horizonlist;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.yy.yjbo.yjboimagecanvas.R;

public class HorMainActivity extends Activity {
        HorizontalListView hListView;
        HorizontalListViewAdapter hListViewAdapter;
        ImageView previewImg;
        View olderSelectView = null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_hor);
            initUI();
        }

        public void initUI(){
            hListView = (HorizontalListView)findViewById(R.id.horizon_listview);
            previewImg = (ImageView)findViewById(R.id.image_preview);
            String[] titles = {"怀师", "南怀瑾军校", "闭关", "南怀瑾", "南公庄严照","怀师", "南怀瑾军校", "闭关", "南怀瑾", "南公庄严照", "怀师法相"};
            final int[] ids = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher};
            hListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(),titles,ids);
            hListView.setAdapter(hListViewAdapter);
            hListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    previewImg.setImageResource(ids[position]);
                    hListViewAdapter.setSelectIndex(position);
                    hListViewAdapter.notifyDataSetChanged();

                }
            });

        }

    }