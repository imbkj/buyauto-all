package com.buyauto.util.core.tool.buyautoenum;

public enum JobFlagEnum {

	CONTRACT_ONLINE_DOWN("contractOnlineDown", "合同自动下线"),
	ORDER_OVERTIME("OrderOvertime","订单定金支付超时"),
	SEARCH_PRODUCT("searchProduct","查找产品并更新"),
	ORDER_AUTOTAKECAR("autoTakeCar","自动收车"),
	USER_UPGRADE("userUpgrade","用户升级"),
	USER_DOWNGRADE("userDowngrade","用户降级");
	

	private JobFlagEnum(String jobType, String jobDesc) {
		this.jobType = jobType;
		this.jobDesc = jobDesc;
	}

	/* job类型，例如对账job */
	private String jobType;

	/* 对job的描述 */
	private String jobDesc;

	public String getJobType() {
		return jobType;
	}

	public String getJobDesc() {
		return jobDesc;
	}

}