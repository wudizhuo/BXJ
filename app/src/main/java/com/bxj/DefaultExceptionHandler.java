package com.bxj;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

/**
 * 未捕获异常处理
 * 
 * @function :
 * @author :chenyong
 * @company :vancl
 * @date :2012-3-13
 */
public class DefaultExceptionHandler implements
		java.lang.Thread.UncaughtExceptionHandler {
	private final Context myContext;

	public DefaultExceptionHandler(Context context) {
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception) {

		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);

		System.out.println("程序出现了异常");

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				new AlertDialog.Builder(myContext).setTitle("提示")
						.setCancelable(false).setMessage("亲，程序繁忙，请稍后再试。")
						.setPositiveButton("知道了", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								System.exit(0);
							}
						}).create().show();
				Looper.loop();
			}
		}.start();

		/*
		 * try {
		 * 
		 * 
		 * 
		 * long time = System.currentTimeMillis(); Date date = new Date(time);
		 * String timestr = date.toGMTString(); StringBuffer sb = new
		 * StringBuffer(); sb.append(timestr); sb.append("\n"); Field[] fields =
		 * Build.class.getDeclaredFields(); for (Field field : fields) { String
		 * name = field.getName(); String values = field.get(null).toString();
		 * sb.append(name + ":" + values); sb.append("\n"); }
		 * 
		 * StringWriter sw = new StringWriter(); PrintWriter writer = new
		 * PrintWriter(sw); exception.printStackTrace(writer);
		 * 
		 * String errorlog = sw.toString(); File log = new
		 * File("/sdcard/log.txt"); FileOutputStream fos = new
		 * FileOutputStream(log); fos.write(sb.toString().getBytes());
		 * fos.write(errorlog.getBytes()); fos.flush(); fos.close();
		 * System.out.println("打印了-----"); // show_dialog(); // System.exit(10);
		 * // Toast.makeText(myContext, "目前网络繁忙，请稍后再试。", 1).show();
		 * 
		 * } catch (Exception e) { e.printStackTrace();
		 * 
		 * } //
		 */// Intent intent = new Intent(//跳转到出错HTML文件 目前无
			// "android.fbreader.action.CRASH",
		// new
		// Uri.Builder().scheme(exception.getClass().getSimpleName()).build()
		// );
		// try {
		// myContext.startActivity(intent);
		// } catch (ActivityNotFoundException e) {
		// intent = new Intent(myContext, VanclActivityGroup.class);
		// intent.putExtra(VanclActivityGroup.INTENT_TYPE,
		// VanclActivityGroup.FORCE_EXIT_ID);
		// myContext.startActivity(intent);
		// }
		// if (myContext instanceof Activity) {
		// ((Activity)myContext).finish();
		// }
		//
		//
		// Process.killProcess(Process.myPid());
		// //linux方法，关掉进程，但android管理，会重启原有activity
	}

}