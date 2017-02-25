package com.bxj.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bxj.App;
import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.activity.MainActivity;
import com.bxj.activity.SettingActivity;
import com.bxj.common.BaseActivity;
import com.bxj.common.BaseFragment;
import com.bxj.domain.BXJListData;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.DownLoadMgr.OndDownloadListener;
import com.bxj.utils.LogUtil;
import com.bxj.utils.StatServiceUtil;
import com.qihoo.updatesdk.lib.UpdateHelper;
import com.umeng.fb.FeedbackAgent;

import java.util.List;

/**
 * 1 在设置页面添加清除缓存功能 2 添加设置页面
 * 
 * @author SunZhuo
 * 
 */
public class SlidingMenuRight extends BaseFragment implements OnClickListener,
		OndDownloadListener {
	private TextView tv_download;
	private TextView tv_setting;
	private ImageView iv_setting_night;
	private ContentBXJFragment content_fragment;
	private boolean downloading = false;
	private ImageView unread;
	private boolean isClickCheckUpdate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slidingright, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		tv_download = (TextView) view.findViewById(R.id.tv_download);
		tv_setting = (TextView) view.findViewById(R.id.tv_setting);
		iv_setting_night = (ImageView) view.findViewById(R.id.iv_setting_night);
		unread = (ImageView) view.findViewById(R.id.unread);
		tv_download.setOnClickListener(this);
		tv_setting.setOnClickListener(this);
		view.findViewById(R.id.feedback).setOnClickListener(this);
		view.findViewById(R.id.check_update).setOnClickListener(this);
		view.findViewById(R.id.setting_night).setOnClickListener(this);
		initBtn();
		return view;
	}

	@Override
	public void onStart() {
		content_fragment = (ContentBXJFragment) getActivity()
				.getSupportFragmentManager().findFragmentByTag(
						MainActivity.CONTENT_FRAGMENT);
		super.onStart();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_download:
			downloadEvent();
			break;
		case R.id.tv_setting:
			toSetting();
			break;
		case R.id.setting_night:
			clickNightMode();
			break;
		case R.id.check_update:
			checkUpdate();
			break;
		case R.id.feedback:
			settingFeedback();
			break;
		default:
			break;
		}
	}

	private void checkUpdate() {
		isClickCheckUpdate = true;
		showProgressDialog("正在检查更新...");
		StatServiceUtil.trackEvent("检查更新");
		UpdateHelper.getInstance().manualUpdate(App.getContext().getPackageName());
	}

	/**
	 * 意见反馈的点击事件
	 */
	private void settingFeedback() {
		StatServiceUtil.trackEvent("意见反馈");
		FeedbackAgent agent = new FeedbackAgent(getActivity());
		agent.startFeedbackActivity();
	}

	/**
	 * 点击夜间模式的点击事件
	 */
	private void clickNightMode() {
		AppConstants.SETTING_MODE_NIGHT = !AppConstants.SETTING_MODE_NIGHT;
		initBtn();
		AppPreferences.setSettingModeNight(AppConstants.SETTING_MODE_NIGHT);
		((BaseActivity) getActivity()).setTheme();
		AppConstants.isNeedRestore = true;
		getActivity().finish();
		getActivity().overridePendingTransition(0, 0);
		Intent intent = getActivity().getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		getActivity().startActivity(intent);
		if (AppConstants.SETTING_MODE_NIGHT) {
			LogUtil.s("夜间模式-开");
			StatServiceUtil.trackEvent("夜间模式-开");
		} else {
			StatServiceUtil.trackEvent("夜间模式-关");
		}
	}

	private void initBtn() {
		if (AppConstants.SETTING_MODE_NIGHT) {
			iv_setting_night.setBackgroundResource(R.drawable.setting_open);
		} else {
			iv_setting_night.setBackgroundResource(R.drawable.setting_close);
		}
	}

	/**
	 * 跳转到设置界面
	 */
	private void toSetting() {
		startActivity(new Intent(mContext, SettingActivity.class));
	}

	/**
	 * 用于处理下载事件
	 */
	private synchronized void downloadEvent() {
		StatServiceUtil.trackEvent("点击离线下载按钮");
		if (downloading) {
			pauseDownload();
			tv_download.setText("离线下载");
			downloading = false;
		} else {
			startDownload();
			downloading = true;
		}
	}

	/**
	 * 启动下载
	 */
	private void startDownload() {
		List<BXJListData> dataList = content_fragment.getDataList();
		if (dataList == null || dataList.size() == 0) {
			return;
		}
		DownLoadMgr downLoadMgr = DownLoadMgr.getInstance();
		downLoadMgr.initDownTask(this, dataList.get(0).getUrlType());
		downLoadMgr.startDownLoadList(dataList);
		onProgressUpdate(0);// 点击开始下载 变成下载状态
	}

	/**
	 * 暂停下载
	 */
	private void pauseDownload() {
		DownLoadMgr downLoadMgr = DownLoadMgr.getInstance();
		downLoadMgr.cancelDownLoad();
	}

	@Override
	public void onProgressUpdate(int progress) {
		LogUtil.s("进度--" + progress);
		tv_download.setText("取消下载      " + progress + " %");
	}

	@Override
	public void onDownLoadFinshed() {
		tv_download.setText("下载完成      " + 100 + " %");
	}

}
