package com.bxj;

public class AppConstants {
	public static String CONTENT_TYPE = "步行街";
	public static String OFFLINE_DIR = "offline_data/";
	public static String TYPE_BXJ = "bxj";
	public static String HTML_DIR = "/html/";
	public static String CHARSET = "utf-8";
	public static final String URL_BXJ = "http://bbs.hupu.com/bxj";

	// 芒果广告平台的ID
	public static final String MOGOID = "07098cca441b48e898cedd87eb226d5c";

	// 程序设置界面的变量
	// 页面是否需要变化
	public static Boolean SETTING_CHANGED = false;
	// 只看亮贴
	public static Boolean SETTING_BXJ_LIGHT = false;
	// 夜间模式
	public static Boolean SETTING_MODE_NIGHT = false;
	// 主页面是否需要重新加载
	public static boolean isNeedRestore = false;

	public static void initConstants() {
		SETTING_BXJ_LIGHT = AppPreferences.getSettingBxjLight();
		SETTING_MODE_NIGHT = AppPreferences.getSettingModeNight();
	}

}
