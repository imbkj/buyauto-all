package com.external.orders.api;


import java.math.BigDecimal;
import java.util.List;

import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.BaseSftpDto;
import com.buyauto.util.pojo.PageVo;

public interface ITOrdersPcApi {
	
	/**
	 * 更改订单状态
	 * @param id 订单id
	 * @param status 要修改的状态
	 * @param oldStatus 订单员状态
	 * @param orderPayStatus 支付状态
	 * @return
	 */
	Integer orderChg(Long id,Integer status,Integer oldStatus,Integer orderPayStatus);
	
	/**
	 * 查询我的订单
	 * @param userId 用户id
	 * @param companyId 经销商id
	 * @param pageNumber 分页
	 * @param pageSize
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param state
	 * @return
	 */
	public PageVo<TOrdersDao> ordersList(Long userId,Long companyId,int pageNumber, int pageSize, Integer state,String searchTitle);
	/**
	 * 订单详情
	 * @param id
	 * @return
	 */
	public TOrdersDao getOrderByid(Long id);
	/**
	 * 订单凭证上传
	 * @param orderId
	 * @param type
	 * @param depositBackImageH 图片标识
	 * @param depositBackImageOld 旧图片
	 * @return
	 */
	Boolean addOrdersFiles(Long orderId,Integer type,String depositBackImageH,String depositBackImageOld);
	
	/**
	 * 订单凭证查询
	 * @param orderId 订单id
	 * @param type 凭证类型
	 * @return
	 */
	List<TOrdersFiles> queryOrdersFiles(Long orderId,Integer type);

	/**
	 * 订单确认提交
	 * @param tProductsPojo
	 * @param extractPerson
	 * @param sysConfigId
	 * @param remarks
	 * @param extractDate
	 * @param distribution 
	 * @param distribution2 
	 * @param userId 
	 * @param insuranceType 
	 */
	TOrders orderSubmit(TProductsPojo tProductsPojo, String extractPerson, Long sysConfigId, String remarks, String extractDate, String distribution, String distribution2, Long userId, Integer insuranceType);
	/**
	 * 销售人员下拉框
	 * @param companyId
	 * @return
	 */
	List<TUsers> getUsersByCompanyId(Long companyId);
	/**
	 * 获取审核原因
	 * @param orderId 订单编号
	 * @return
	 */
	public TAuditing getCheckInfo(Long orderId);
	/*
	 * 生成CSV文件并发送给元初
	 */
	public boolean createOrderCsv(Long orderID,Integer status,BaseSftpDto ftpDto);
	
	/**
	 * 前台销售取消订单
	 */
	int cancelOrderBySeller(Long orderId,String cancelReason);
	/**
	 * 查找后台审核人员
	 * @return
	 */
	public List<SysRuleUser> getUsersForCheck();
	
	public Boolean modifyOrder(Long orderId,Integer status);

	void saveOrderFinance(TOrders tOrders, Integer term, String phone, BigDecimal carPrice);

	TOrderFinance getOrderFinanceByid(Long orderId);

	/**
	 * 查询经销商订单
	 * @param id
	 * @param companyId
	 * @param pageNumber
	 * @param pageSize
	 * @param state
	 * @param searchTitle
	 * @return
	 */
	PageVo<TOrdersDao> supplierOrdersList(Long id, Long companyId, Integer pageNumber, Integer pageSize, Integer state, String searchTitle);
}
