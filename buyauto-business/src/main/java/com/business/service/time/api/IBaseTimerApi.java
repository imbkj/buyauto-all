package com.business.service.time.api;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.products.TProductsTemp;
import com.buyauto.entity.user.TUsers;
import com.buyauto.util.pojo.BaseSftpDto;

public interface IBaseTimerApi {
	//定金支付超时的订单
	List<TOrders> queryOrderOvertimeList();

	//修改超时状态的订单
	boolean updateOrderOvertime(Long id);

	//获取FTP产品信息
	Map<String, File> queryFTPcarList(BaseSftpDto ftpDto);
	//获取三证信息
	void queryFTPCarLicense(BaseSftpDto ftpDto);

	//数据录入如product表
	void productDataInput();
	
	List<TOrders> getAutomaticTakeCarOrders();
	
	boolean updateAutomaticTakeCarOrders(Long orderId) throws ParseException;
	
	Map<String, List<TUsers>> queryUpGradeUserList();
	
	//用户实际等级升级
	boolean updateUserUpgrade(Long id);

	//用户实际等级降级
	boolean updateUserDowngrade(Long id);
	
}
