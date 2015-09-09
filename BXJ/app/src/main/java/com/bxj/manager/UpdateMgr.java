package com.bxj.manager;

import u.aly.di;

import com.bxj.App;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 升级管理器
 * 
 * @author SunZhuo
 * 
 */
public class UpdateMgr {
	private static UpdateMgr mInstance = null;
	protected boolean hasNewVersion;
	private UpdateListener listener;

	private UpdateMgr() {
		UmengUpdateAgent.setUpdateListener(umengUpdateListener);
	}

	public interface UpdateListener {
		void onUpdateReturned(boolean hasNewVersion);
	}

	public void setUpdateListener(UpdateListener listener) {
		this.listener = listener;
	}

	private UmengUpdateListener umengUpdateListener = new UmengUpdateListener() {

		@Override
		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {

			switch (updateStatus) {
			case UpdateStatus.Yes: //
				hasNewVersion = true;
				break;
			case UpdateStatus.No: //
				hasNewVersion = false;
				break;
			}
			if (listener != null) {
				listener.onUpdateReturned(hasNewVersion);
			}
		}
	};

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
