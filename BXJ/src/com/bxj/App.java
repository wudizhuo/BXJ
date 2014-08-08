package com.bxj;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bxj.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class App extends Application {
	private static Context applicationContext;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this.getApplicationContext();
		LogUtil.s("BXJApplication 创建啦");
		checkConfig();
	}

	/**
	 * 检查应用配置
	 */
	private void checkConfig() {
		initFileDir();
		// 友盟的配置
		// 调试模式
		MobclickAgent.setDebugMode(BuildConfig.DEBUG);
		// 使用在线配置功能
		MobclickAgent.updateOnlineConfig(getContext());
		// 检查友盟的res是否都复制进去了
		UmengUpdateAgent.setUpdateCheckConfig(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		getOnlineValue();
	}

	/**
	 * 得到友盟的在线配置参数
	 * 
	 * 1，开启界面的图片的下载地址
	 */
	private void getOnlineValue() {
		String value = MobclickAgent.getConfigParams(getContext(), "wallpaper");
		// TODO 开启子线程下载wallpaper 在wifi情况下
	}

	/**
	 * 初始化文件目录
	 */
	private void initFileDir() {
		try {
			File appDir = new File(getContext().getExternalFilesDir(null),
					AppConstants.OFFLINE_DIR);
			if (!appDir.exists()) {
				appDir.mkdirs();
			}
			File createFile = new File(appDir, AppConstants.TYPE_BXJ + "/");// 创建步行街目录
			if (!createFile.exists()) {
				createFile.mkdirs();
			}
			createFile = new File(createFile, AppConstants.HTML_DIR);
			if (!createFile.exists()) {
				createFile.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Context getContext() {
		return applicationContext;
	}

	/**
	 * 获取app版本号。
	 */
	public static String getVersionName() {
		try {
			return getContext().getPackageManager().getPackageInfo(
					getContext().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
