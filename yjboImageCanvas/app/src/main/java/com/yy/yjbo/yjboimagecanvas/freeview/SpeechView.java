package com.yy.yjbo.yjboimagecanvas.freeview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SpeechView extends LinearLayout {

    private TextView mTitle;

    private FFEditText mDialogue;

    public SpeechView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
    }

    public void addViewSp(Context context, String title, String words) {

        mTitle = new FFTextView(context);
        mTitle.setText(title);
        mTitle.setTag(title);

        addView(mTitle);

        mDialogue = new FFEditText(context);
        mDialogue.setText(words);
        addView(mDialogue);
    }

    public void addViewSp(Context context, FreeItemviewVo freeviewVo) {
        if ("string".equals(freeviewVo.getItemtype())) {
            mTitle = new FFTextView(context);
            mTitle.setText(freeviewVo.getItemname());
            mTitle.setTag(freeviewVo.getItemkey());
            addView(mTitle);
        } else if ("string2".equals(freeviewVo.getItemtype())) {
            mDialogue = new FFEditText(context);
            mDialogue.setText(freeviewVo.getItemname());
            mDialogue.setTag(freeviewVo.getItemkey());
            addView(mDialogue);
        }
    }
    public void addViewSp(Context context, List<FreeItemviewVo> freeviewList) {
        for (int i = 0; i < freeviewList.size(); i++) {
            FreeItemviewVo freeviewVo = new FreeItemviewVo();
            if ("string".equals(freeviewVo.getItemtype())) {
                mTitle = new FFTextView(context);
                mTitle.setText(freeviewVo.getItemname());
                mTitle.setTag(freeviewVo.getItemkey());
                addView(mTitle);
            } else if ("string2".equals(freeviewVo.getItemtype())) {
                mDialogue = new FFEditText(context);
                mDialogue.setText(freeviewVo.getItemname());
                mDialogue.setTag(freeviewVo.getItemkey());
                addView(mDialogue);
            }
        }

    }
    /**
     * Convenience method to set the title of a SpeechView
     */

    public void setTitle(String title) {

        mTitle.setText(title);

    }

    /**
     * Convenience method to set the dialogue of a SpeechView
     */

    public void setDialogue(String words) {

        mDialogue.setText(words);
    }

    /**
     *
     * @return 获取所有的view
     */
//    public View getCCView() {
//        return this.getChildAt();
//    }

}