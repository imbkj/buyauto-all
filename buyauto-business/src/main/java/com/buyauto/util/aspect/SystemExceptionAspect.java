package com.buyauto.util.aspect;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buyauto.entity.log.LogErrorAction;
import com.buyauto.mapper.log.LogErrorActionMapper;
import com.buyauto.util.method.KeyUtil;
import com.buyauto.util.method.SessionUtil;
import com.buyauto.util.method.SpringContextHolder;
import com.buyauto.util.pojo.UserSessionInfo;
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
public class SystemExceptionAspect {

	private static final Logger logger = LoggerFactory.getLogger(SystemExceptionAspect.class);

	private static final String[] handlerName = new String[] { "org.springframework.web.servlet.HandlerInterceptor" };
	private static final String[] fileName = new String[] { "preHandle", "postHandle", "afterCompletion", "initBinder" };

	@Around(value = "execution(* com.business..*.*(..))")
	public Object exinfoBusiness(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
			exinfo(proceedingJoinPoint, result);
		} catch (Exception e) {
			e.printStackTrace();
			exerror(proceedingJoinPoint, e);
		}
		return result;
	}

	@Around(value = "execution(* com.controller..*.*(..))")
	public Object exinfoController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
			exinfo(proceedingJoinPoint, result);
		} catch (Exception e) {
			e.printStackTrace();
			exerror(proceedingJoinPoint, e);
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
		if (isNext) {
			result = result != null ? result.toString() : "null";
			logger.info("@请求信息：目标方法为：【" + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "."
					+ proceedingJoinPoint.getSignature().getName() + "】" + "\r\n				" + "-@请求信息：目标参数为：【"
					+ Arrays.toString(proceedingJoinPoint.getArgs()) + "】" + "\r\n				" + "-@请求信息：回调结果为：【" + result
					+ "】");
		}
	}

	public void exerror(JoinPoint jp, Throwable e) {

		logger.error("@异常错误：目标方法为：" + jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName()
				+ "\r\n				" + "-@异常错误：目标参数为：【" + Arrays.toString(jp.getArgs()) + "】");

		LogErrorActionMapper errorMapper = SpringContextHolder.getBean(LogErrorActionMapper.class);

		LogErrorAction action = new LogErrorAction();

		Object[] args = jp.getArgs();
		HttpServletRequest request = null;
		// 通过分析aop监听参数分析出request等信息
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest) {
				request = (HttpServletRequest) args[i];
			}
		}
		action.setId(KeyUtil.generateDBKey());
		if (request != null) {
			UserSessionInfo pcUser = SessionUtil.getUserSessionInfoFromPc(request.getSession());
			UserSessionInfoBackend backendUser = SessionUtil.getUserSessionInfoFromBackend(request.getSession());
			if (pcUser != null) {
				action.setUserId(pcUser.getId());
			} else if (backendUser != null) {
				action.setUserId(backendUser.getId());
			}

			if (request.getHeader("x-forwarded-for") == null) {
				action.setRequestIp(request.getRemoteAddr());
			} else {
				action.setRequestIp(request.getHeader("x-forwarded-for"));
			}
		}

		action.setAction(jp.getSignature().getDeclaringTypeName() + "." + jp.getSignature().getName());
		action.setNotes("errorlog:【" + e.getMessage() + "】" + "arg:【" + Arrays.toString(jp.getArgs()) + "】");

		action.setCreateTime(new Date());
		errorMapper.insert(action);

	}
}
