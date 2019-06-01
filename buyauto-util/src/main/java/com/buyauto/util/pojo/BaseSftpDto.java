package com.buyauto.util.pojo;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public class BaseSftpDto implements Serializable {

	@Value("${stfp.host}")
	private String host;
	@Value("${stfp.userName}")
	private String userName;
	@Value("${stfp.userPwd}")
	private String userPwd;
	@Value("${stfp.port}")
	private String port;
	@Value("${stfp.filePath}")
	private String filePath;
	@Value("${sftp.loadPath}")
	private String loadPath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLoadPath() {
		return loadPath;
	}

	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseSftpDto [host=" + host + ", userName=" + userName
				+ ", userPwd=" + userPwd + ", port=" + port + ", filePath="
				+ filePath + ", loadPath=" + loadPath + "]";
	}

}
