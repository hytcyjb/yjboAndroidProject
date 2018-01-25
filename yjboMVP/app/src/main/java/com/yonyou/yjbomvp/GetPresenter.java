package com.yonyou.yjbomvp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yjbo on 17/9/29.
 */

public class GetPresenter implements Contract.Presenter {
    private Contract.view mContractView;

    public GetPresenter(Contract.view contractView) {
        mContractView = contractView;
        mContractView.setPresenter(this);
    }

    @Override
    public void start(String str) {

        Log.e("yjbo测试--", "GetPresenter类： start: ==");
    }

    @Override
    public void addText(Context mContext, String str) {
        Log.e("yjbo测试--", "GetPresenter类： addText: ==");
        mContractView.showUpdateView(str);
        Toast.makeText(mContext, "你好你好", Toast.LENGTH_SHORT).show();
    }

}
