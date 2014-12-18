package com.bxj.manager;

import com.bxj.App;
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
		// 友盟检查更新服务
		UmengUpdateAgent.forceUpdate(App.getContext());
	}
}
