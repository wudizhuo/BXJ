package com.bxj.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.bxj.App;
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
			Toast.makeText(App.getContext(), text, 0).show();
		}
	}

	public static void show(int resId) {
		if (DEBUG) {
			Toast.makeText(App.getContext(), resId, 0).show();
		}
	}
	
	public static void showInCenter(CharSequence text) {
		if (text == null)
			return;
		Toast toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showLongTimeInCenter(CharSequence text) {
		if (text == null)
			return;
		Toast toast = Toast.makeText(App.getContext(), text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
