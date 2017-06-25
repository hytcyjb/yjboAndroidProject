package com.yy.yjbo.yjbodownmanager2.downUtil.util;

import android.util.Log;

/**
 * Log统一管理类,可以选择默认tag（cloudbag_log）或自己转入tag
 * @author lxf
 */
public class LogUtils {
	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "89mc_log";

	// 下面四个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			yjbo(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			yjbo(TAG, msg);
//			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			yjbo(TAG, msg);
//			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			yjbo(TAG, msg);
//			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			yjbo(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			yjbo(tag, msg);
//			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			yjbo(tag, msg);
//			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String msg, Exception tr) {
		if (isDebug) {
			Log.i(TAG, msg, tr);
//			Log.e(TAG, msg, tr);
		}
		
	}
	public static void yjbo(String tag, String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
//			Log.d(tag, msg);
		}

	}
}
