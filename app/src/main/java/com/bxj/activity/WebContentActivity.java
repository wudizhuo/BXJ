package com.bxj.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.domain.WebData;
import com.bxj.utils.LogUtil;
import com.bxj.utils.NetUitl;
import com.bxj.utils.ToastUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import java.net.MalformedURLException;
import java.net.URL;

public class WebContentActivity extends BaseActivity implements OnTouchListener {
    private String webContent;// 显示的网页的本地内容
    private PullToRefreshWebView mPullRefreshWebView;
    private WebView webView;// 用于显示的webview
    private static final int SET_VIEW = 1;
    private WebData webData;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcontent);
        setTitleText(AppConstants.CONTENT_TYPE);
        mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webcontent_wb);
        mPullRefreshWebView.setOnRefreshListener(onRefreshListener);
        webView = mPullRefreshWebView.getRefreshableView();
        webView.setOnTouchListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);// 支持js脚本
        settings.setSupportZoom(true);// 支持缩放
        settings.setUseWideViewPort(true);// 支持不同的分辨率
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setLoadWithOverviewMode(true);

        webView.setWebViewClient(new LoadingWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        if (!TextUtils.isEmpty(webData.getUrl())) {
            webView.loadUrl(webData.getUrl());
        }

        View night_theme_view = findViewById(R.id.night_theme_view);
        if (AppConstants.SETTING_MODE_NIGHT) {
            night_theme_view.setVisibility(View.VISIBLE);
        } else {
            night_theme_view.setVisibility(View.GONE);
        }
        webView.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!AppPreferences.getIsShowGestureCloseNotice()) {
                    ToastUtil.show("手势右划返回");
                    ToastUtil.show("手势右划返回");
                    AppPreferences.setIsShowGestureCloseNotice(true);
                }
            }
        }, 1000);
    }

    OnRefreshListener<WebView> onRefreshListener = new OnRefreshListener<WebView>() {

        @Override
        public void onRefresh(PullToRefreshBase<WebView> refreshView) {
            if (NetUitl.isConnected()) {
                webView.loadUrl(webData.getUrl());
            } else {
                ToastUtil.show("当前未联网哦");
                mPullRefreshWebView.onRefreshComplete();
            }
        }
    };

    private class LoadingWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (new URL(url).getPath().equals(new URL(webData.getUrl()).getPath())) {
                    view.loadUrl(url);
                } else {
                    Intent intent = SecondWebActivity.getIntent2Me(
                            WebContentActivity.this, url);
                    startActivity(intent);
                }
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissProgressDialog();
            progressBar.setVisibility(View.GONE);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            LogUtil.s("webView.goBack()");
            webView.goBack(); // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 不要删除这个方法 保证右滑返回 因为内嵌webview　 要把WebView的Touch时间　给activity处理 才能处理右划返回。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
