package com.bxj;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
	public static final String SHAREDPREFERENCES_NAME = "vancl_sp";
	public static SharedPreferences sPreferences;
	static {
		sPreferences = PreferenceManager
				.getDefaultSharedPreferences(App.getContext());
	}

	// 保存离线下载文件的日期 用于判断之前的离线缓存是否删除
	private static final String OFFLINE_TIME = "offline_time";

	/**
	 * 得到 离线下载文件的日期 用于判断之前的离线缓存是否删除
	 * 
	 * @return
	 */
	public static int getOfflineTime() {
		return sPreferences.getInt(OFFLINE_TIME, 0);
	}

	/**
	 * 设置 离线下载文件的日期 用于判断之前的离线缓存是否删除
	 * 
	 * @param value
	 * @return
	 */
	public static boolean setOfflineTime(int value) {
		return sPreferences.edit().putInt(OFFLINE_TIME, value).commit();
	}

	// 保存离线下载文件的日期 用于判断之前的离线缓存是否删除

	private static final String SETTING_BXJLIGHT = "setting_bxjlight";
	private static final String SETTING_MODE_NIGHT = "setting_mode_night";
	private static final String HAS_SLIDINGGUIDE = "has_slidingguide";

	/**
	 * 得到设置变量 是否只显示亮贴
	 * 
	 * @return
	 */
	public static boolean getSettingBxjLight() {
		return sPreferences.getBoolean(SETTING_BXJLIGHT, false);
	}

	public static boolean setSettingBxjLight(boolean value) {
		return sPreferences.edit().putBoolean(SETTING_BXJLIGHT, value).commit();
	}

	/**
	 * 是否显示过引导页
	 * 
	 * @return
	 */
	public static boolean getHasSlidingGuide() {
		return sPreferences.getBoolean(HAS_SLIDINGGUIDE, false);
	}

	public static boolean setHasSlidingGuide(boolean value) {
		return sPreferences.edit().putBoolean(HAS_SLIDINGGUIDE, value).commit();
	}

	/**
	 * 夜间模式
	 * 
	 * @return
	 */
	public static boolean getSettingModeNight() {
		return sPreferences.getBoolean(SETTING_MODE_NIGHT, false);
	}

	public static boolean setSettingModeNight(boolean value) {
		return sPreferences.edit().putBoolean(SETTING_MODE_NIGHT, value)
				.commit();
	}

}
