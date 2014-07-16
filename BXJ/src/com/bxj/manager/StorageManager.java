package com.bxj.manager;

import java.io.File;

import com.bxj.AppApplication;
import com.bxj.AppConstants;
import com.bxj.domain.WebData;

/**
 * 目录管理类
 * 
 * @author SunZhuo
 * 
 */
public class StorageManager {

	private static StorageManager mInstance = null;

	private StorageManager() {
	}

	/**
	 * 返回单例对象
	 * 
	 * @return
	 */
	public static StorageManager getInstance() {
		if (null == mInstance) {
			mInstance = new StorageManager();
		}
		return mInstance;
	}

	public File getOfflineDataDir() {
		File result = null;
		result = new File(
				AppApplication.getContext().getExternalFilesDir(null),
				AppConstants.OFFLINE_DIR);
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	public File getTypeDir(String urlType) {
		File result = null;
		result = new File(getOfflineDataDir(), urlType);
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	public File getHtmlDir(String urlType) {
		File result = null;
		result = new File(getOfflineDataDir(), urlType + AppConstants.HTML_DIR);
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	/**
	 * 根据网络数据类型得到对应的文件
	 * 
	 * @param info
	 * @return
	 */
	public File getDataFileByWebData(WebData info) {
		File result = null;
		result = new File(getHtmlDir(info.getUrlType()), info.getUrl()
				.hashCode() + "");
		return result;
	}

	/**
	 * 判断存储卡是否存在
	 * 
	 * @return
	 */
	public static boolean existSDcard() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			return true;
		} else
			return false;
	}

}
