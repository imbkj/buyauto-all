package com.controller.carConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.util.web.UrlRegulation;

@RestController
@RequestMapping(value = UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_LOGIN
		+ UrlRegulation.BizPrefix.CONFIG)
public class CarConfigController {
	private static final Logger logger = LoggerFactory.getLogger(CarConfigController.class);

	@RequestMapping(value = "/carConfig")
	public ModelAndView carConfig(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("car/carConfig");
		return modelAndView;
	}

}
