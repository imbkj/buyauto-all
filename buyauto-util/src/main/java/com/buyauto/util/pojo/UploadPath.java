package com.buyauto.util.pojo;

import org.springframework.beans.factory.annotation.Value;

public class UploadPath {
	@Value("${UploadPath.rootPath}")
	private String rootPath;

	@Value("${UploadPath.showPath}")
	private String showPath;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getShowPath() {
		return showPath;
	}

	public void setShowPath(String showPath) {
		this.showPath = showPath;
	}

}
