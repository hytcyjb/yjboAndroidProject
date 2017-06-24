package yjbo.yy.yjbodownmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import yjbo.yy.yjbodownmanager.adapter.MutipleAdaper;

/**
 * 写一个文件下载器
 *
 * @author yjbo
 * @time 2017/6/24 16:24
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MutipleAdaper mMutipleAdaper;
    private ArrayList<Item> Datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        Datas = new ArrayList<>();

        Datas.add(new Item("美丽加", 1, "http://pic3.apk8.com/small2/14325422596306671.png",
                "http://001file.liqucn.com/upload/2014/shenghuo/cn.mljia.o2o_2.5.3_liqucn.com.apk"));
        Datas.add(new Item("果然方便", 1, "http://pic3.apk8.com/small2/14313175771828369.png",
                "http://001file.liqucn.com/upload/2017/313/b/com.hurong.grfb_1.3_liqucn.com.apk"));
        Datas.add(new Item("薄荷", 1, "http://pic3.apk8.com/small2/14308183888151824.png",
                "http://001file.liqucn.com/upload/2013/jiankang/com.boohee.one_5.8.0.2_liqucn.com.apk"));
        Datas.add(new Item("GG助手", 1, "http://pic3.apk8.com/small2/14302008166714263.png",
                "http://001file.liqucn.com/upload/2012/wangluo/com.muzhiwan.installer_2.2.0_liqucn.com.apk"));
        Datas.add(new Item("红包惠锁屏", 1, "http://pic3.apk8.com/small2/14307106593913848.png",
                "http://001file.liqucn.com/upload/2017/313/s/com.huaqian_4.3.7.1_liqucn.com.apk"));
        Datas.add(new Item("快的打车", 1, "http://up.apk8.com/small1/1439955061264.png",
                "http://001file.liqucn.com/upload/2013/jiaotong/com.funcity.taxi.passenger_4.4_liqucn.com.apk"));
        Datas.add(new Item("叮当快药", 1, "http://pic3.apk8.com/small2/14315954626414886.png",
                "http://001file.liqucn.com/upload/2015/jiankang/com.ddsy.songyao_4.6.1_liqucn.com.apk"));
        Datas.add(new Item("悦跑圈", 1, "http://pic3.apk8.com/small2/14298490191525146.jpg",
                "http://001file.liqucn.com/upload/2016/jiankang/co.runner.app_2.7.0_liqucn.com.apk"));
        Datas.add(new Item("悠悠导航", 1, "http://pic3.apk8.com/small2/14152456988840667.png",
                "http://001file.liqucn.com/upload/2014/shenghuo/1437382880com.suc13.youyouquan_3.apk"));
        Datas.add(new Item("虎牙直播", 1, "http://up.apk8.com/small1/1439892235841.jpg",
                "http://001file.liqucn.com/upload/2017/286/i/com.duowan.kiwi_4.7.3_liqucn.com.apk"));


        mMutipleAdaper = new MutipleAdaper(this, Datas);
        recyclerView.setAdapter(mMutipleAdaper);


    }
}
