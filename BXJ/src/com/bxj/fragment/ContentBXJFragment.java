package com.bxj.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.activity.MainActivity;
import com.bxj.adpter.BxjListAdpter;
import com.bxj.common.BaseFragment;
import com.bxj.domain.BXJListData;
import com.bxj.manager.BXJDataParseMgr;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.StorageManager;
import com.bxj.utils.LogUtil;
import com.bxj.utils.NetUitl;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 此页面待办事项 1.加入等待动画
 * 
 * @author SunZhuo
 * 
 */

public class ContentBXJFragment extends BaseFragment implements
		OnClickListener, OnRefreshListener2 {
	public static int INDEX = 1;
	public static final String URL_MAIN = "http://bbs.hupu.com/bxj";
	public static final int PARSE_SUCC = 1;
	public static final int PARSE_ERROR = 2;
	// 表示是否是下拉刷新状态
	public static Boolean ISREFRESHING = false;

	private static List<BXJListData> showDataList = new ArrayList<BXJListData>();
	private List<BXJListData> dataList;
	private ListView contentList;
	private BxjListAdpter mAdpter;
	private View containerView;
	private PullToRefreshListView pullToRefreshListView;
	private View emptyView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		containerView = inflater.inflate(R.layout.fragment_content, null);
		containerView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		emptyView = inflater.inflate(R.layout.view_content_empty, null);
		if (AppPreferences.getHasSlidingGuide()) {
			// 显示guide
		}
		pullToRefreshListView = (PullToRefreshListView) containerView
				.findViewById(R.id.contentList);
		pullToRefreshListView.setMode(Mode.BOTH);
		pullToRefreshListView.setOnRefreshListener(this);
		contentList = pullToRefreshListView.getRefreshableView();
		INDEX = 1;
		initTitle();
		return containerView;
	}

	private void initTitle() {
		TextView tv_titlebar_midtv = (TextView) containerView
				.findViewById(R.id.tv_titlebar_midtv);
		tv_titlebar_midtv.setText(AppConstants.CONTENT_TYPE);
		Button btn_title_left = (Button) containerView
				.findViewById(R.id.btn_title_left);
		View btn_title_right = containerView.findViewById(R.id.btn_title_right);
		btn_title_left
				.setBackgroundResource(R.drawable.content_left_btn_selector);
		btn_title_right
				.setBackgroundResource(R.drawable.content_right_btn_selector);
		btn_title_left.setVisibility(View.VISIBLE);
		btn_title_left.setText("");
		btn_title_right.setVisibility(View.VISIBLE);
		btn_title_left.setOnClickListener(this);
		btn_title_right.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(AppConstants.isNeedRestore){
			LogUtil.s("--onReInitContent---");
			onReInitContent();
		}else {
			LogUtil.s("--initContent---");
			initContent();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.s("onPause--暂停了");
	}

	public void settingChanged() {
		if (AppConstants.SETTING_CHANGED) {
			onReInitContent();
			AppConstants.SETTING_CHANGED = !AppConstants.SETTING_CHANGED;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PARSE_SUCC:
				dismissProgressDialog();
				updateListData();
				break;
			case PARSE_ERROR:
				dismissProgressDialog();
				contentList.setEmptyView(emptyView);
				LogUtil.s("PARSE_ERROR");
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 清除页面数据 初始化数据
	 */
	public void initContent() {
		showDataList.clear();
		if (NetUitl.isConnected()) {
			initContentFromNet();
		} else {
			initContentFromLocal();
		}
	}
	
	/**
	 * 设置修改等 需要重新从本地加载新设置的内容
	 */
	private void onReInitContent() {
		if (mAdpter == null) {
			mAdpter = new BxjListAdpter(mContext, showDataList);
			contentList.setAdapter(mAdpter);
		} else {
			mAdpter.notifyDataSetChanged();
		}
	}

	/**
	 * 从本地文件初始数据
	 */
	private void initContentFromLocal() {
		getBXJContent(false);
	}

	/**
	 * 从网络初始数据
	 */
	private void initContentFromNet() {
		// 第一次打开相当于是一次下拉刷新
		showProgressDialog();
		pullToRefreshListView.setRefreshing();
		onPullDownToRefresh(pullToRefreshListView);
	}

	private void getBXJContent(final Boolean isGetFromNet) {
		new Thread() {
			public void run() {
				try {
					/**
					 * 使用Jsoup解析html
					 */
					if (isGetFromNet && NetUitl.isConnected()) {
						dataList = getNetData();
					} else {
						dataList = getLocalData();
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(PARSE_ERROR);
					e.printStackTrace();
					return;
				}
				handler.sendEmptyMessage(PARSE_SUCC);
			};
		}.start();
	}

	/**
	 * 得到本地文件 加载本地文件的时候 显示进度条
	 * 
	 * @param saveHtmlFile
	 * @return
	 * @throws IOException
	 */
	private List<BXJListData> getLocalData() throws IOException {
		showProgressDialog();
		File saveHtmlFile = new File(StorageManager.getInstance().getTypeDir(
				AppConstants.TYPE_BXJ), AppConstants.TYPE_BXJ + "_data_"
				+ INDEX);
		return (List<BXJListData>) BXJDataParseMgr.getInstance().parse(
				saveHtmlFile);
	}

	/**
	 * 得到网络文件
	 * 
	 * @param saveHtmlFile
	 * @return
	 * @throws IOException
	 */
	private List<BXJListData> getNetData() throws IOException {
		File saveHtmlFile = new File(StorageManager.getInstance().getTypeDir(
				AppConstants.TYPE_BXJ), AppConstants.TYPE_BXJ + "_data_"
				+ INDEX);
		// URL_MAIN = "http://bbs.hupu.com/bxj-" + INDEX;
		String url = URL_MAIN + "-" + INDEX;
		DownLoadMgr.getInstance().getHtmlAndSave(url, saveHtmlFile,false);
		return (List<BXJListData>) BXJDataParseMgr.getInstance().parse(
				saveHtmlFile);
	}

	/**
	 * 用于下载时候调用获取下载的内容
	 * 
	 * @return
	 */
	public List<BXJListData> getDataList() {
		return showDataList;
	}

	/**
	 * 更新页面数据
	 */
	public void updateListData() {
		// 适应上拉刷新和第一次启动的情况
		if (INDEX == 1) {
			showDataList.clear();
		}
		showDataList.addAll(dataList);
		if (mAdpter == null) {
			mAdpter = new BxjListAdpter(mContext, showDataList);
			contentList.setAdapter(mAdpter);
		} else {
			mAdpter.notifyDataSetChanged();
		}
		if (ISREFRESHING) {
			pullToRefreshListView.onRefreshComplete();
			ISREFRESHING = false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			((MainActivity) getActivity()).getSlidingmenu().showMenu();
			break;
		case R.id.btn_title_right:
			((MainActivity) getActivity()).getSlidingmenu().showSecondaryMenu();
			break;

		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		LogUtil.s("下拉刷新");
		INDEX = 1;
		ISREFRESHING = true;
		getBXJContent(true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		LogUtil.s("上拉刷新");
		INDEX++;
		ISREFRESHING = true;
		getBXJContent(true);
	};

}
