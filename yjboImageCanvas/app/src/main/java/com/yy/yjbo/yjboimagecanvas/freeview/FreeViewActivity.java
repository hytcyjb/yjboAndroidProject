package com.yy.yjbo.yjboimagecanvas.freeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.yy.yjbo.yjboimagecanvas.R;
import com.yy.yjbo.yjboimagecanvas.freeview.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * 自由表单
 * 2017年9月17日17:50:47
 */
public class FreeViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_view);
        testFreeView();

    }

    /**
     * 绘制自由表单
     * 2017年9月16日23:06:58
     */
    private void testFreeView() {
//        WebView webView = new WebView(FreeViewActivity.this);
//        webView.loadUrl("file:///android_asset/freeForm_data");
        SpeechView freeview = (SpeechView) findViewById(R.id.freeview);
        String json = Util.getJson(FreeViewActivity.this, "freeForm_data");
//        freeview.addView(webView);
        List<FreeItemviewVo> freeList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String data = new JSONObject(json).opt("data") + "";
            JSONArray jsonArray = new JSONArray(new JSONObject(new JSONObject(data).opt("headerlist") + "").opt("")) ;
//            JSONArray jsonArray = new JSONArray(headerlist);

//"itemkey": "字段唯一标识",
//"itemname": "客户编码",
//"itemtype": "string",
//"iseditbale": "Y",
//"isrequired": "Y",
//"ismultiselect": "",
//"precision": "",
//"isdisplay": "Y",
//"maxlenth": "",
//"referto": "",
//"editformula": "",
//"style": ""

            for (int i = 0; i < jsonArray.length(); i++) {
                FreeItemviewVo freeItemviewVo = new FreeItemviewVo();
                Object o = jsonArray.get(i);
                JSONObject jsonObject1 = new JSONObject(o + "");
                freeItemviewVo.setItemkey(jsonObject1.opt("itemkey")+"");
                freeItemviewVo.setItemname(jsonObject1.opt("itemname")+"");
                freeItemviewVo.setItemtype(jsonObject1.opt("itemtype")+"");
                freeList.add(freeItemviewVo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        freeview.addViewSp(FreeViewActivity.this,freeList);
//        FreeItemviewVo freeviewVo = new FreeItemviewVo();
//        freeview.addViewSp(FreeViewActivity.this, freeviewVo);

    }
}
