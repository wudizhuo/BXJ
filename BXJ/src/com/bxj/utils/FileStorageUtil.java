package com.bxj.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件存储工具类。
 */
public class FileStorageUtil {
	/**
	 * 应用程序根目录
	 */
	public static final String APP_DIR = "/bxj/";

	public static boolean writeObject(File file, Object object) {
		try {
			FileOutputStream fs = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(object);
			os.flush();
			os.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Object readObject(File file) {
		try {
			FileInputStream fs = new FileInputStream(file);
			ObjectInputStream os = new ObjectInputStream(fs);
			Object object = os.readObject();
			os.close();
			return object;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用于ObjectList的序列化
	 * 
	 * @param <T>
	 * 
	 * @param file
	 * @param lists
	 * @return
	 * @throws Exception
	 */
	public static <T> boolean writeObjectList(File file, List<T> lists) {
		if (lists == null || lists.size() <= 0) {
			return false;
		}
		try {
			FileOutputStream fs = new FileOutputStream(file, true);
			ObjectOutputStream os = null;
			if (file.length() < 1) {
				os = new ObjectOutputStream(fs);
			} else {
				os = new AppendableObjectOutputStream(fs);
			}
			for (T object : lists) {
				os.writeObject(object);
			}
			os.flush();
			fs.flush();
			os.close();
			fs.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取序列化的List<Object>
	 * 
	 * @param <T>
	 * 
	 * @param file
	 * @return
	 */
	private static FileInputStream fs;
	private static ObjectInputStream os;

	public static <T> List<T> readListObject(File file) {
		List<T> lists = new ArrayList<T>();
		try {
			fs = new FileInputStream(file);
			os = new ObjectInputStream(fs);
			Object obj = null;
			while ((obj = os.readObject()) != null) {
				lists.add((T) os.readObject());
			}
			os.close();
			fs.close();
		} catch (Exception e) {
			try {
				os.close();
				fs.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return lists;
	}

	/**
	 * 用于writeObject时 不写入header 方便反序列化时正常读取
	 * 
	 * @author SunZhuo
	 * 
	 */
	static class AppendableObjectOutputStream extends ObjectOutputStream {

		protected AppendableObjectOutputStream() throws IOException {
			super();
		}

		public AppendableObjectOutputStream(OutputStream output)
				throws IOException {
			super(output);
		}

		@Override
		protected void writeStreamHeader() throws IOException {
			// do nothing for 方便反序列化时正常读取
		}

	}

	/**
	 * 从字节流写入到文件中
	 * 
	 * @param file
	 *            要保存到的文件
	 * @param is
	 *            输入流
	 * @return
	 * @throws IOException
	 */
	public static void writeInputStream(File file, InputStream is)
			throws IOException {
		if (is == null || is.available() == 0) {
			throw new IOException();
		}
		OutputStream out = new FileOutputStream(file);
		byte buffer[] = new byte[1024 * 8];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		is.close();
	}

	/**
	 * 递归删除文件（夹）。
	 * 
	 * @param f
	 */
	public static void deleteFiles(File f) {
		if (null == f || !f.exists()) {
			return;
		}

		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				deleteFiles(child);
			}
		}

		f.delete();
	}

	/**
	 * 获取一个文件 |文件夹大小
	 * 
	 * @param file
	 * @return, 文件夹大小bytes
	 */
	public static long getFileSize(File file) {
		if (null == file || !file.exists()) {
			return 0L;
		}

		// 如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
		if (file.isDirectory()) {
			// 获取文件大小
			File[] fl = file.listFiles();
			long size = 0;
			for (File f : fl)
				size += getFileSize(f);
			return size;
		} else {
			return file.length();
		}
	}

}
