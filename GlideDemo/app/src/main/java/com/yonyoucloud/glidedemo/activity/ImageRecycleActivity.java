package com.yonyoucloud.glidedemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.glidedemo.adapter.ImageRecycleAdapt;

/**
 * 图片列表展示
 *
 * @aouto yjbo
 * @time 17/9/4 上午11:13
 */
public class ImageRecycleActivity extends AppCompatActivity {

    private String[] stringArray = new String[]{
            "https://cdn.pixabay.com/photo/2013/04/03/12/05/tree-99852__480.jpg",
            "https://cdn.pixabay.com/photo/2015/11/12/17/07/tree-stump-1040639__480.jpg",
            "https://cdn.pixabay.com/photo/2015/11/19/20/40/leaves-1051937__480.jpg",
            "https://cdn.pixabay.com/photo/2013/10/17/22/45/nilgans-197240__480.jpg",
            "https://cdn.pixabay.com/photo/2014/08/20/15/53/maple-leaf-422582__480.jpg",
            "https://cdn.pixabay.com/photo/2015/02/13/14/32/apples-635239__480.jpg",
            "https://cdn.pixabay.com/photo/2013/11/09/16/33/autumn-207854__480.jpg",
            "https://cdn.pixabay.com/photo/2014/09/29/13/06/anemone-466088__480.jpg",
            "https://cdn.pixabay.com/photo/2016/10/06/14/07/sunflowers-1719119__480.jpg",
            "https://cdn.pixabay.com/photo/2016/09/27/13/09/tee-1698297__480.jpg",
            "https://cdn.pixabay.com/photo/2012/11/11/23/36/pine-65769__480.jpg",
            "https://cdn.pixabay.com/photo/2013/07/13/12/47/autumn-160322__480.png",
            "https://cdn.pixabay.com/photo/2015/03/26/09/45/maple-leaves-690233__480.jpg",
            "https://cdn.pixabay.com/photo/2015/02/13/14/32/apple-pie-635241__480.jpg",
            "https://cdn.pixabay.com/photo/2012/02/22/09/06/conkers-15186__480.jpg",
            "https://cdn.pixabay.com/photo/2015/03/06/13/36/apple-661670__480.jpg",
            "https://cdn.pixabay.com/photo/2013/07/18/10/56/domino-163523__480.jpg",
            "https://cdn.pixabay.com/photo/2015/03/13/04/56/helocasting-671186__480.jpg",
            "https://cdn.pixabay.com/photo/2014/09/10/22/35/pumpkin-441203__480.jpg",
            "https://cdn.pixabay.com/photo/2014/02/27/16/08/horse-chestnut-tree-275921__480.jpg",
            "https://cdn.pixabay.com/photo/2014/12/08/02/59/bench-560435__480.jpg",
            "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823__480.jpg",
    };

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
//        String[] stringArray = getResources().getStringArray(R.array.pic_arr);
        String tt = "";
        for (int i = 0; i < stringArray.length; i++) {
            tt += "\"" + stringArray[i] + "\",\n";
        }
        Log.e("yjbo测试--", "ImageRecycleActivity类： onCreate: ==" + tt);
        ImageRecycleAdapt mAdapter = new ImageRecycleAdapt(ImageRecycleActivity.this, stringArray);
        mRecyclerView.setAdapter(mAdapter);
    }

}
