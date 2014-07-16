package com.bxj.activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.manager.StorageManager;
import com.bxj.utils.FileStorageUtil;
import com.bxj.utils.StatServiceUtil;
import com.bxj.utils.ToastUtil;

public class SettingActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting);
		setTitleText("设置");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_clear:
			settingClear();
			break;
		case R.id.setting_feedback:
			settingFeedback();
			break;
		case R.id.setting_about:
			settingAbout();
			break;

		default:
			break;
		}
	}

	/**
	 * 意见反馈的点击事件
	 */
	private void settingFeedback() {
		startActivity(new Intent(this, FeedBcakActivity.class));
	}

	/**
	 * 清理缓存的点击事件
	 */
	private void settingClear() {
		StatServiceUtil.trackEvent(this, "设置页面_手动清理缓存");
		File offlineDataDir = StorageManager.getInstance().getOfflineDataDir();
		if (offlineDataDir.exists()) {
			FileStorageUtil.deleteFiles(offlineDataDir);
		}
		ToastUtil.show("清除离线数据完成");
	}

	/**
	 * 关于的点击事件
	 */
	private void settingAbout() {
		startActivity(new Intent(this, AboutActivity.class));
	}
}
