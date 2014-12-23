package com.bxj.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.common.BaseActivity;
import com.bxj.fragment.ContentBXJFragment;
import com.bxj.fragment.SlidingMenuLeft;
import com.bxj.fragment.SlidingMenuRight;
import com.bxj.manager.StorageManager;
import com.bxj.manager.UpdateMgr;
import com.bxj.utils.ChannelUtil;
import com.bxj.utils.StatServiceUtil;
import com.bxj.utils.SystemUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.fb.FeedbackAgent;

public class MainActivity extends BaseActivity implements
		SlidingMenuLeft.Callbacks {
	private ContentBXJFragment contentFragment;
	public static String CONTENT_FRAGMENT = "content_fragment";
	public static String LEFT_FRAGMENT = "slidingmenu_left";
	public static String RIGHT_FRAGMENT = "slidingmenu_right";
	private long exitTime = 0;
	private SlidingMenu slidingmenu;
	private boolean isSecondaryOpen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		if (savedInstanceState != null)
			contentFragment = (ContentBXJFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, "mContent");
		if (contentFragment == null) {
			contentFragment = new ContentBXJFragment();
		}
		setContentView(R.layout.activity_main);
		setAllowSwipeFinsh(false);

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
		slidingmenu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				if (isSecondaryOpen) {
					isSecondaryOpen = false;
				} else {
					StatServiceUtil.trackEvent("打开左菜单");
				}
			}
		});

		slidingmenu.setSecondaryOnOpenListner(new OnOpenListener() {

			@Override
			public void onOpen() {
				isSecondaryOpen = true;
				StatServiceUtil.trackEvent("打开右菜单");
			}
		});

		slidingmenu.post(new Runnable() {

			@Override
			public void run() {
				appInit();
			}
		});

	}

	private void appInit() {
		UpdateMgr.getInstance().checkUpdate();
		if (!AppPreferences.getRegisterPush()) {
			Context context = getApplicationContext();
			XGPushManager.registerPush(context, new XGIOperateCallback() {
				@Override
				public void onSuccess(Object data, int flag) {
					Log.d("TPush", "注册成功，设备token为：" + data);
					AppPreferences.setRegisterPush(true);
				}

				@Override
				public void onFail(Object data, int errCode, String msg) {
					Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
				}
			});
		}

		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
	}

	@Override
	public void onItemSelected(String id) {

	}

	@Override
	public void onBackPressed() {
		if (contentFragment != null && contentFragment.onBackPressed()) {
			// 如果事件 被消费掉 return
			return;
		}

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
		super.onDestroy();
	}

}
