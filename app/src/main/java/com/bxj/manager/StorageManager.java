package com.bxj.manager;

import com.bxj.App;
import com.bxj.AppConstants;

import java.io.File;

public class StorageManager {

	private static StorageManager mInstance = null;

	private StorageManager() {
	}

	public static StorageManager getInstance() {
		if (null == mInstance) {
			mInstance = new StorageManager();
		}
		return mInstance;
	}

	public File getOfflineDataDir() {
		File result = new File(
				App.getContext().getExternalFilesDir(null),
				AppConstants.OFFLINE_DIR);
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	public File getTypeDir(String urlType) {
		File result = new File(getOfflineDataDir(), urlType);
		if (!result.exists()) {
			result.mkdirs();
		}
		return result;
	}

	public static boolean existSDcard() {
		return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState());
	}

}
