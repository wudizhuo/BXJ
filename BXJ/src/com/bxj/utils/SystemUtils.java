package com.bxj.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class SystemUtils {
	/**
	 * 如果没有存在存储卡 填出对话框 提醒没有存储卡
	 * 
	 * @param mContext
	 */
	public static void checkSDcardDlg(final Activity mContext) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("没有存储卡哦，请先安装存储卡");
		builder.setPositiveButton("知道了", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mContext.finish();
			}
		});

		builder.create().show();
	}
}
