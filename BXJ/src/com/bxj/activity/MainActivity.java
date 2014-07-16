package com.bxj.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.adsmogo.adview.AdsMogoLayout;
import com.bxj.AppApplication;
import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.fragment.ContentBXJFragment;
import com.bxj.fragment.SlidingMenuLeft;
import com.bxj.fragment.SlidingMenuRight;
import com.bxj.manager.StorageManager;
import com.bxj.utils.LogUtil;
import com.bxj.utils.SystemUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity implements
		SlidingMenuLeft.Callbacks {
	private ContentBXJFragment contentFragment;
	public static String CONTENT_FRAGMENT = "content_fragment";
	public static String LEFT_FRAGMENT = "slidingmenu_left";
	public static String RIGHT_FRAGMENT = "slidingmenu_right";
	long exitTime = 0;
	private SlidingMenu slidingmenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// set the Above View
		if (savedInstanceState != null)
			contentFragment = (ContentBXJFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, "mContent");
		if (contentFragment == null) {
			contentFragment = new ContentBXJFragment();
		}
		setContentView(R.layout.activity_main);
		// 没有内存卡就弹窗退出
		if (!StorageManager.getInstance().existSDcard()) {
			SystemUtils.checkSDcardDlg(this);
		}

		slidingmenu = new SlidingMenu(this);
		slidingmenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingmenu.setBehindOffset(getWindowManager().getDefaultDisplay()
				.getWidth() / 3);
		slidingmenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingmenu.setMenu(R.layout.layout_menu_left);
		slidingmenu.setSecondaryMenu(R.layout.layout_menu_right);

		// set the Above View

		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.content_container, contentFragment,
						CONTENT_FRAGMENT).commit();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.slidingmenu_left, new SlidingMenuLeft(),
						LEFT_FRAGMENT).commit();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.slidingmenu_right, new SlidingMenuRight(),
						RIGHT_FRAGMENT).commit();
		slidingmenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				contentFragment.settingChanged();
			}
		});
		NotificationManager mNotificationManager = (NotificationManager) AppApplication
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(R.layout.layout_menu_right);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 友盟检查更新服务
				UmengUpdateAgent.update(MainActivity.this);
			}
		});
	}

	@Override
	public void onItemSelected(String id) {

	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), R.string.exit_notiy,
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			exitApp();
		}
	}

	/**
	 * 退出应用
	 */
	private void exitApp() {
		finish();
		System.exit(0);
	}

	public SlidingMenu getSlidingmenu() {
		return slidingmenu;
	}

	public void setSlidingmenu(SlidingMenu slidingmenu) {
		this.slidingmenu = slidingmenu;
	}

	@Override
	protected void onDestroy() {
		AdsMogoLayout.clear();
		super.onDestroy();
	}

}
