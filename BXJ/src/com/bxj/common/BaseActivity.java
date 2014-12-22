package com.bxj.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.utils.LogUtil;
import com.bxj.utils.StatServiceUtil;
import com.bxj.view.CustomerProgressDialog;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity {

	private CustomerProgressDialog progressDialog;
	private Button btn_title_left;
	private Button btn_title_right;
	private int swipeMinDistance = 80;
	private GestureDetector mGestureDetector;
	private boolean isAllowSwipeFinsh = true;
	private boolean progressOnShow = false;

	/** 获取intent附加数据 */
	protected void getIntentParams() {
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme();
		getIntentParams();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (isAllowSwipeFinsh) {
			mGestureDetector = new GestureDetector(this, gestureListener);
		}
	}

	/**
	 * 设置是否允许滑动屏幕返回
	 */
	protected void setAllowSwipeFinsh(boolean isAllowSwipeFinsh) {
		this.isAllowSwipeFinsh = isAllowSwipeFinsh;
	}

	SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e1 != null && e2 != null) {
				if (e2.getX() - e1.getX() > swipeMinDistance
						&& (Math.abs(e2.getX() - e1.getX()) / 4 > Math.abs(e1
								.getY() - e2.getY()))
						&& Math.abs(velocityX) > 0) {
					StatServiceUtil.trackEvent("右滑返回");
					BaseActivity.this.onBackPressed();
				}
			}
			return false;
		}
	};

	public boolean onTouchEvent(MotionEvent event) {
		if (isAllowSwipeFinsh) {
			return mGestureDetector.onTouchEvent(event);
		} else {
			return super.onTouchEvent(event);
		}
	};

	public void setTheme() {
		if (AppPreferences.getSettingModeNight()) {
			setTheme(R.style.NightTheme);
			LogUtil.s("BaseActivity---setTheme----NightTheme");
		} else {
			setTheme(R.style.DefultTheme);
			LogUtil.s("BaseActivity---setTheme----DefultTheme");
		}
	}

	public void setRightBtnText(CharSequence text) {
		btn_title_right = (Button) findViewById(R.id.btn_title_right);
		if (btn_title_right != null) {
			btn_title_right.setVisibility(View.VISIBLE);
			btn_title_right.setText(text);
		}
	}

	public void setTitleText(String text) {
		TextView tv_titlebar_midtv = (TextView) findViewById(R.id.tv_titlebar_midtv);
		tv_titlebar_midtv.setText(text);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}

	/**
	 * 显示等待对话框
	 * 
	 * @param msg
	 *            对话框显示的字体
	 */
	public void showProgressDialog(String msg) {
		if (!isFinishing() && !progressOnShow) {
			progressDialog = new CustomerProgressDialog(this);
			if (!TextUtils.isEmpty(msg)) {
				progressDialog.setMessage(msg);
			}
			progressDialog.show();
			progressOnShow = true;
		}
	}

	/**
	 * 显示等待对话框
	 */
	public void showProgressDialog() {
		this.showProgressDialog(null);
	}

	/**
	 * 取消等待对话框
	 */
	public void dismissProgressDialog() {
		if (!isFinishing() && progressDialog != null
				&& progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressOnShow = false;
		}
	}

	// public void addGuideImage(int guideResourceId) {
	// View viewParent = getWindow().getDecorView();
	// if (viewParent instanceof FrameLayout) {
	// final FrameLayout frameLayout = (FrameLayout) viewParent;
	// if (guideResourceId != 0) {// 设置了引导图片
	// ImageView guideImage = new ImageView(this);
	// guideImage.setVisibility(View.INVISIBLE);
	// FrameLayout.LayoutParams params = new
	// FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
	// ViewGroup.LayoutParams.FILL_PARENT);
	// guideImage.setLayoutParams(params);
	// guideImage.setScaleType(ScaleType.FIT_XY);
	// guideImage.setImageResource(guideResourceId);
	// guideImage.setOnClickListener(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// frameLayout.removeView(guideImage);
	// guideImage.getDrawable().setCallback(null);
	// VanclPreferences.setIsGuided(getApplicationContext(),
	// BasicActivity.this.getClass().getName());
	// }
	// });
	// frameLayout.addView(guideImage);// 添加引导图片
	//
	// }
	// }
	// }

	public void onBackPressed(View v) {
		LogUtil.s("btn_title_left");
		onBackPressed();
	}

	public Button getRightBtn() {
		return btn_title_right;
	}

}
