package com.buyauto.util.method;

import java.io.File;
import java.util.Date;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @Title: AjaxFileUtil.java
 * @Package com.hoyoda.blade.common.file
 * @Description: 文件上传util
 * @author LeoZhang
 * @date 2014-6-19 上午11:46:54
 * @version V2.0
 */
public class AjaxFileUtil {
	/**
	 * 将文件写入目标路径
	 * 
	 * @param rootPath
	 *            根目录
	 * @param fileName
	 *            文件名
	 * @param mFile
	 *            实际文件
	 * @throws Exception
	 */
	public static String write(String rootPath, String fileName, CommonsMultipartFile mFile) throws Exception {
		if (mFile == null || mFile.getFileItem() == null)
			return "";
		File rootFile = new File(rootPath);
		if (!rootFile.exists()) {
			rootFile.mkdirs();
		}
		File file = new File(rootFile.getPath() + File.separator + fileName);
		file.createNewFile();
		// mFile.getFileItem().write(file);

		return file.getName();
	}

	/**
	 * 删除目标文件
	 * 
	 * @param realPath
	 *            文件的绝对路径
	 * @return
	 */
	public static boolean delete(String realPath) {
		File file = new File(realPath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 创建目标文件夹
	 * 
	 * @param rootPath
	 */
	private static synchronized void createRootPath(String rootPath) {
		File file = new File(rootPath);
		if (file.exists()) {
			return;
		} else {
			file.mkdirs();
		}
	}

	/**
	 * 将文件头转换成16进制字符串
	 * 
	 * @param 原生byte
	 * @return 16进制字符串
	 */
	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	private static String getCurTime(String filename) {
		int lastIndex = filename.lastIndexOf(".");
		String str = filename.substring(0, lastIndex) + new Date().getTime() + "" + filename.substring(lastIndex);
		return str;
	}

}
