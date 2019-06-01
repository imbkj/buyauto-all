package com.controller.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.entity.user.TUsers;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.web.UrlRegulation;

public class BuyautoPcInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(BuyautoPcInterceptor.class);

	@Autowired
	private TUsersMapper userMapper;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		/* 登录拦截验证 */
		UserSessionInfo usrSessionInfo = SessionUtil.getUserSessionInfoFromPc(request.getSession());
		/*
		 * UserSessionInfo usrSessionInfo = (UserSessionInfo)
		 * request.getSession().getAttribute( UserSessionInfo.SessionKey);
		 */
		if (requestURI.contains(UrlRegulation.RequestPrefix.REQ_LOGIN + "/")) {
			if (SessionUtil.getUserSessionInfoFromPc(request.getSession()) == null) {
				logger.debug("用户未登录，即将重定向至登陆页面");
				response.sendRedirect(request.getContextPath() + UrlRegulation.SecurityPrefix.HTTP
						+ UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.BizPrefix.USER
						+ UrlRegulation.BizPrefix.LOGIN);
				return false;
			} else {
				if (usrSessionInfo == null) {
					return true;
				}
				
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();
				Set<Long> userRules = usrSessionInfo.getAuthorities();
				if (method.isAnnotationPresent(AuthAnno.class)) {
					AuthAnno authAnno = method.getAnnotation(AuthAnno.class);
					AuthBackend[] rules = authAnno.rules();
					if (rules == null || rules.length < 1) {
						return true;
					}
					if (userRules == null || userRules.size() < 1) {
						// handlerAccessError(handlerMethod, response);
						// logger.info("权限拒绝！userId：" + usrSessionInfo.getId());
						logger.debug("用户权限不足 重新定向到友好页面");
						response.sendRedirect(request.getContextPath() + "/error/notPermission");
						return false;
					}
					for (int i = 0; i < rules.length; i++) {
						if (!userRules.contains(rules[i].getValue())) {
							// handlerAccessError(handlerMethod, response);
							// logger.info("权限拒绝！userId：" +
							// usrSessionInfo.getId() + " 权限：" +
							// rules[i].getValue());
							logger.debug("用户权限不足 重新定向到友好页面");
							response.sendRedirect(request.getContextPath() + "/error/notPermission");
							return false;
						}
					}
					return true;
				} else {
					return true;
				}
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (modelAndView != null) {
			String viewName = modelAndView.getViewName();
			if (!StringUtils.isEmpty(viewName) && !viewName.startsWith("redirect:")) {
				UserSessionInfo userSessionInfo = SessionUtil.getUserSessionInfoFromPc(request.getSession());
				System.out.println(userSessionInfo);
				if (userSessionInfo != null) {
					long uId = userSessionInfo.getId();
					TUsers mermbersInfo = userMapper.selectByPrimaryKey(uId);
					modelAndView.addObject("mermbersInfo", mermbersInfo);
					modelAndView.addObject("userSessionInfo",
							SessionUtil.getUserSessionInfoFromPc(request.getSession()));
				}
			}
		}
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	private void handlerAccessError(HandlerMethod handlerMethod, HttpServletResponse resp) throws IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = resp.getWriter();
		writer.write("{'温馨提示':'当前用户无权限！'}");
		writer.flush();
		writer.close();
	}

}
