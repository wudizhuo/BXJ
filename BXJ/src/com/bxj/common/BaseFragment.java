package com.bxj.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.bxj.utils.LogUtil;
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
		if (this.getView() == null) {
			return;
		}
		this.getView().post(new Runnable() {
			@Override
			public void run() {
				if (progressDialog == null) {
					progressDialog = new CustomerProgressDialog(
							BaseFragment.this.getView().getContext());
				}
				if (!TextUtils.isEmpty(msg)) {
					progressDialog.setMessage(msg);
				}
				LogUtil.s("打开progressDialog");
				progressDialog.show();
			}
		});
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
		LogUtil.s("关闭progressDialog--null");
		if (progressDialog != null) {
			if (this.getView() == null) {
				return;
			}
			this.getView().post(new Runnable() {
				@Override
				public void run() {
					LogUtil.s("关闭progressDialog");
					progressDialog.dismiss();
				}
			});
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
