package com.business.service.orders.api;

import java.util.List;
import java.util.Map;

import com.buyauto.util.pojo.BaseSftpDto;

public interface ITOrdersCSVApi {

	/*
	 * 生成CSV文件并发送给元初
	 */
	public boolean createOrderCsv(Long orderID,Integer status,BaseSftpDto ftpDto);
	
	/*
	 * 取消订单
	 */
	public void createOrderCancelCsv(Long orderID,BaseSftpDto ftpDto);
	
	public void createCarResultCsv(List exportData,BaseSftpDto ftpDto);
	public void createOrderResultCsv(List exportData,BaseSftpDto ftpDto);
	/*
	 * 查询FTP返回结果
	 */
	public List<Map<String, Object>> queryFTPResult(BaseSftpDto ftpDto) throws Exception;
}
