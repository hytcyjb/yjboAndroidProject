package com.yy.yjbo.yjboimagecanvas.freeview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.yjbo.yjboimagecanvas.freeview.bean.FreeHeadItemVo;
import com.yy.yjbo.yjboimagecanvas.freeview.bean.FreeItemviewVo;

import java.util.List;

public class SpeechView extends LinearLayout {

    private LinearLayout mTitle;

    private FFEditText mDialogue;

    public SpeechView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
    }

    public void addViewSp(Context context, String title, String words) {

        mTitle = new FFTextView(context,new FreeItemviewVo(title,words,"string"));
//        mTitle.setText(title);
//        mTitle.setTag(title);

        addView(mTitle);

        mDialogue = new FFEditText(context);
        mDialogue.setText(words);
        addView(mDialogue);
    }

    public void addViewSp(Context context, FreeItemviewVo freeviewVo) {
        if ("string".equals(freeviewVo.getItemtype())) {
            mTitle = new FFTextView(context,freeviewVo);
//            mTitle.setText(freeviewVo.getItemname());
//            mTitle.setTag(freeviewVo.getItemkey());
            addView(mTitle);
        } else if ("string2".equals(freeviewVo.getItemtype())) {
            mDialogue = new FFEditText(context);
            mDialogue.setText(freeviewVo.getItemname());
            mDialogue.setTag(freeviewVo.getItemkey());
            addView(mDialogue);
        }
    }



    public void addViewHead(Context context, List<FreeHeadItemVo> freeHeadList) {
        for (int i = 0; i < freeHeadList.size(); i++) {
            Log.e("yjbo测试--", "SpeechView类： addViewHead: =="+i);
            FreeHeadItemVo freeHeadItemVo = freeHeadList.get(i);
            mDialogue = new FFEditText(context);
            mDialogue.setText(freeHeadItemVo.getHeadernam());
            mDialogue.setTag(freeHeadItemVo.getHeadernam());
            addView(mDialogue);
            List<FreeItemviewVo> itemlist = freeHeadItemVo.getItemlist();
            if (itemlist != null && itemlist.size() >0){
                addViewBody(context,itemlist);
            }
        }
    }

    public void addViewBody(Context context, List<FreeItemviewVo> freeviewList) {
        for (int i = 0; i < freeviewList.size(); i++) {
            Log.e("yjbo测试--", "SpeechView类： addViewBody: =="+i);
            FreeItemviewVo freeviewVo = freeviewList.get(i);
            if ("string".equals(freeviewVo.getItemtype())) {
                mTitle = new FFTextView(context,freeviewVo);
                addView(mTitle);
            } else if ("string2".equals(freeviewVo.getItemtype())) {
                mDialogue = new FFEditText(context);
                mDialogue.setText(freeviewVo.getItemname());
                mDialogue.setTag(freeviewVo.getItemkey());
                addView(mDialogue);
            }
        }
    }

}