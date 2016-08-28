package com.bxj.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.bxj.App;
import com.bxj.R;
import com.bxj.activity.MainActivity;
import com.bxj.domain.WebData;
import com.bxj.fragment.SlidingMenuRight;
import com.bxj.manager.DownLoadMgr;
import com.bxj.manager.DownLoadMgr.OndDownloadListener;

import java.util.List;

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
		NotificationManager mNotificationManager = (NotificationManager) App
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(App.getContext(),
				MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(
				App.getContext(), 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(App.getContext())
						.setSmallIcon(R.drawable.app_icon)
						.setContentTitle(App.getContext()
								.getText(R.string.notification_title))
						.setContentText(App
								.getContext().getText(R.string.notification_content))
						.setContentIntent(contentIntent);
		Notification notification = mBuilder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
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
