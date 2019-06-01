package com.business.service.index.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.index.api.BackendIndexApi;
import com.buyauto.dao.products.TProductsInputPojo;
import com.buyauto.mapper.index.backendIndexMapper;
import com.google.common.collect.Maps;

@Service
public class BackendIndexImpl implements BackendIndexApi {
	@Autowired
	private backendIndexMapper indexMapper;

	/**
	 * 达时报
	 */
	@Override
	public Map<String, Object> getIndexReport() {
		Map<String, Object> map = Maps.newHashMap();
		
		int ordersToday = indexMapper.ordersToday(); //今日订单数
		BigDecimal totalDepositToday = indexMapper.totalDepositToday();	//今日定金成交总金额
		BigDecimal totalTailMoneyToday = indexMapper.totalTailMoneyToday();	//今日尾款成交总金额
		
		BigDecimal totalAmountToday = indexMapper.totalAmountToday();	//今日成交总金额
		Integer ordersReadyPaied = indexMapper.orderPaymentToday();	//代付款订单数
		Integer productsInsale = indexMapper.productsInSale();	//出售中的商品数
		Integer readyTakeCar = indexMapper.readyTakeCar(); //等待交车的订单数量
		
		map.put("ordersToday", ordersToday);
		map.put("totalDepositToday", totalDepositToday);
		map.put("totalTailMoneyToday", totalTailMoneyToday);
		map.put("totalAmountToday", totalAmountToday);
		map.put("ordersReadyPaied", ordersReadyPaied);
		map.put("productsInsale", productsInsale);
		map.put("readyTakeCar", readyTakeCar);
		
		return map;
	}

}
