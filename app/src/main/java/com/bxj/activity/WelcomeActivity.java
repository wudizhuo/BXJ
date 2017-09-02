package com.bxj.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import com.bxj.AppConstants;
import com.bxj.R;
import com.bxj.common.BaseActivity;

/**
 * 欢迎页 显示图片
 * 
 * @author SunZhuo
 * 
 */
public class WelcomeActivity extends BaseActivity {

	private int SHOW_TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				appInit();
				SystemClock.sleep(SHOW_TIME);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				startApp();
			}

		}.execute();
	}

	/**
	 * 程序的初始化
	 */
	private void appInit() {
		AppConstants.initConstants();
	}

	public void startApp() {
		startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
		finish();
	}

}
