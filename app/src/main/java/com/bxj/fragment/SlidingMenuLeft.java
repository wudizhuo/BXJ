package com.bxj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bxj.AppConstants;
import com.bxj.AppPreferences;
import com.bxj.R;
import com.bxj.common.BaseFragment;
import com.bxj.utils.StatServiceUtil;
import com.bxj.utils.ToastUtil;

public class SlidingMenuLeft extends BaseFragment implements OnClickListener {
	private Callbacks mCallbacks;
	private ImageView iv_setting_light;

	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(String id);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slidingleft, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		iv_setting_light = (ImageView) view.findViewById(R.id.iv_setting_light);

		view.findViewById(R.id.tv_bxj).setOnClickListener(this);
		view.findViewById(R.id.rl_bxj_light).setOnClickListener(this);
		initBtn();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bxj:
			// ToastUtil.show("点击的了步行街--对应的功能在开发中");
			break;
		case R.id.rl_bxj_light:
			AppConstants.SETTING_BXJ_LIGHT = !AppConstants.SETTING_BXJ_LIGHT;
			initBtn();
			AppPreferences.setSettingBxjLight(AppConstants.SETTING_BXJ_LIGHT);
			AppConstants.SETTING_CHANGED = true;
			if(!AppConstants.SETTING_BXJ_LIGHT){
				ToastUtil.showInCenter("刷新页面后生效");
				StatServiceUtil.trackEvent("只看亮贴-关");
			}else {
				StatServiceUtil.trackEvent("只看亮贴-开");
			}
			break;
		default:
			break;
		}
	}

	private void initBtn() {
		if (AppConstants.SETTING_BXJ_LIGHT) {
			iv_setting_light.setBackgroundResource(R.drawable.setting_open);
		} else {
			iv_setting_light.setBackgroundResource(R.drawable.setting_close);
		}
	}
}
