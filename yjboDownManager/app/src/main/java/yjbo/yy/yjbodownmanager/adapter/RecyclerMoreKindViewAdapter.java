package yjbo.yy.yjbodownmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

public abstract class RecyclerMoreKindViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected int mLayoutId;//布局id
    protected List<T> mDatas;//数据源
    protected Context mContext;//上下文
    private LayoutInflater mInflater;


    //多种类型的界面中使用的
    private MutipleTypeSupport<T> mMutipleTypeSupport;

    public RecyclerMoreKindViewAdapter(Context context, List<T> datas, MutipleTypeSupport typeSupport) {
        this(context, -1, datas);
        this.mMutipleTypeSupport = typeSupport;
    }


    public RecyclerMoreKindViewAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mDatas = datas;
        this.mInflater = LayoutInflater.from(mContext);
    }
    //上拉加载更多1-0
    public void RecyclerAddMoreKindViewAdapter(List<T> datas, MutipleTypeSupport typeSupport,int add) {
        this.mMutipleTypeSupport = typeSupport;
        this.mLayoutId = -1;
        this.mDatas.addAll(datas);

        notifyDataSetChanged();
    }

    //调用 onCreateViewHolder() 方法之前调用 getItemViewType()
    @Override
    public int getItemViewType(int position) {
        if (mMutipleTypeSupport != null) {
            return mMutipleTypeSupport.getLayoutId(mDatas.get(position));
        }
        return super.getItemViewType(position);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断是否需要多布局
        if (mMutipleTypeSupport != null) {
            mLayoutId = viewType;
        }

        View itemView = mInflater.inflate(mLayoutId, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        bindData(holder, mDatas.get(position), position,mDatas);
    }


    /**
     * 把必要参数传进去，让每个 Adapter 去设置具体值
     *
     * @param holder   RecyclerViewHolder
     * @param t        数据
     * @param position 当前位置
     */
    protected abstract void bindData(RecyclerViewHolder holder, T t, int position,List<T> mDatas);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}