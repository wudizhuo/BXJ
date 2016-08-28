package com.bxj.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.os.AsyncTask;

import com.bxj.AppConstants;
import com.bxj.R;
import com.bxj.domain.WebData;
import com.bxj.utils.NetUitl;
import com.bxj.utils.ToastUtil;

/**
 * 用于管理网页显示的内容
 * 
 * @author SunZhuo
 * 
 */
public class WebContentMgr {
	/**
	 * 唯一实例
	 */
	private static WebContentMgr mInstance;

	private WebContentMgr() {
	}

	public static WebContentMgr getInstance() {
		if (mInstance == null) {
			mInstance = new WebContentMgr();
		}
		return mInstance;
	}

}
