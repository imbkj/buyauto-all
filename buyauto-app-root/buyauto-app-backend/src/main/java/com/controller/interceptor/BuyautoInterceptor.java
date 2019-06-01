package com.controller.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.buyauto.util.anno.AuthAnno;
import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.pojo.UserSessionInfoBackend;
import com.buyauto.util.web.UrlRegulation;

public class BuyautoInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(BuyautoInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestURI = request.getRequestURI();
		if (requestURI.contains("login")) {
			return true;
		}
		UserSessionInfoBackend usrSessionInfo = (UserSessionInfoBackend) request.getSession().getAttribute(
				UserSessionInfoBackend.SessionKey);
		if (requestURI.contains(UrlRegulation.RequestPrefix.REQ_LOGIN + "/")) {
			if (usrSessionInfo == null) {
				response.sendRedirect(request.getContextPath() + UrlRegulation.SecurityPrefix.HTTP
						+ UrlRegulation.RequestPrefix.REQ_NO_LOGIN + UrlRegulation.PrefixBackendBusiness.LOGIN);
				return false;
			} else {
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
						logger.info("权限拒绝！userId：" + usrSessionInfo.getId());
						response.sendRedirect(request.getContextPath() + "/error/noAuth");
						return false;
					}
					for (int i = 0; i < rules.length; i++) {
						if (!userRules.contains(rules[i].getValue())) {
							// handlerAccessError(handlerMethod, response);
							logger.info("权限拒绝！userId：" + usrSessionInfo.getId() + " 权限：" + rules[i].getValue());
							response.sendRedirect(request.getContextPath() + "/error/noAuth");
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

	}

	private void handlerAccessError(HandlerMethod handlerMethod, HttpServletResponse resp) throws IOException {
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter writer = resp.getWriter();
		writer.write("{'温馨提示':'当前用户无权限！'}");
		writer.flush();
		writer.close();
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
