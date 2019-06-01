package com.buyauto.mapper.index;

import java.math.BigDecimal;

/**
 * 后台达时报
 * @author whz
 *
 * 2017年5月26日
 */
public interface backendIndexMapper {
	//获得今日订单数
	int ordersToday();
	//今日成交总金额
	BigDecimal totalAmountToday();
	//等待支付定金的订单数
	int orderPaymentToday();
	//出售中的商品
	int productsInSale();
	//等待交车的订单数量
	int readyTakeCar();
	//今日定金成交金额
	BigDecimal totalDepositToday();
	//今日尾款成交金额
	BigDecimal totalTailMoneyToday();
}
