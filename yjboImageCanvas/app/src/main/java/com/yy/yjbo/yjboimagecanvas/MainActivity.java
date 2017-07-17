package com.yy.yjbo.yjboimagecanvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * 原作者，https://github.com/hdodenhof/CircleImageView
 * 基于此做矩形的四周圆角加边框的功能；
 * @author yjbo
 * @time 2017/6/1 20:50
 * @qq 1457521527
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //主要的亮点就在于对自定义view的进一步了解，以及对边框添加的掌握；
        // 在以后有能力的情况下，可以进一步的看这种自定义的消耗内容，线程阻塞的情况的发生；
    }
}
