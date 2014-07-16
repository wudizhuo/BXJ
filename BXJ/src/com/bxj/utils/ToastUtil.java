package com.bxj.utils;

import android.util.Log;
import android.widget.Toast;

import com.bxj.AppApplication;
import com.bxj.BuildConfig;

/**
 * log工具类。
 */
public class ToastUtil {
	/**
	 * 是否是调试状态
	 */
	public static final boolean DEBUG = BuildConfig.DEBUG;

	/**
	 * 调试提醒日志。
	 * 
	 * @param msg
	 * @param text
	 */
	public static void show(CharSequence text) {
		if (DEBUG) {
			if (text == null)
				return;
			Toast.makeText(AppApplication.getContext(), text, 0).show();
		}
	}

	public static void show(int resId) {
		if (DEBUG) {
			Toast.makeText(AppApplication.getContext(), resId, 0).show();
		}
	}

}
