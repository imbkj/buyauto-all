package com.business.service.orders.api;

public interface ITCommissionApi {

	/** 
	* @Title: commCalculate 
	* @Description: 计算佣金
	* @param orderId
	* @return Integer    返回类型 
	*/
	Integer commCalculate(String orderId);
	
	
}
