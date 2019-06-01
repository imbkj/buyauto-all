package com.controller.index;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.MessageParam;
import com.google.common.collect.Maps;
import com.msg.sms.IMessageApi;

@RestController
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private IMessageApi messageApi;

	@RequestMapping(value = "/1")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());

		MessageParam messageParam = new MessageParam("18539592505", CommonCode.CmsType.Infomartion, "Register");
		Map<String, String> map = Maps.newHashMap();
		map.put("vCode", "8888");
		messageParam.setTemplateParam(map);
		messageApi.sendMessagel(messageParam);

		return modelAndView;
	}

	@RequestMapping(value = "/2")
	public ModelAndView index1(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());

		MessageParam messageParam = new MessageParam("501837145@qq.com", CommonCode.CmsType.Email, "OrdersEmail");
		Map<String, String> map = Maps.newHashMap();
		map.put("orderId", "20174546431345631");
		map.put("orderStatus", "待发货");
		messageParam.setTemplateParam(map);
		messageParam.setSubject("订单异常状况处理");
		messageApi.sendMessagel(messageParam);

		return modelAndView;
	}
}
