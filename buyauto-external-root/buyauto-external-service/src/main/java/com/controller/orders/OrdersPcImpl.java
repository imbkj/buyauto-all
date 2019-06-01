package com.controller.orders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.audit.api.TAuditingApi;
import com.business.service.orders.api.ITOrdersCSVApi;
import com.business.service.orders.api.ITOredersApi;
import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.dao.products.TProductsPojo;
import com.buyauto.entity.audit.TAuditing;
import com.buyauto.entity.orders.TOrderFinance;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.orders.TOrdersFiles;
import com.buyauto.entity.sys.SysRuleUser;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.pojo.BaseSftpDto;
import com.buyauto.util.pojo.PageVo;
import com.external.orders.api.ITOrdersPcApi;

@Service("ordersPcImpl")
public class OrdersPcImpl implements ITOrdersPcApi {

	@Autowired
	private ITOredersApi ordersApi;
	
	@Autowired
	private TAuditingApi auditingApi;
	
	@Autowired
	private ITOrdersCSVApi ordersCSVApi;
	
	@Autowired
	private ISysEngineApi engineApi;
	
	
	/**
	 * 查询列表
	 */
	@Override
	public PageVo<TOrdersDao> ordersList(Long userId, Long companyId,
			int pageNumber, int pageSize,Integer state,String searchTitle) {
		return ordersApi.ordersPcList(userId, companyId, pageNumber,pageSize, state,searchTitle);
	}
	/**
	 * 订单详情
	 */
	@Override
	public TOrdersDao getOrderByid(Long id) {
		return ordersApi.getOrdersById(id);
	}
	/**
	 * 订单凭证上传
	 */
	@Override
	public Boolean addOrdersFiles(Long orderId, Integer type,
			String depositBackImageH, String depositBackImageOld) {
		return ordersApi.addOrdersFiles(orderId, type, depositBackImageH, depositBackImageOld);
	}
	/**
	 * 查看订单凭证
	 */
	@Override
	public List<TOrdersFiles> queryOrdersFiles(Long orderId, Integer type) {
		return ordersApi.queryOrdersFiles(orderId, type);
	}
	/**
	 * 更该订单状态
	 */
	@Override
	public Integer orderChg(Long id, Integer status, Integer oldStatus,
			Integer orderPayStatus) {
		return ordersApi.orderChg(id, status, oldStatus, orderPayStatus);
	}
	@Override
	public TOrders orderSubmit(TProductsPojo tProductsPojo, String extractPerson, Long sysConfigId, String remarks, String extractDate,String deliveryMode, String distribution,Long userId,Integer insuranceType) {
		return ordersApi.orderSubmit(tProductsPojo,extractPerson,sysConfigId,remarks,extractDate,deliveryMode,  distribution,userId,insuranceType);
	}
	/**
	 * 销售人员列表
	 */
	@Override
	public List<TUsers> getUsersByCompanyId(Long companyId) {
		return ordersApi.getUsersByCompanyId(companyId);
	}
	/**
	 * 获取审核结论
	 */
	@Override
	public TAuditing getCheckInfo(Long orderId) {
		return auditingApi.getCheckInfo(orderId);
	}
	
	/* 调用元初接口确认订单 
	 * @see com.external.orders.api.ITOrdersPcApi#createOrderCsv(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public boolean createOrderCsv(Long orderID, Integer status,BaseSftpDto ftpDto) {
		return ordersCSVApi.createOrderCsv(orderID, status,ftpDto);
	}
	/*
	 * 经销商销售取消订单
	 * @see com.external.orders.api.ITOrdersPcApi#cancelOrderBySeller(java.lang.Long, java.lang.String)
	 */
	@Override
	public int cancelOrderBySeller(Long orderId, String cancelReason) {
		return ordersApi.cancelOrderBySeller(orderId, cancelReason);
	}
	@Override
	public List<SysRuleUser> getUsersForCheck() {
		return engineApi.getUsersForCheck();
	}
	@Override
	public Boolean modifyOrder(Long orderId, Integer status) {
		Boolean isNext = ordersApi.orderChg(orderId, status, null, null) == CommonCode.DBSuccess.Success;
		return isNext;
	}
	@Override
	public void saveOrderFinance(TOrders tOrders, Integer term,String phone, BigDecimal carPrice) {
		TOrderFinance tOrderFinance = new TOrderFinance();
		tOrderFinance.setId(KeyUtil.generateDBKey());
		tOrderFinance.setUserId(tOrders.getUserId());
		tOrderFinance.setUserPhone(phone);
		tOrderFinance.setLoanAmount(carPrice);
		tOrderFinance.setOrderId(tOrders.getId());
		tOrderFinance.setStatus(CommonCode.SalesStatus.WAITPASS);
		tOrderFinance.setTerm(term);
		tOrderFinance.setCreateDate(new Date());
		ordersApi.saveOrderFinance(tOrderFinance);
	}
	@Override
	public TOrderFinance getOrderFinanceByid(Long orderId) {
		return ordersApi.getOrderFinanceByid(orderId);
	}
	@Override
	public PageVo<TOrdersDao> supplierOrdersList(Long id, Long companyId, Integer pageNumber, Integer pageSize, Integer state, String searchTitle) {
		return ordersApi.supplierOrdersList(id, companyId, pageNumber,pageSize, state,searchTitle);
	}

}
