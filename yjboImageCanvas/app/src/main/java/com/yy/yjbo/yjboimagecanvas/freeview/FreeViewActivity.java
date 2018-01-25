package com.yy.yjbo.yjboimagecanvas.freeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yy.yjbo.yjboimagecanvas.R;
import com.yy.yjbo.yjboimagecanvas.freeview.bean.FreeHeadItemVo;
import com.yy.yjbo.yjboimagecanvas.freeview.bean.FreeItemviewVo;
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

        List<FreeHeadItemVo> freeHeadList = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            String data = jsonObject.opt("data") + "";
            String headerlist = new JSONObject(data).opt("headerlist") + "";
//            JSONArray jsonArray = new JSONArray(new JSONObject(new JSONObject(data).opt("headerlist") + "").opt("")) ;
            JSONArray jsonArray = new JSONArray(headerlist);
            for (int k = 0; k < jsonArray.length(); k++) {
                List<FreeItemviewVo> freeList = new ArrayList<>();
                Object o = jsonArray.get(k);
                JSONObject hobj = new JSONObject(o + "");
                String itemlist = hobj.opt("itemlist")+ "";
                String headername = hobj.opt("headername")+ "";
                String headerstyle = hobj.opt("style")+ "";
//                freeList.add(new FreeItemviewVo(headername,"表头："+headername,"string"));
                JSONArray itemlistArray = new JSONArray(itemlist);
//                if (itemlistArray == null ||itemlistArray.length() == 0){
//                    freeHeadList.add(new FreeHeadItemVo(headername,headerstyle));
//                }else {
                    for (int i = 0; i < itemlistArray.length(); i++) {
                        FreeItemviewVo freeItemviewVo = new FreeItemviewVo();
                        Object o1 = itemlistArray.opt(i);
                        JSONObject jsonObject1 = new JSONObject(o1 + "");
                        freeItemviewVo.setItemkey(jsonObject1.opt("itemkey") + "");
                        freeItemviewVo.setItemname(jsonObject1.opt("itemname") + "");
                        freeItemviewVo.setItemtype(jsonObject1.opt("itemtype") + "");
                        freeList.add(freeItemviewVo);
                    }
                    freeHeadList.add(new FreeHeadItemVo(headername,headerstyle,freeList));
//                }
            }
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


        } catch (JSONException e) {
            e.printStackTrace();
        }
//        freeview.addViewBody(FreeViewActivity.this, freeList);
        freeview.addViewHead(FreeViewActivity.this, freeHeadList);
//        FreeItemviewVo freeviewVo = new FreeItemviewVo();
//        freeview.addViewSp(FreeViewActivity.this, freeviewVo);

    }
}
