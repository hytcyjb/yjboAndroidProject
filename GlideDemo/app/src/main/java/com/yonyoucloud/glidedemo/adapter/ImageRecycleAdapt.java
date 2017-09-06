package com.yonyoucloud.glidedemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yonyoucloud.glidedemo.R;
import com.yonyoucloud.glidedemo.util.GlideUtil;


/**
 * 适配器
 * Created by yjbo on 17/9/4.
 */

public class ImageRecycleAdapt extends RecyclerView.Adapter<ImageRecycleAdapt.ViewHolder> {
    private String[] datas = null;
    private Context mcontext;

    public ImageRecycleAdapt(Context context, String[] datas) {
        this.datas = datas;
        this.mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_list, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        GlideUtil.showImage(mcontext,viewHolder.mImage, datas[position]);
    }

    @Override
    public int getItemCount() {
        return datas.length;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;

        private ViewHolder(View view) {
            super(view);
            mImage = (ImageView) view.findViewById(R.id.image);
        }
    }
}
