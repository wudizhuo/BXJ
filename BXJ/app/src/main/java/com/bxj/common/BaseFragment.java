package com.bxj.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bxj.view.CustomerProgressDialog;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
	protected Activity mContext;
	private CustomerProgressDialog progressDialog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = this.getActivity();
	}

	/**
	 * 显示等待对话框
	 * 
	 * @param msg
	 *            对话框显示的字体
	 */
	public void showProgressDialog(final String msg) {
		if (getActivity() != null) {
			((BaseActivity) getActivity()).showProgressDialog(msg);
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
		if (getActivity() != null) {
			((BaseActivity) getActivity()).dismissProgressDialog();
		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getName());
	}

	public void onPause() {
		MobclickAgent.onPageEnd(this.getClass().getName());
		super.onPause();
	}
}
