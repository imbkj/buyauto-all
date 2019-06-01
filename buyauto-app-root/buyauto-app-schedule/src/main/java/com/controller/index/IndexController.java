package com.controller.index;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.controller.job.ContractDemo;
import com.controller.job.OrderOvertime;
import com.controller.time.OrderOvertimeTask;

@RestController
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	
	@Autowired
	private ContractDemo contractDemo;
	
	@Autowired
	private OrderOvertime  orderOvertime;
	
	@RequestMapping(value = "/")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		contractDemo.execute();
		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());
		return modelAndView;
	}
	
	@RequestMapping(value = "/1")
	public ModelAndView index1(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		orderOvertime.execute();
		ModelAndView modelAndView = new ModelAndView("index", "time", new Date());
		return modelAndView;
	}
}
