package com.bxj.manager;

import com.bxj.domain.WebData;
import com.bxj.utils.DownLoadTask;
import com.bxj.utils.FileStorageUtil;
import com.bxj.utils.HttpUtil;
import com.bxj.utils.NetUitl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DownLoadMgr {
	/**
	 * 唯一实例
	 */
	private static DownLoadMgr mInstance;
	private static int MAX_FILE_LENTH = 5 * 1024 * 1024;
	private List<WebData> downLoadingList;// 正在下载的列表
	private DownLoadTask downloadTask;// 下载任务

	private DownLoadMgr() {
		downLoadingList = new ArrayList<WebData>();
	}

	public static DownLoadMgr getInstance() {
		if (mInstance == null) {
			mInstance = new DownLoadMgr();
		}
		return mInstance;
	}

	public interface OndDownloadListener {
		void onProgressUpdate(int values);

		void onDownLoadFinshed();
	}

	/**
	 * 初始化下载任务类
	 *
	 * @param listener
	 *            下载监听器
	 */
	public void initDownTask(OndDownloadListener listener, String htmlType) {
		downloadTask = new DownLoadTask(listener);// 下载任务
		checkDataFile(htmlType);
	}

	/**
	 * 检查文件 大小 超过阀值就删除之前的下载文件
	 */
	private void checkDataFile(String htmlType) {
		File htmlDir = StorageManager.getInstance().getHtmlDir(htmlType);
		if (FileStorageUtil.getFileSize(htmlDir) > MAX_FILE_LENTH) {
			FileStorageUtil.deleteFiles(htmlDir);
		}
	}

	/**
	 * 开始下载
	 *
	 * @param list
	 */
	public void startDownLoadList(List list) {
		// 1将之前的下载列表(如果存在)清空 并且下载任务关闭
		if (downLoadingList.size() > 0) {
			// 暂停并关闭现有的下载任务
			// cancelDownLoad();
			downLoadingList.clear();
		}
		// 2添加现在的下载列表
		this.downLoadingList.addAll(list);
		// 3 开启下载
		downloadTask.execute(downLoadingList);
	}

	/**
	 * 取消下载
	 */
	public void cancelDownLoad() {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
	}

	/**
	 * 根据URL 下载Html文件 并且保存到本地文件中
	 *
	 * @param url
	 *            要下载的URL
	 * @param saveHtmlFile
	 *            保存到本地的文件
	 * @throws IOException
	 */
	public void getHtmlAndSave(String url, File saveHtmlFile,
			Boolean isMoblieUrl) throws IOException {
		if (!NetUitl.isConnected()) {
			throw new IOException();
		}
		InputStream htmlInputStream = HttpUtil.getHtmlInputStream(url,
				isMoblieUrl);
		FileStorageUtil.writeInputStream(saveHtmlFile, htmlInputStream);
	}

	public void getHtmlAndSave(WebData info, Boolean isMoblieUrl) throws IOException {
		InputStream is = HttpUtil
				.getHtmlInputStream(info.getUrl(), isMoblieUrl);
		write2File(info, is);
	}

	private void write2File(WebData info, InputStream is) throws IOException {
		File saveFile = StorageManager.getInstance().getDataFileByWebData(info);
        FileStorageUtil.writeInputStream(saveFile, is);
	}
}
