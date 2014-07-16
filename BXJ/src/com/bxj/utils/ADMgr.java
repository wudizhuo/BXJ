package com.bxj.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.adsmogo.adview.AdsMogoLayout;
import com.bxj.AppConstants;

/**
 * 广告管理类
 * 
 * @author SunZhuo
 * 
 */
public class ADMgr {
	/**
	 * 添加自定义尺寸广告
	 * 
	 * @param viewGroup
	 *            用于fragment
	 * @param width
	 * @param height
	 */
	public static void addBottomAD(ViewGroup viewGroup, int width, int height) {
		/**
		 * width为自定义尺寸的宽。height为自定义尺寸的高。isRotate是否需要轮换，如果不轮换则 展示或是失败以后就不再请求广告。
		 */
		AdsMogoLayout adsMogoLayoutCode = new AdsMogoLayout(
				(Activity) viewGroup.getContext(), AppConstants.MOGOID, width,
				height, true);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告出现的位置(悬浮于底部)
		params.bottomMargin = 0;
		// adsMogoLayoutCode.setAdsMogoListener(this);
		params.gravity = Gravity.BOTTOM;
		viewGroup.addView(adsMogoLayoutCode, params);
	}

	/**
	 * 添加自定义尺寸广告
	 * 
	 * @param mContext
	 *            用于Activity
	 * @param width
	 * @param height
	 */
	public static void addBottomAD(Activity mActivity, int width, int height) {
		/**
		 * width为自定义尺寸的宽。height为自定义尺寸的高。isRotate是否需要轮换，如果不轮换则 展示或是失败以后就不再请求广告。
		 */
		AdsMogoLayout adsMogoLayoutCode = new AdsMogoLayout(mActivity,
				AppConstants.MOGOID, width, height, true);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		// 设置广告出现的位置(悬浮于底部)
		params.bottomMargin = 0;
		// adsMogoLayoutCode.setAdsMogoListener(this);
		params.gravity = Gravity.BOTTOM;
		mActivity.addContentView(adsMogoLayoutCode, params);
	}
}
