package yjbo.yy.yjbodownmanager.adapter;

/** 
 * 多种类目状态时用到的
 * @author yjbo
 * @time 2017/4/1 23:50
 */

public interface MutipleTypeSupport<T> {
    //根据当前条目获取布局
    int getLayoutId(T t);
}