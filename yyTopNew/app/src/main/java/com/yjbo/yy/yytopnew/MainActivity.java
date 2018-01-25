package com.yjbo.yy.yytopnew;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/***
 * 仿今日头条的详情内全部的功能
 * @author  yjbo  2018年1月25日20:56:26
 */
public class MainActivity extends AppCompatActivity {
    private Button submitBtn;
    private EditText inputEdit;
    private TextView texthuluo;
    private String defultStr = "为了增加收入，我家几乎年年要养蚕。一到春天一到春天2121323424324324354636363636363636363546444444，" +
            "母亲就拿出几张粗草纸，铺在一只竹制的筛子里，上面撒上一片片新鲜嫩绿的桑叶，然后小心翼翼地将蚂蚁般幼小的蚕宝宝轻轻放在筛子里。" +
            "几天过去了，蚕宝宝悄无声息地吃光了桑叶。我从竹篮子里抓起一把刚从树上摘下来的桑叶，细心地撒在筛子里，让蚕宝宝爬在桑叶上慢慢享用。" +
            "眼看着蚕宝宝一天天长大，听着蚕食桑叶发出的“沙、沙”声，如同春雨润心田那样舒畅。不到一天，桑叶被蚕食光了，只剩下一根根脉络和一粒粒黑色蚕屎。" +
            "这时，灰白色的蚕完全露了出来，连成一片，在筛子里爬动。";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitBtn = (Button) findViewById(R.id.submit_btn);
        inputEdit = (EditText) findViewById(R.id.input_edit);
        texthuluo = (TextView) findViewById(R.id.texthuluo);

        texthuluo.setEllipsize(TextUtils.TruncateAt.END);
        texthuluo.setMaxLines(3);

        inputEdit.setText(defultStr);
        testTexthuluo();
        initOnclick();
    }

    //测试忽略的样式
    private void testTexthuluo() {
        defultStr =  inputEdit.getText()+"";
        texthuluo.setText(defultStr);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String showTextStr = texthuluo.getLayout().getText() + "";
                if (texthuluo.getLineCount() <3 ){
                    return;
                }else {
                    if (showTextStr != null && showTextStr.indexOf("…") > -1){
                        //说明整整三行
                    }else {
                        //说明虽然有三行，但是没有排满,也不处理
                        return;
                    }
                }

                String replace = showTextStr.replace("\uFEFF", "");
                Log.d("三行之后显示真实的", showTextStr+"====="+replace.length());
                for (int i = replace.length()-30; i < replace.length(); i++) {
                    char c = replace.charAt(i);
                    Log.d("三行之后显示截取的111===", c+"");
                }

                replace = replace.substring(0, replace.length() - 4);
                String temp = replace + "…全部";
                SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                ssb.setSpan(new ForegroundColorSpan(getResources().getColor
                                (R.color.colorPrimary)),
                        temp.length() - 2, temp.length(),    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                texthuluo.setText(ssb);
                Log.d("三行之后显示截取的000===", temp);
            }
        }, 500);
    }

    private void initOnclick() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testTexthuluo();
            }
        });
    }
}
