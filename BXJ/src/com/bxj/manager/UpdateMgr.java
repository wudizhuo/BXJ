package com.bxj.manager;

import com.bxj.AppApplication;
import com.bxj.AppPreferences;
import com.bxj.utils.NetUitl;
import com.umeng.update.UmengUpdateAgent;

/**
 * 升级管理器
 * 
 * @author SunZhuo
 * 
 */
public class UpdateMgr {
	private static UpdateMgr mInstance = null;

	private UpdateMgr() {
	}

	/**
	 * 返回单例对象
	 * 
	 * @return
	 */
	public static UpdateMgr getInstance() {
		if (null == mInstance) {
			mInstance = new UpdateMgr();
		}
		return mInstance;
	}

	public void checkUpdate() {
		long checkTime = AppPreferences.getLastCheckUpdateTime() + 2 * 24 * 60
				* 60 * 1000;
		if (checkTime < System.currentTimeMillis()
				&& NetUitl.getNetType() == NetUitl.WIFI) {
			// 友盟检查更新服务
			UmengUpdateAgent.forceUpdate(AppApplication.getContext());
			AppPreferences.setLastCheckUpdateTime();
		}
	}
}
