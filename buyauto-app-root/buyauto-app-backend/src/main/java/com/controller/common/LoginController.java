package com.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.business.service.sys.api.ISysEngineApi;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;

@RestController
@RequestMapping(UrlRegulation.SecurityPrefix.HTTP + UrlRegulation.RequestPrefix.REQ_NO_LOGIN
		+ UrlRegulation.PrefixBackendBusiness.LOGIN)
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private ISysEngineApi sysEngine;

	@RequestMapping("")
	@ResponseBody
	public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse res) {
		return new ModelAndView("/background_login/backgournd_login");
	}

	@RequestMapping("/doLogin")
	@ResponseBody
	public ModelAndView login(HttpServletRequest request, HttpServletResponse res, HttpSession session,
			@RequestParam("userName") String userName, @RequestParam("userPwd") String userPwd) {
		ModelAndView mv = new ModelAndView("redirect:/h/l/i");
		int code = sysEngine.backendUserLogin(userName, userPwd, session);
		if (code == CommonCode.SysRuleLogin.SUCCESS) {
			return mv;
		}
		return new ModelAndView("/background_login/backgournd_login", "code", code);
	}

	@RequestMapping("/logout")
	@ResponseBody
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse res, HttpSession session) {
		session.removeAttribute(UserSessionInfoBackend.SessionKey);
		return new ModelAndView("/background_login/backgournd_login");
	}
	
}
