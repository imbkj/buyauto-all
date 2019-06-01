package com.buyauto.util.method;

import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.buyauto.util.pojo.BaseSftpDto;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * @ClassName: IBaseSftpApi
 * @Description: 连接sftp通用类
 * @author cxz
 * @date 2015年9月22日 上午10:34:04
 *
 */
public class BaseSftpUtil {

	private final static Logger logger = LoggerFactory.getLogger(BaseSftpUtil.class);

	public void reconciliation(BaseSftpDto baseSftpDto) throws Exception {

		// String path = getLocal(fileName, now);
		// logger.info("解析文件地址：" + path);
		// CvsAnalysisUtil parser = new CvsAnalysisUtil(path);
		// List<BaseJymxResp> list = parser.readCSVFile();

	}

	/**
	 * 
	 * @Title: getLocal
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param fn 文件名
	 * @param @param now
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getLocal(BaseSftpDto sftp) throws Exception {

		String name = "";
//		logger.debug("获取对账目录文件名：" + "");
//		String path = "";
//		logger.debug("获取对账key文件：" + path);
//		JSch jsch = new JSch();
//		// jsch.addIdentity(path);
//		Session session = jsch.getSession(sftp.getUserName(), sftp.getSendPath(), Integer.parseInt(sftp.getPort()));
//		session.setPassword(sftp.getUserPwd());
//
//		Properties config = new Properties();
//		config.put("StrictHostKeyChecking", "no");
//		session.setConfig(config);
//		logger.debug("创建session对象，连接服务器：" + session);
//		session.connect();
//		logger.info("服务器连接成功：" + session);
//		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
//		logger.debug("打开sftp目录：" + channelSftp);
//		channelSftp.connect();
//		logger.info("目录访问成功：" + channelSftp);
//		channelSftp.cd("/");
//		logger.debug("定位目录到：");
//		try {
//
//			Vector<?> vector = channelSftp.ls("/");
//			System.out.println(vector);
//			logger.info("sina服务器目录:");
//			for (Object obj : vector) {
//				if (obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
//					String fileName = ((com.jcraft.jsch.ChannelSftp.LsEntry) obj).getFilename();
//					logger.info(fileName + "\r\n");
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("访问异常");
//		} finally {
//			channelSftp.quit();
//			logger.debug("sftp关闭");
//			session.disconnect();
//			logger.debug("session关闭");
//		}

		return name;
	}
}
