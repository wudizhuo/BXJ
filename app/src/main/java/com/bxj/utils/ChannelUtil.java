package com.bxj.utils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class ChannelUtil {

	/**
	 * 获取 Umeng渠道号
	 * 
	 * @param context
	 * @return
	 */
	public static String getUmengChannal(Context context) {
		String result = "99999";
		String metainf = getMETAINF(context, "Uchannel");
		LogUtil.s(metainf);
		String[] split = metainf.split("_");
		if (split != null && split.length >= 2) {
			result = metainf.substring(split[0].length() + 1);
		}
		System.out.println("---UmengChannal---" + result);
		return result;
	}

	private static String getMETAINF(Context context, String startsWithchannel) {
		ApplicationInfo appinfo = context.getApplicationInfo();
		String sourceDir = appinfo.sourceDir;
		String ret = "";
		ZipFile zipfile = null;
		try {
			zipfile = new ZipFile(sourceDir);
			Enumeration<?> entries = zipfile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				String entryName = entry.getName();
				if (entryName.startsWith("META-INF/" + startsWithchannel)) {
					ret = entryName;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zipfile != null) {
				try {
					zipfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
