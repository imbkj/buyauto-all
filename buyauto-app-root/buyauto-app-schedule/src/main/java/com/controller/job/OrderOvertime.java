package com.controller.job;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.time.api.IBaseTimerApi;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.TimeSetting;
import com.controller.base.IJob;
import com.controller.time.OrderOvertimeTask;
import com.controller.time.RunTimer;

@Service("OrderOvertimeJob")
public class OrderOvertime implements IJob {

	private final static Logger logger = LoggerFactory
			.getLogger(OrderOvertime.class);

	@Autowired
	private IBaseTimerApi baseApi;

	/* 随机休眠器 */
	private final static Random randomSleep = new Random();

	@Override
	public void execute() {
		// 进行随机休眠，避免锁循环
		try {
			Thread.sleep(randomSleep.nextInt(10000));
		} catch (InterruptedException e) {
			// 系统原因或者中断原因，直接返回
			logger.error(e.getMessage(), e);
			return;
		}

		// 取得所有超时订单
		List<TOrders> tCommodityOrder = baseApi.queryOrderOvertimeList();
		if(tCommodityOrder.size()!=0){
			for (TOrders tCommodityOrders : tCommodityOrder) {
				RunTimer.setTimer(new OrderOvertimeTask(baseApi, tCommodityOrders.getCreateDate(), tCommodityOrders.getId()));
			}
			RunTimer.runTask();
		}
		
	}

}
