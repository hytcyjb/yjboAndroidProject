package yjbo.yy.yjbodownmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import yjbo.yy.yjbodownmanager.Item;
import yjbo.yy.yjbodownmanager.R;


/**
 * 多样式的item使用的adapter
 *
 * @author yjbo
 * @time 2017/4/2 15:23
 */
public class MutipleAdaper extends RecyclerMoreKindViewAdapter<Item> {

    public MutipleAdaper(Context context, List<Item> datas) {

        super(context, datas, new MutipleTypeSupport<Item>() {
            @Override
            public int getLayoutId(Item item) {
                if (item.getType() == 1) {//该处1是通过 item 传过来的
                    return R.layout.list_item;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * 加载更多的时候用到的
     *
     * @author yjbo  @time 2017/4/6 11:50
     */
    public void addMore(List<Item> datas, int add) {

        RecyclerAddMoreKindViewAdapter(datas, new MutipleTypeSupport<Item>() {
            @Override
            public int getLayoutId(Item item) {
                if (item.getType() == 1) {//该处1是通过 item 传过来的
                    return R.layout.list_item;
                } else {
                    return -1;
                }
            }
        }, 1);
    }

    public void refreshOne(List<Item> datas, int position) {
        Item item1 = mDatas.get(position);
        item1.setTv1("yjbo在操作");
        mDatas.remove(position);
        mDatas.add(position, item1);
        notifyItemChanged(position);
    }

    @Override
    protected void bindData(final RecyclerViewHolder holder, final Item item, final int position, final List<Item> mDatas) {
        if (item.getType() == 1) {
            ImageView img = holder.getView(R.id.img);
            Glide.with(mContext)
                    .load(item.getIconUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(img);

            holder.setText(R.id.tv1, item.getTv1())
                    .setText(R.id.tv2, item.getTv2())
                    .setOnClickListener(R.id.liner_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(mContext, item.getTv1() + "----" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}