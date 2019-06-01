package com.buyauto.util.pojo;

import org.springframework.beans.factory.annotation.Value;

public class TimeSetting {
	
	//定金超时时间
	@Value("${timeSetting.depositTimeOut}")
	private String depositTimeOut;

	public String getDepositTimeOut() {
		return depositTimeOut;
	}

	public void setDepositTimeOut(String depositTimeOut) {
		this.depositTimeOut = depositTimeOut;
	}
	
	

}
