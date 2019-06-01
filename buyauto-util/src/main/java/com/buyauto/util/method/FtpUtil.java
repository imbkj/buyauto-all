package com.buyauto.util.method;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.buyauto.util.json.JSONUtil;
import com.buyauto.util.pojo.BaseSftpDto;

public class FtpUtil {

	private FTPClient ftpClient = null;
	private OutputStream os = null;
	private FileInputStream is = null;

	private BaseSftpDto ftpbean = null;

	public FtpUtil() {
		// ftpbean = new BaseSftpDto();
		// ftpbean.setHost("ioa.origin-sh.com");
		// ftpbean.setUserName("car");
		// ftpbean.setUserPwd("car123");
		// ftpbean.setPort(21);
		// ftpbean.setLoadPath("carlist");
		// ftpbean.setFilePath("E:\\work\\temp\\");
	}

	/**
	 * 连接ftp服务器
	 * 
	 * @throws IOException
	 */
	public boolean connectServer(BaseSftpDto ftpdto) {
		if (ftpClient != null) {
			return true;
		}
		ftpbean = ftpdto;
		ftpClient = new FTPClient();
		try {
			Integer valueOf = Integer.valueOf(ftpbean.getPort());
			if (valueOf != -1) {
				ftpClient.connect(ftpbean.getHost(), valueOf);
			} else {
				ftpClient.connect(ftpbean.getHost());
			}
			ftpClient.login(ftpbean.getUserName(), ftpbean.getUserPwd());
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); // 用2进制上传、下载
			System.out.println("已登录到\"" + ftpClient.printWorkingDirectory()
					+ "\"目录");
			ftpClient.enterLocalPassiveMode();	
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 断开与ftp服务器连接
	 * 
	 * @throws IOException
	 */
	public boolean closeServer() {
		try {
			if (is != null) {
				is.close();
			}
			if (os != null) {
				os.close();
			}
			if (ftpClient != null) {
				ftpClient.disconnect();
				ftpClient = null;
			}
			System.out.println("已从服务器断开");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 检查文件夹在当前目录下是否存在
	 * 
	 * @param dir
	 * @return
	 */
	private boolean isDirExist(String dir) {
		try {
			System.out.println("原目录：" + ftpClient.printWorkingDirectory());
			ftpClient.changeWorkingDirectory(dir);
			System.out.println("现目录：" + ftpClient.printWorkingDirectory());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 在当前目录下创建文件夹
	 * 
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	private boolean createDir(String dir) {
		boolean flag = true;
		try {
			// System.out.println("dir=======" dir);
			flag = ftpClient.makeDirectory(dir);
			if (flag) {
				System.out.println("make Directory " + dir + " succeed");

			} else {

				System.out.println("make Directory " + dir + " false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 取得相对于当前连接目录的某个目录下所有文件列表
	 * 
	 * @param path
	 * @return
	 */
	public List getFileList(String path) {
		List list = new ArrayList();
		try {
			FTPFile[] ftpfiles = ftpClient.listFiles(path);
			for (int i = 0; ftpfiles.length > i; i++) {
				list.add(ftpfiles[i].getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List getFileList() {
		List list = new ArrayList();
		try {
			String[] filenames = ftpClient.listNames();
			if (filenames == null || filenames.length == 0) {
				System.out.println("目录为空");
			} else {
				for (int i = 0; i < filenames.length; i++) {
					list.add(filenames[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 重命名文件
	 * 
	 * @param oldFileName
	 *            --原文件名
	 * @param newFileName
	 *            --新文件名
	 */
	public boolean renameFile(String oldFileName, String newFileName) {
		try {
			ftpClient.rename(oldFileName, newFileName);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * 删除
	 */
	public boolean removeDirectory(String ftppath, String directory,
			String fileName) {
		try {
			ftpClient.changeWorkingDirectory(ftppath);// 转移到FTP服务器目录
			return ftpClient.removeDirectory(directory);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeFile(String ftppath, String fileName) {
		try {
			if (!fileName.isEmpty()) {
				boolean a = ftpClient.changeWorkingDirectory(ftppath);// 转移到FTP服务器目录
				boolean b = ftpClient.deleteFile(fileName);
				return b;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * FTP下载文件
	 */
	public boolean fileDownload(String uploadTemp, String fileName) {
		FileOutputStream fos = null;
		try {
			String remoteFileName = uploadTemp + fileName;
			fos = new FileOutputStream(ftpbean.getFilePath() + fileName);
			System.out.println("服务器路径=" + remoteFileName + ",本地路径"
					+ ftpbean.getFilePath() + fileName);
			return ftpClient.retrieveFile(remoteFileName, fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
			// try {
			// ftpClient.disconnect();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
		return false;
	}

	public String fileDownloadtoString(String ftppath, String localpath,
			String fileName) {
		FileOutputStream fos = null;
		try {
			File file = new File(localpath);
			if (!file.exists()) {
				boolean bb = file.mkdirs();
				System.out.println("创建文件夹：" + localpath + ":" + bb);
			}
			String remoteFileName = ftppath + fileName;
			fos = new FileOutputStream(localpath + fileName);
			System.out.println("[服务器路径=" + remoteFileName + ",本地路径" + localpath
					+ fileName + "]");
			boolean res = ftpClient.retrieveFile(remoteFileName, fos);
			if (res) {
				return localpath + fileName;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fos);
		}
		return null;
	}

	/**
	 * FTP上传文件
	 */
	public boolean fileUpload(String ftpPath, String fileName) {
		FileInputStream fis = null;
		try {

			File srcFile = new File(ftpbean.getFilePath() + fileName);
			fis = new FileInputStream(srcFile);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(ftpPath);
			ftpClient.setBufferSize(1024);
			return ftpClient.storeFile(fileName, fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean fileUpload(String ftpPath, File upFile) {
		System.out.println("file path " + ftpPath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(upFile);
			// 设置上传目录
			ftpClient.enterLocalPassiveMode();	
			ftpClient.changeWorkingDirectory(ftpPath);
			ftpClient.setBufferSize(1024);
			return ftpClient.storeFile(upFile.getName(), fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		FtpUtil ftp = new FtpUtil();
		String ftppath = "/carList/";
		List<String> returnList = new ArrayList<>();

	}
}
