package com.bxj.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bxj.AppConstants;
import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.domain.WebData;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.StorageManager;
import com.bxj.utils.LogUtil;
import com.bxj.utils.NetUitl;
import com.bxj.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * 此页面待办事项 1.webView 给路径 直接加载就行了 不用自己读进来 修改
 * 
 * @author SunZhuo
 * 
 */
public class WebContentActivity extends BaseActivity {
	private String webContent;// 显示的网页的本地内容
	private PullToRefreshWebView mPullRefreshWebView;
	private WebView webView;// 用于显示的webview
	private static final int SET_VIEW = 1;
	private WebData webData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webcontent);
		setTitleText(AppConstants.CONTENT_TYPE);
		mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webcontent_wb);
		mPullRefreshWebView.setOnRefreshListener(onRefreshListener);
		webView = mPullRefreshWebView.getRefreshableView();
		webView.setFocusable(true);// 设置触摸焦点起作用
		webView.getSettings().setJavaScriptEnabled(true);// 支持js脚本
		webView.getSettings().setPluginState(WebSettings.PluginState.ON);// 设置不支持插件
		// mWebView.getSettings().setSupportZoom(true);// 支持缩放
		webView.getSettings().setUseWideViewPort(true);// 支持不同的分辨率
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebViewClient(new LoadingWebViewClient());
		getContent(webData);
	}

	OnRefreshListener<WebView> onRefreshListener = new OnRefreshListener<WebView>() {

		@Override
		public void onRefresh(PullToRefreshBase<WebView> refreshView) {
			if (NetUitl.isConnected()) {
				getNetContent(webData);
			} else {
				ToastUtil.show("当前未联网哦");
				mPullRefreshWebView.onRefreshComplete();
			}
		}
	};
	private GestureDetector mGestureDetector;

	private class LoadingWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			showProgressDialog();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Intent intent = SecondWebActivity.getIntent2Me(
					WebContentActivity.this, url);
			startActivity(intent);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			dismissProgressDialog();
			super.onPageFinished(view, url);
		}
	}

	@Override
	protected void getIntentParams() {
		Intent intent = getIntent();
		webData = (WebData) intent.getSerializableExtra("WebData");
	}

	public static Intent getIntent2Me(Context mContext, WebData info) {
		Intent intent = new Intent(mContext, WebContentActivity.class);
		intent.putExtra("WebData", info);
		return intent;
	}

	/**
	 * 显示内容
	 */
	public void getContent(final WebData info) {
		final File offFile = StorageManager.getInstance().getDataFileByWebData(
				info);
		// 本地有缓存
		if (offFile.exists()) {
			webContent = getUrlDataFromOffline(offFile);
			webView.loadDataWithBaseURL(null, webContent, "text/html", "utf-8",
					null);
		} else if (NetUitl.isConnected()) {
			getNetContent(info);
		} else {
			ToastUtil.show(R.string.notiy_net_errer);
		}
	}

	/**
	 * 
	 * 获取网络数据 如果本地没有缓存 有网络连接 先进行下载缓存 再次递归操作
	 * 
	 * @param info
	 */
	private void getNetContent(final WebData info) {
		new AsyncTask<WebData, Void, Void>() {
			@Override
			protected void onPreExecute() {
				showProgressDialog();
			}

			@Override
			protected Void doInBackground(WebData... params) {
				DownLoadMgr.getInstance().getHtmlAndSave(params[0], true);
				LogUtil.s("没有本地缓存 下载中");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				dismissProgressDialog();
				getContent(info);
			}
		}.execute(info);
	}

	/**
	 * 从下载的本地文件中 获得离线网页内容
	 * 
	 * @param offFile
	 */
	private String getUrlDataFromOffline(File offFile) {
		StringBuilder result = new StringBuilder();
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(
					offFile), AppConstants.CHARSET);
			BufferedReader br = new BufferedReader(is);
			String tmp;
			while ((tmp = br.readLine()) != null) {
				result.append(tmp);
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			LogUtil.s("webView.goBack()");
			webView.goBack(); // goBack()表示返回webView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		LogUtil.s("点击了返回");
		super.onBackPressed();
	}

}
