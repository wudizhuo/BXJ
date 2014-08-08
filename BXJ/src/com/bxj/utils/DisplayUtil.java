package com.bxj.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.bxj.App;

/**
 * Android大小单位转换工具类
 * 
 * @author wader
 * 
 */
public class DisplayUtil {

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth() {
		return App.getContext().getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 获取屏幕高度 实际显示的高度 (去除顶部栏)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenShowHeight(Activity context) {
		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return App.getContext().getResources().getDisplayMetrics().heightPixels
				- frame.top;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight() {
		return App.getContext().getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @param pActivity
	 * @return bitmap
	 */
	public static int getStautsHeight(Activity activity) {
		View view = activity.getWindow().getDecorView();
		// 获取状态栏高度
		Rect frame = new Rect();
		// 测量屏幕宽和高
		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight = frame.top;

		return stautsHeight;
	}

	/**
	 * dip 转换成 px值
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}
