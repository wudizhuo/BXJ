package com.bxj.utils;

import java.util.Properties;

import android.content.Context;

import com.bxj.App;
import com.umeng.analytics.MobclickAgent;

public class StatServiceUtil {
	private static Properties prop = new Properties();

	/**
	 * 
	 * @param mContext
	 *            页面上下文
	 * @param event_id
	 *            事件标识 ID参考统计平台上注册的ID
	 */
	public static void trackEvent(String event_id) {
		MobclickAgent.onEvent(App.getContext(), event_id);
	}

	/**
	 * 统计时间
	 */
	public static void trackBeginEvent(Context mContext, String event_id,
			String key, String value) {
		prop.clear();
		prop.setProperty(key, value);
		// StatService.trackCustomBeginKVEvent(mContext, event_id, prop);
	}

	/**
	 * 统计时间
	 */
	public static void trackEndEvent(Context mContext, String event_id,
			String key, String value) {
		prop.clear();
		prop.setProperty(key, value);
		// StatService.trackCustomEndKVEvent(mContext, event_id, prop);
	}
}
