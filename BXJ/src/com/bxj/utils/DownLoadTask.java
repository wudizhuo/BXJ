package com.bxj.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bxj.AppApplication;
import com.bxj.R;
import com.bxj.activity.MainActivity;
import com.bxj.domain.WebData;
import com.bxj.fragment.SlidingMenuRight;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.DownLoadMgr.OndDownloadListener;
import com.bxj.manager.StorageManager;

/**
 * 下载完成后要加通知 提醒 明天的版本就应该能内测
 * 
 * @author SunZhuo
 * 
 */
public class DownLoadTask extends AsyncTask<List<WebData>, Integer, Void> {
	SlidingMenuRight view;
	private OndDownloadListener listener;

	public DownLoadTask(OndDownloadListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		view = new SlidingMenuRight();
	}

	@Override
	protected Void doInBackground(List<WebData>... params) {
		WebData info;
		for (int i = 0; i < params[0].size(); i++) {
			info = params[0].get(i);
			DownLoadMgr.getInstance().getHtmlAndSave(info, true);
			int progress = (i + 1) * 100 / params[0].size();// 计算百分比
			publishProgress(progress);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		NotificationManager mNotificationManager = (NotificationManager) AppApplication
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.app_icon, null,
				System.currentTimeMillis());
		Intent intent = new Intent(AppApplication.getContext(),
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(
				AppApplication.getContext(), 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(AppApplication.getContext(),
				AppApplication.getContext()
						.getText(R.string.notification_title), AppApplication
						.getContext().getText(R.string.notification_content),
				contentIntent);
		// 以R.layout.layout_menu_right为ID 因为R文件不会重复
		mNotificationManager.notify(R.layout.layout_menu_right, notification);
		if (listener != null) {
			listener.onDownLoadFinshed();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		LogUtil.s("当前进度" + values[0]);
		if (listener != null) {
			listener.onProgressUpdate(values[0]);
		}
	}
}
