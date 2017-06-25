package yjbo.yy.yjbodownmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import yjbo.yy.yjbodownmanager.adapter.MutipleAdaper;
import yjbo.yy.yjbodownmanager.adapter.RecyclerViewHolder;
import yjbo.yy.yjbodownmanager.downmanager.DownloadManager;
import yjbo.yy.yjbodownmanager.downmanager.DownloadService;
import yjbo.yy.yjbodownmanager.downmanager.db.DownloadInfo;
import yjbo.yy.yjbodownmanager.downmanager.listener.DownloadListener;
import yjbo.yy.yjbodownmanager.downmanager.request.GetRequest;

/**
 * 写一个文件下载器
 * 模仿：https://github.com/jeasonlzy/okhttp-OkGo
 *
 * @author yjbo
 * @time 2017/6/24 16:24
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MutipleAdaper mMutipleAdaper;
    private ArrayList<Item> Datas;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        downloadManager = DownloadService.getDownloadManager(this);
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");
        downloadManager.getThreadPool().setCorePoolSize(5);
//
        Datas = new ArrayList<>();

        Datas.add(new Item("美丽加", 1, "http://pic3.apk8.com/small2/14325422596306671.png",
                "http://001file.liqucn.com/upload/2014/shenghuo/cn.mljia.o2o_2.5.3_liqucn.com.apk"));
        Datas.add(new Item("暴风影音", 1, "http://up.apk8.com/small1/1494232079296.png",
                "http://gdown.baidu.com/data/wisegame/07d3b11e1957231f/baofengyingyin_720100.apk"));
        Datas.add(new Item("为什么要开展一小课？", 1, "http://video.89mc.com/89mc/knowledge/img/0ee807e6-264f-490b-9cdd-51ae6dbea740.jpg",
                "http://video.89mc.com/89mc/knowledge/video/f48bd536-0018-42a7-a8d7-1096bfefb51b.mp4"));
        Datas.add(new Item("沟通中如何学会聆听？ ——掌握“四字真言", 1, "http://video.89mc.com/89mc/knowledge/img/918e7e8b-a08a-4aa2-b26b-966d85ef4734.jpg",
                "http://video.89mc.com/89mc/knowledge/video/c3363f74-068a-4fcb-8373-f28edf395da9.mp4"));
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


        mMutipleAdaper = new MutipleAdaper(this, Datas) {
            @Override
            protected void onItemClick(RecyclerViewHolder holder, Item item, int position) {
                Toast.makeText(mContext, item.getTv1() + "----" + position, Toast.LENGTH_SHORT).show();
                TextView textView = holder.getView(R.id.show_change);
                if ("已在队列".equals(textView.getText() + "")) {
                    Toast.makeText(getApplicationContext(), "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                } else {
                    if (downloadManager.getDownloadInfo(item.getDownUrl()) != null) {
                        Toast.makeText(getApplicationContext(), "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                    } else {

                        GetRequest request = OkGo.get(item.getDownUrl())//
                                .headers("headerKey1", "headerValue1")//
                                .headers("headerKey2", "headerValue2")//
                                .params("paramKey1", "paramValue1")//
                                .params("paramKey2", "paramValue2");

                        ApkModel apkModel = new ApkModel(item.getTv1(), item.getDownUrl(), item.getIconUrl());
                        MyDownloadListener myDownloadListener = new MyDownloadListener();
                        myDownloadListener.setUserTag(holder);

                        downloadManager.addTask(item.getDownUrl(), apkModel, request, myDownloadListener);


                        holder.setText(R.id.show_change, "已在队列");
                    }
                }
            }

            @Override
            protected void showStatus(RecyclerViewHolder holder, Item item, int position) {
                if (downloadManager.getDownloadInfo(item.getDownUrl()) != null) {
                    holder.setText(R.id.show_change, "已在队列");
                } else {
                    holder.setText(R.id.show_change, "下载");
                }
            }
        };
        recyclerView.setAdapter(mMutipleAdaper);

        findViewById(R.id.open_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DownloadManagerActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //直接下载全部未下载的内容
        downloadManager.startAllTask();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMutipleAdaper.notifyDataSetChanged();
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            Log.e("yjbo", "====="+downloadInfo.getFileName() + "=====" + downloadInfo.getProgress());
            if (downloadInfo.getProgress() == 1.0){
                DownloadListener listener = downloadInfo.getListener();
                RecyclerViewHolder holder = (RecyclerViewHolder) listener.getUserTag();
                holder.setText(R.id.show_change,"下载完成");
            }else {
                DownloadListener listener = downloadInfo.getListener();
                RecyclerViewHolder holder = (RecyclerViewHolder) listener.getUserTag();
                holder.setText(R.id.show_change,"正在下载中"+downloadInfo.getProgress());
            }
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
        }
    }
}
