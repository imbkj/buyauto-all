package com.business.service.orders.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.FinanceDto;
import com.buyauto.util.pojo.PageVo;

public interface ITOredersApi {
	
	Integer insertOrders(TOrders orders);
	
	Integer deleteOrders(Long id);
	
	Integer updateOrders(TOrders orders);
	
	TOrdersDao getOrdersById(Long id);
	
	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param state 订单状态
	 * @return
	 */
	PageVo<TOrdersDao> queryOrders(Long userId,Long companyId,Long orderId,int pageNumber, int pageSize,
			String strStartDate, String strEndDate, Integer state);
	
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
	 * 经销商列表
	 * @return
	 */
	public List<TUsers> companyList();
	/**
	 * 修改车辆状态（发货）
	 * @param carId
	 * @return
	 */
	Boolean changeCarStatus(Long carId);
	/**
	 * 分页查询
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param strStartDate 开始日期
	 * @param strEndDate 结束日期
	 * @param state 订单状态
	 * @return
	 */
	PageVo<TOrdersDao> ordersPcList(Long userId,Long companyId,int pageNumber, int pageSize,Integer state,String searchTitle);
	/**
	 * 审核取消订单
	 * @param result
	 * @param type
	 * @param id
	 * @param operName
	 * @param operId
	 * @param info
	 * @return
	 */
	int cancelOrders(Integer result,Integer type,Long id,String operName,Long operId,String info);
	
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
	TOrders orderSubmit(TProductsPojo tProductsPojo, String extractPerson, Long sysConfigId, String remarks, String extractDate ,String deliveryMode, String distribution, Long userId, Integer insuranceType);
	/**
	 * 前台销售人员下拉框
	 * @param companyId
	 * @return
	 */
	List<TUsers> getUsersByCompanyId(Long companyId);
	
	/**
	 * 修改订单状态（自动确认）
	 * @param orderId
	 * @param status
	 * @return
	 */
	Boolean modifyOrder(String carNo,Integer status);
	
	/**
	 * 前台销售取消订单
	 */
	int cancelOrderBySeller(Long orderId,String cancelReason);
	/*
	 * 车辆三证上传
	 */
	Boolean uploadCarTh(String carNo, List<String> orderFiles);
	
	/*
	 * 获取车架号
	 */
	String getCarNo(Long carId);
	
	TOrders getOrderById(Long orderId);
	
	TOrders getOrderByCarNo(String carNo);

	Map<String, Object> carFinanceCalculate(BigDecimal amount, Integer term, FinanceDto financeDto);

	void saveOrderFinance(TOrderFinance tOrderFinance);

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
