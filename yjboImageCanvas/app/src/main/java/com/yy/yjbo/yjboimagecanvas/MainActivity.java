package com.yy.yjbo.yjboimagecanvas;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yy.yjbo.yjboimagecanvas.freeview.FreeViewActivity;
import com.yy.yjbo.yjboimagecanvas.freeview.SpeechView;
import com.yy.yjbo.yjboimagecanvas.horizonlist.HorizontalListView;
import com.yy.yjbo.yjboimagecanvas.horizonlist.HorizontalListViewAdapter;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 原作者，https://github.com/hdodenhof/CircleImageView
 * 基于此做矩形的四周圆角加边框的功能；
 *
 * @author yjbo
 * @time 2017/6/1 20:50
 * @qq 1457521527
 */
public class MainActivity extends AppCompatActivity {
    private EditText cfViewTextEdit;
    private TextView mark;
    HorizontalListView hListView;
    HorizontalListViewAdapter hListViewAdapter;
    ImageView previewImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主要的亮点就在于对自定义view的进一步了解，以及对边框添加的掌握；
        // 在以后有能力的情况下，可以进一步的看这种自定义的消耗内容，线程阻塞的情况的发生；
        testFF();
        testMapList();
        testEdit();
        testSplit();
        testSplit2();
        testHorizon();
        testGetLanguage();
        //testDialog();
        testContent();
        testFreeView();
        testTexthuluo();
    }

    //测试忽略的样式
    private void testTexthuluo() {
        final TextView texthuluo = (TextView) findViewById(R.id.texthuluo);
        texthuluo.setText("为了增加收入，我家几乎年年要养蚕。一到春天一到春天2121323424324324354636363636363636363546444444，母亲就拿出几张粗草纸，铺在一只竹制的筛子里，上面撒上一片片新鲜嫩绿的桑叶，然后小心翼翼地将蚂蚁般幼小的蚕宝宝轻轻放在筛子里。几天过去了，蚕宝宝悄无声息地吃光了桑叶。我从竹篮子里抓起一把刚从树上摘下来的桑叶，细心地撒在筛子里，让蚕宝宝爬在桑叶上慢慢享用。眼看着蚕宝宝一天天长大，听着蚕食桑叶发出的“沙、沙”声，如同春雨润心田那样舒畅。不到一天，桑叶被蚕食光了，只剩下一根根脉络和一粒粒黑色蚕屎。这时，灰白色的蚕完全露了出来，连成一片，在筛子里爬动。");
//        textUtil.toggleEllipsize(MainActivity.this,texthuluo,3,texthuluo.getText().toString(),"全部",R.color.colorPrimary,false);
        texthuluo.setEllipsize(TextUtils.TruncateAt.END);
        texthuluo.setMaxLines(3);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String showText = texthuluo.getLayout().getText() + "";
                Log.d("三行之后显示真实的", showText);
//                String substring = showText.substring(0,showText.indexOf("\uFEFF")-5);
                String replace = showText.replace("\uFEFF", "");
                replace = replace.substring(0, replace.length() - 5);
//                String replace = showText.replace("…", "…全部");
                String temp = replace + "…全部";
                SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                ssb.setSpan(new ForegroundColorSpan(getResources().getColor
                                (R.color.colorPrimary)),
                        temp.length() - 2, temp.length(),    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                texthuluo.setText(ssb);
//                texthuluo.setText(replace + "…全部");
                Log.d("三行之后显示截取的000===", replace + "…全部");
//                Log.d("三行之后显示截取的",replace);
            }
        }, 500);
        texthuluo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textStr = texthuluo.getText().toString();
                Log.d("三行之后显示全部222", texthuluo.getLayout().getText() + "");
                texthuluo.setText(textStr);
            }
        });
    }

    /**
     * 绘制自由表单
     * 2017年9月16日23:06:58
     */
    private void testFreeView() {
        Button freeview = (Button) findViewById(R.id.skip_freeview);
        freeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FreeViewActivity.class));
            }
        });
    }

    /**
     * 分割文字，如果
     *
     * @aouto yjbo
     * @time 17/9/11 上午9:50
     */
    private void testContent() {
        TextView content = (TextView) findViewById(R.id.content);
        String contentStr = "ssasaksakhksajksaaaaaaaaabivpsanpsvankvasknsavkvcasvysabusaobsacnpasc;sca;assacksacviuivauii" +
                "bjjkbcsjkbkjbasckjbscajkbsbjbbjlbaslbsaclkcsalklkbscalbasclbjcsajlb";
        content.setText(contentStr);
        setMaxEcplise(content, 2, contentStr);
    }

    /**
     * 参数：maxLines 要限制的最大行数
     * 参数：content  指TextView中要显示的内容
     * 需求：想显示文本的最后2行，并在最前面添加“...”
     */
    public void setMaxEcplise(final TextView mTextView, final int maxLines, final String content) {

        ViewTreeObserver observer = mTextView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                mTextView.setText(content);
                if (mTextView.getLineCount() > maxLines) {
                    int lineEndIndex = mTextView.getLayout().getLineEnd(mTextView.getLineCount() - 1);
                    //下面这句代码中：我在项目中用数字3发现效果不好，改成1了
                    String text = "..." + content.subSequence(3, lineEndIndex);
                    mTextView.setText(text);
                } else {
                    removeGlobalOnLayoutListener(mTextView.getViewTreeObserver(), this);
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void removeGlobalOnLayoutListener(ViewTreeObserver obs, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (obs == null)
            return;
        if (Build.VERSION.SDK_INT < 16) {
            obs.removeGlobalOnLayoutListener(listener);
        } else {
            obs.removeOnGlobalLayoutListener(listener);
        }
    }

    private void testDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_title) + getString(R.string.dialog_title2));//提示如：根据潜在用油总量，建议客户等级为X，是否修改
        builder.setNeutralButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {//右-否--直接提交
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("不知道", null);
        builder.create().show();
    }

    private void testGetLanguage() {
        String country = getResources().getConfiguration().locale.getCountry();
        String country1 = Locale.getDefault().getCountry().toLowerCase();
        Log.e("yjbo测试--", "MainActivity类： testGetLanguage: ==" + country + "===" + country1);

    }

    /**
     * 添加注释：水平滑动
     *
     * @aouto yjbo
     * @time 17/8/22 下午7:14
     */
    private void testHorizon() {
        hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
        previewImg = (ImageView) findViewById(R.id.image_preview);
        String[] titles = {"怀师", "南怀瑾军校", "闭关", "南怀瑾", "南公庄严照", "怀师", "南怀瑾军校", "闭关", "南怀瑾", "南公庄严照", "怀师法相"};
        final int[] ids = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher};
        hListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(), titles, ids);
        hListView.setAdapter(hListViewAdapter);
        hListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                previewImg.setImageResource(ids[position]);
                hListViewAdapter.setSelectIndex(position);
                hListViewAdapter.notifyDataSetChanged();

            }
        });

    }

    /**
     * 添加注释：分组取除了最后一组之前的
     *
     * @aouto yjbo
     * @time 17/8/21 下午6:16
     */
    private void testSplit2() {
        String unSplit = "1,2,3,4,5";
        //根据长度取数组的最后一个元素
        Log.e("yjbo测试--", "MainActivity类： testSplit2: ==" + unSplit.split(",")[unSplit.split(",").length - 1]);
        int i = unSplit.lastIndexOf(",");
        String substring = unSplit.substring(0, i);
        Log.e("yjbo测试--", "MainActivity类： testSplit2: ==" + substring);
    }

    /**
     * 添加注释：测试拆分字段
     *
     * @aouto yjbo
     * @time 17/8/21 下午1:18
     */
    private void testSplit() {
        List<String> list1 = new ArrayList<>();
        list1.add("object:1");
        list1.add("oshbx:32");
        list1.add("cas:21");
        list1.add("asvfvfd:8");
        list1.add("gg:");

        for (int i = 0; i < list1.size(); i++) {
            String s = list1.get(i);
            if (s.indexOf(":") != -1) {
                String[] split = s.split(":");

                if (split.length > 1) {
                    Log.e("yjbo测试--", "MainActivity类： testSplit: =2=" + i + "---" + split[0] + "===" + split[1]);
                } else {
                    Log.e("yjbo测试--", "MainActivity类： testSplit: =1=" + i + "---" + split[0]);
                }

            }
        }
    }

    /**
     * 添加注释：测试map结构中放入多个内容
     *
     * @aouto yjbo
     * @time 17/8/2 上午11:33
     */
    private void testMapList() {
        Map<String, List<String>> mapTop = new HashMap<>();
        Map<String, List<String>> mapTop1 = new HashMap<>();
        Map<String, List<String>> mapTop2 = new HashMap<>();

        List<String> mapValue1 = new ArrayList<>();
        mapValue1.add("1");
        mapValue1.add("2");
        mapValue1.add("3");
        mapValue1.add("4");
        List<String> mapValue2 = new ArrayList<>();
        mapValue2.add("a");
        mapValue2.add("b");
        mapValue2.add("c");
        mapValue2.add("d");
        List<String> mapValue3 = new ArrayList<>();
        mapValue3.add("A");
        mapValue3.add("B");
        mapValue3.add("C");
        mapValue3.add("D");
        mapTop1.put("mapValue1", mapValue1);
        mapTop1.put("mapValue2", mapValue2);
        mapTop2.put("mapValue3", mapValue3);

        List<String> mapValue11 = mapTop1.get("mapValue1");
        int size = mapValue11.size();

        mapTop.putAll(mapTop1);
        mapTop.putAll(mapTop2);
    }

    /**
     * 添加注释：这是测试时间的；
     *
     * @aouto yjbo
     * @time 17/8/2 上午11:04
     */
    private void testFF() {
        String agoTimeStr = "2017-08-02 11:05:28";
        String result = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");

            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date parse = simpleDateFormat1.parse(agoTimeStr);
            result = simpleDateFormat.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("yjbo测试--", "MainActivity类： testFF: ==" + result);
    }

    public void testEdit() {
        cfViewTextEdit = (EditText) findViewById(R.id.cfViewTextEdit);
        mark = (TextView) findViewById(R.id.mark);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cfViewTextEdit.getWindowToken(), 0);

        showPre();
        cfViewTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showPre();
            }
        });
    }

    private void showPre() {
        String content = cfViewTextEdit.getText() + "";
        if (content.length() > 0) {
            mark.setVisibility(View.VISIBLE);
        } else {
            mark.setVisibility(View.GONE);
        }
    }
}
