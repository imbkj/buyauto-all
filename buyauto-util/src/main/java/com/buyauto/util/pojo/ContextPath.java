package com.buyauto.util.pojo;

import org.springframework.beans.factory.annotation.Value;

public class ContextPath {
	
	@Value("${Request.ContextPathUrl}")
	private String contextPath;

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	
}
