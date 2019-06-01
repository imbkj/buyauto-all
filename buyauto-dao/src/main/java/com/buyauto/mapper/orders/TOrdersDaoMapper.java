package com.buyauto.mapper.orders;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.dao.orders.TOrdersDao;
import com.buyauto.entity.orders.TOrders;

public interface TOrdersDaoMapper {
	//根据id获取订单详情
    TOrdersDao selectByPrimaryKey(Long id);
    
	//后台查询订单总数
	int selectOrdersCount(
			@Param("userId") Long userId,
			@Param("companyId") Long companyId,
			@Param("orderId") Long orderId,
			@Param("startTime") Date strStartDates,
			@Param("endTime") Date strEndDates, @Param("state") Integer state);
	
	//后台查询订单列表
	List<TOrdersDao> selectordersList(
			@Param("userId") Long userId,
			@Param("companyId") Long companyId,
			@Param("orderId") Long orderId,
			@Param("startNumber") int startNumber,
			@Param("endNumber") int endNumber,
			@Param("startTime") Date strStartDates,
			@Param("endTime") Date strEndDates, 
			@Param("state") Integer state);
	
	/*前台*/
	
	//查询订单总数(用于前台订单搜索)
	int searchOrdersCountPc(
			@Param("userId") Long userId,
			@Param("companyId") Long companyId,
			@Param("state") Integer state,
			@Param("searchTitle") String searchTitle);
	
	//查询订单列表(用于前台订单搜索)
	List<TOrdersDao> searchOrdersPc(
			@Param("userId") Long userId,
			@Param("companyId") Long companyId,
			@Param("startNumber") int startNumber,
			@Param("endNumber") int endNumber,
			@Param("state") Integer state,
			@Param("searchTitle") String searchTitle);
	
	//查询车辆品牌名称
	String getCarBrandName(@Param("brandId") Long brandId);
	
	Long getCarIdByCarNo(@Param("carNo") String carNo);

	//查询供应商下的订单列表
	List<TOrdersDao> searchSupplierOrdersPc(@Param("id")Long id, @Param("companyId")Long companyId,@Param("pageStartNumber") int pageStartNumber,@Param("pageEndNumber") int pageEndNumber,@Param("state") Integer state, @Param("searchTitle")String searchTitle);

	Integer searchSupplierOrdersPcCount(@Param("id")Long id, @Param("companyId")Long companyId,@Param("state") Integer state, @Param("searchTitle")String searchTitle);

	
}