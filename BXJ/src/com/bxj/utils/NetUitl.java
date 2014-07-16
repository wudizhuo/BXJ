package com.bxj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.bxj.AppApplication;

public class NetUitl {
	public static String WIFI = "WIFI";
	public static String MOBLIE_3G = "3G";
	public static String MOBLIE_2G = "2G";

	// static {
	// ConnectivityManager connectMgr = (ConnectivityManager) AppApplication
	// .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo info = connectMgr.getActiveNetworkInfo();
	// }
	/**
	 * 判断网络是否可用 <br>
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected() {
		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) AppApplication
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			// 是否在漫游，可根据程序需求更改返回值
			return false;
		}
		return true;
	}

	/**
	 * 获取网络类型 3G 2G WIFI
	 * 
	 * @return
	 */
	public static String getNetType() {
		try {
			ConnectivityManager pManager = (ConnectivityManager) AppApplication
					.getContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = pManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				int type = networkInfo.getType();
				if (type == ConnectivityManager.TYPE_WIFI) {
					return WIFI;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					int subtype = networkInfo.getSubtype();
					switch (subtype) {
					case TelephonyManager.NETWORK_TYPE_CDMA:
						return MOBLIE_2G;
					case TelephonyManager.NETWORK_TYPE_EDGE:
						return MOBLIE_2G;
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
						return MOBLIE_3G;
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
						return MOBLIE_3G;
					case TelephonyManager.NETWORK_TYPE_GPRS:
						return MOBLIE_2G;
					case TelephonyManager.NETWORK_TYPE_HSDPA:
						return MOBLIE_3G;
					case TelephonyManager.NETWORK_TYPE_UMTS:
						return MOBLIE_3G;
					default:
						return "other";
					}
				} else {
					return "other";
				}
			} else {
				return "unknown";
			}
		} catch (Exception ex) {
			return "unknown";
		}
	}
}
