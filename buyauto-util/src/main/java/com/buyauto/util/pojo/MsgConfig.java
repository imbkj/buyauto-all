package com.buyauto.util.pojo;

import org.springframework.beans.factory.annotation.Value;

public class MsgConfig {

	@Value("${RealSend}")
	private boolean realSend;

	public boolean isRealSend() {
		return realSend;
	}

	public void setRealSend(boolean realSend) {
		this.realSend = realSend;
	}

	@Override
	public String toString() {
		return "MsgConfig [realSend=" + realSend + "]";
	}

}
