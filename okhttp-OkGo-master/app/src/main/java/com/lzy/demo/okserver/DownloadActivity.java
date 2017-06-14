package com.lzy.demo.okserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.demo.R;
import com.lzy.demo.base.BaseActivity;
import com.lzy.demo.base.BaseRecyclerAdapter;
import com.lzy.demo.base.DividerItemDecoration;
import com.lzy.demo.model.ApkModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.targetFolder) TextView targetFolder;
    @Bind(R.id.tvCorePoolSize) TextView tvCorePoolSize;
    @Bind(R.id.sbCorePoolSize) SeekBar sbCorePoolSize;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.openManager) Button openManager;

    private ArrayList<ApkModel> apks;
    private DownloadManager downloadManager;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initToolBar(toolbar, true, "下载管理");

        initData();
        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");

        targetFolder.setText("下载路径: " + downloadManager.getTargetFolder());
        sbCorePoolSize.setMax(5);
        sbCorePoolSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                downloadManager.getThreadPool().setCorePoolSize(progress);
                tvCorePoolSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbCorePoolSize.setProgress(3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new MainAdapter(this);
        recyclerView.setAdapter(adapter);
        openManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DownloadManagerActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class MainAdapter extends BaseRecyclerAdapter<ApkModel, ViewHolder> {

        public MainAdapter(Context context) {
            super(context, apks);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_download_details, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ApkModel apkModel = mDatas.get(position);
            holder.bind(apkModel);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.name) TextView name;
        @Bind(R.id.icon) ImageView icon;
        @Bind(R.id.download) Button download;

        private ApkModel apkModel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ApkModel apkModel) {
            this.apkModel = apkModel;
            if (downloadManager.getDownloadInfo(apkModel.getUrl()) != null) {
                download.setText("已在队列");
                download.setEnabled(false);
            } else {
                download.setText("下载");
                download.setEnabled(true);
            }
            name.setText(apkModel.getName());
            Glide.with(getApplicationContext()).load(apkModel.getIconUrl()).error(R.mipmap.ic_launcher).into(icon);
            download.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.download) {
                if (downloadManager.getDownloadInfo(apkModel.getUrl()) != null) {
                    Toast.makeText(getApplicationContext(), "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                } else {
                    GetRequest request = OkGo.get(apkModel.getUrl())//
                            .headers("headerKey1", "headerValue1")//
                            .headers("headerKey2", "headerValue2")//
                            .params("paramKey1", "paramValue1")//
                            .params("paramKey2", "paramValue2");
                    downloadManager.addTask(apkModel.getUrl(), apkModel, request, null);
                    download.setText("已在队列");
                    download.setEnabled(false);
                }
            } else {
                Intent intent = new Intent(getApplicationContext(), DesActivity.class);
                intent.putExtra("apk", apkModel);
                startActivity(intent);
            }
        }
    }

    private void initData() {
        apks = new ArrayList<>();
        ApkModel apkInfo1 = new ApkModel();
        apkInfo1.setName("美丽加");
        apkInfo1.setIconUrl("http://pic3.apk8.com/small2/14325422596306671.png");
        apkInfo1.setUrl("http://001file.liqucn.com/upload/2014/shenghuo/cn.mljia.o2o_2.5.3_liqucn.com.apk");
        apks.add(apkInfo1);
        ApkModel apkInfo2 = new ApkModel();
        apkInfo2.setName("果然方便");
        apkInfo2.setIconUrl("http://pic3.apk8.com/small2/14313175771828369.png");
        apkInfo2.setUrl("http://001file.liqucn.com/upload/2017/313/b/com.hurong.grfb_1.3_liqucn.com.apk");
        apks.add(apkInfo2);
        ApkModel apkInfo3 = new ApkModel();
        apkInfo3.setName("薄荷");
        apkInfo3.setIconUrl("http://pic3.apk8.com/small2/14308183888151824.png");
        apkInfo3.setUrl("http://001file.liqucn.com/upload/2013/jiankang/com.boohee.one_5.8.0.2_liqucn.com.apk");
        apks.add(apkInfo3);
        ApkModel apkInfo4 = new ApkModel();
        apkInfo4.setName("GG助手");
        apkInfo4.setIconUrl("http://pic3.apk8.com/small2/14302008166714263.png");
        apkInfo4.setUrl("http://001file.liqucn.com/upload/2012/wangluo/com.muzhiwan.installer_2.2.0_liqucn.com.apk");
        apks.add(apkInfo4);
        ApkModel apkInfo5 = new ApkModel();
        apkInfo5.setName("红包惠锁屏");
        apkInfo5.setIconUrl("http://pic3.apk8.com/small2/14307106593913848.png");
        apkInfo5.setUrl("http://001file.liqucn.com/upload/2017/313/s/com.huaqian_4.3.7.1_liqucn.com.apk");
        apks.add(apkInfo5);
        ApkModel apkInfo6 = new ApkModel();
        apkInfo6.setName("快的打车");
        apkInfo6.setIconUrl("http://up.apk8.com/small1/1439955061264.png");
        apkInfo6.setUrl("http://001file.liqucn.com/upload/2013/jiaotong/com.funcity.taxi.passenger_4.4_liqucn.com.apk");
        apks.add(apkInfo6);
        ApkModel apkInfo7 = new ApkModel();
        apkInfo7.setName("叮当快药");
        apkInfo7.setIconUrl("http://pic3.apk8.com/small2/14315954626414886.png");
        apkInfo7.setUrl("http://001file.liqucn.com/upload/2015/jiankang/com.ddsy.songyao_4.6.1_liqucn.com.apk");
        apks.add(apkInfo7);
        ApkModel apkInfo8 = new ApkModel();
        apkInfo8.setName("悦跑圈");
        apkInfo8.setIconUrl("http://pic3.apk8.com/small2/14298490191525146.jpg");
        apkInfo8.setUrl("http://001file.liqucn.com/upload/2016/jiankang/co.runner.app_2.7.0_liqucn.com.apk");
        apks.add(apkInfo8);
        ApkModel apkInfo9 = new ApkModel();
        apkInfo9.setName("悠悠导航");
        apkInfo9.setIconUrl("http://pic3.apk8.com/small2/14152456988840667.png");
        apkInfo9.setUrl("http://001file.liqucn.com/upload/2014/shenghuo/1437382880com.suc13.youyouquan_3.apk");
        apks.add(apkInfo9);
        ApkModel apkInfo10 = new ApkModel();
        apkInfo10.setName("虎牙直播");
        apkInfo10.setIconUrl("http://up.apk8.com/small1/1439892235841.jpg");
        apkInfo10.setUrl("http://001file.liqucn.com/upload/2017/286/i/com.duowan.kiwi_4.7.3_liqucn.com.apk");
        apks.add(apkInfo10);
    }
}