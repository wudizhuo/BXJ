package com.bxj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bxj.R;
import com.bxj.common.BaseActivity;

public class SecondWebActivity extends BaseActivity {
	private WebView webview;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_webcontent);
		webview = (WebView) findViewById(R.id.webview);
		webview.setWebViewClient(new LoadingWebViewClient());
		if (!TextUtils.isEmpty(url)) {
			webview.loadUrl(url);
		}
	}

	@Override
	protected void getIntentParams() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
	}

	public static Intent getIntent2Me(Context mContext, String url) {
		Intent intent = new Intent(mContext, SecondWebActivity.class);
		intent.putExtra("url", url);
		return intent;
	}

	private class LoadingWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			showProgressDialog();
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			dismissProgressDialog();
			super.onPageFinished(view, url);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Check if the key event was the Back button and if there's history
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
