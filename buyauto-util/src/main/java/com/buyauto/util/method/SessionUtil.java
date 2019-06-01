package com.buyauto.util.method;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.buyauto.util.core.tool.web.GsonUtil;
import com.buyauto.util.pojo.UserSessionInfo;
import com.buyauto.util.pojo.UserSessionInfoBackend;

/**
 * 
 * @ClassName: SessionUtil
 * @Description: 用户session操作类
 * @author cxz
 * @date 2016年5月23日 下午3:39:37
 *
 */
public class SessionUtil {

	/**
	 * 
	 * @Title: addSessionKey
	 * @Description: 获取session相关key
	 * @param @param session
	 * @param @param key
	 * @param @param value 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void addSessionKey(HttpSession session, String key, String value) {
		System.out.println(session.getId() + "-" + key);
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(session.getId() + "-" + key, value, 10L, TimeUnit.MINUTES);
	}

	/**
	 * 
	 * @Title: addSessionKey
	 * @Description: 获取session相关key
	 * @param @param session
	 * @param @param key
	 * @param @param value 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static String getSessionKey(HttpSession session, String key) {
		System.out.println(session.getId() + "-" + key);
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String string = opsForValue.get(session.getId() + "-" + key);
		return string;
	}

	/**
	 *
	 * 添加前台用户session
	 * 
	 * @param session
	 * @param userSessionInfo
	 */
	public static void addUserSessionInfoIntoSessionFromPc(HttpSession session, UserSessionInfo userSessionInfo) {
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(session.getId(), GsonUtil.toJson(userSessionInfo), 30L, TimeUnit.MINUTES);
	}

	/**
	 * 获取前台用户session
	 * 
	 * @param session
	 * @return
	 */
	public static UserSessionInfo getUserSessionInfoFromPc(HttpSession session) {
		UserSessionInfo userSession = (UserSessionInfo) session.getAttribute(UserSessionInfo.SessionKey);
		if (userSession == null) {
			StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
			ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
			String sessionJson = opsForValue.get(session.getId());
			userSession = GsonUtil.fromJsonUnderScoreStyle(sessionJson, UserSessionInfo.class);
		}
		return userSession;
	}

	/**
	 * 前台用户退出
	 * @param session
	 */
	public static void delUserSessionInfoFromPc(HttpSession session) {
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(session.getId(), "null", 1, TimeUnit.MILLISECONDS);
		session.setAttribute(UserSessionInfo.SessionKey, null);
	}


	/**
	 *
	 * 添加后台用户session
	 * 
	 * @param session
	 * @param userSessionInfo
	 */
	public static void addUserSessionInfoIntoSessionFromBackend(HttpSession session, UserSessionInfoBackend userSessionInfo) {
		session.setAttribute(UserSessionInfoBackend.SessionKey, userSessionInfo);
	}

	/**
	 * 
	 * 获取后台用户session
	 * 
	 * @param session
	 * @return
	 */
	public static UserSessionInfoBackend getUserSessionInfoFromBackend(HttpSession session) {
		return (UserSessionInfoBackend) session.getAttribute(UserSessionInfoBackend.SessionKey);
	}

	/**
	 * 根据key获取session中对应数据
	 * 
	 * @param session
	 * @param key
	 * @return
	 */
	public static Object getAttr(HttpSession session, String key) {
		return session.getAttribute(key);
	}

}
