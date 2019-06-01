package com.controller.commission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.business.service.orders.api.ITCommissionApi;
import com.buyauto.util.web.UrlRegulation;

/**
 * 返佣信息
 */
@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.COMMISSION)
public class CommissionController {

	private static final Logger logger = LoggerFactory.getLogger(CommissionController.class);

	@Autowired
	private ITCommissionApi tCommissionImpl;

	/**
	 * @Title: commCalculate
	 * @Description: 调用计算佣金
	 * @param request
	 * @param response
	 * @param orderId
	 * @return String    返回类型
	 */
	@RequestMapping(value = "/commCalculate", method=RequestMethod.POST)
	public String commCalculate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "orderId", required = true) String orderId) {
		Integer c = tCommissionImpl.commCalculate(orderId);
		return c.toString();
	}

}
