package com.yonyou.yjbomvp;

import android.content.Context;

/**
 * Created by yjbo on 17/9/29.
 */

public interface Contract {
    interface view extends BaseView<Presenter> {
        void showUpdateView(String str);
    }

    interface Presenter extends BasePresenter {
        void addText(Context mContext, String str);
    }
}
