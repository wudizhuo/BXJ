package com.bxj.utils;

import android.util.Log;

import com.bxj.BuildConfig;

/**
 * log工具类。
 */
public class LogUtil {
	/**
	 * 是否是调试状态
	 */
	private static final boolean DEBUG = BuildConfig.DEBUG;

	/**
	 * 调试提醒日志。
	 * 
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (DEBUG) {
			if (msg == null)
				return;
			Log.i(tag, msg);
		}
	}

	/**
	 * 调试提醒日志。
	 * 
	 * @param msg
	 */
	public static void s(String msg) {
		i("sunzhuo", msg);
	}
}
