package com.buyauto.util.aspect;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buyauto.entity.log.LogAdminAction;
import com.buyauto.mapper.log.LogAdminActionMapper;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.SpringContextHolder;
import com.buyauto.util.pojo.UserSessionInfoBackend;

/**
 * 
 * @ClassName: SystemExceptionAspect
 * @Description: 系统切面，异常和日志处理
 * @author cxz
 * @date 2016年11月23日 下午5:46:26
 *
 */
@Aspect
public class BackendOptionsAspect {

	private static final Logger logger = LoggerFactory.getLogger(BackendOptionsAspect.class);

	private static final String[] handlerName = new String[] { "org.springframework.web.servlet.HandlerInterceptor" };
	private static final String[] fileName = new String[] { "preHandle", "postHandle", "afterCompletion" };

	@Around(value = "execution(* com.controller..*.*(..))")
	public Object exinfoController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();

			exinfo(proceedingJoinPoint, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void exinfo(ProceedingJoinPoint proceedingJoinPoint, Object result) {

		boolean isNext = true;
		for (String string : handlerName) {
			if (string.indexOf(proceedingJoinPoint.getSignature().getDeclaringTypeName()) < 0) {
				isNext = true;
			}
		}
		for (String string : fileName) {
			if (string.equals(proceedingJoinPoint.getSignature().getName())) {
				isNext = false;
			}
		}
		Object[] args = proceedingJoinPoint.getArgs();
		HttpServletRequest request = null;
		// 通过分析aop监听参数分析出request等信息
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
			}
		}
		if (request != null) {
			UserSessionInfoBackend pcUser = SessionUtil.getUserSessionInfoFromBackend(request.getSession());

			LogAdminActionMapper errorMapper = SpringContextHolder.getBean(LogAdminActionMapper.class);
			LogAdminAction action = new LogAdminAction();
			action.setId(KeyUtil.generateDBKey());
			if (isNext) {
				result = result != null ? result.toString() : "null";
				if (pcUser != null) {
					action.setOperId(pcUser.getId() + "");
					action.setUserName(pcUser.getName());

					if (request.getHeader("x-forwarded-for") == null) {
						action.setRequestIp(request.getRemoteAddr());
					} else {
						action.setRequestIp(request.getHeader("x-forwarded-for"));
					}
					action.setClassPath(proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
							+ proceedingJoinPoint.getSignature().getName());
					action.setCreateDate(new Date());
					errorMapper.insert(action);
					logger.debug("@@用户日志：【" + pcUser.getId() + "】- " + pcUser.getName() + "：【"
							+ proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
							+ proceedingJoinPoint.getSignature().getName() + "】" + "\r\n				" + "-@请求信息：目标参数为：【"
							+ Arrays.toString(proceedingJoinPoint.getArgs()) + "】" + "\r\n				" + "-@请求信息：回调结果为：【"
							+ result + "】");
				}
			}
		}
	}
}
