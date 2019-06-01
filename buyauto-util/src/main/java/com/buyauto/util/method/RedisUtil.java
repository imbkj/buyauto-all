package com.buyauto.util.method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Redis工具类
 */
public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);
	
	/**
	 * 保存到Redis缓存
	 * @param key 键
	 * @param value 值
	 */
	public static void setRedis(String key, String value) {
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		logger.info("Redis缓存保存数据。key:"+key+" value:"+value);
		opsForValue.set(key, value);
	}
	
	/**
	 * 用户登录,保存到Redis缓存,key自动生成并返回
	 * @param value 值
	 */
	public static String setRedis(String value) {
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String key = CommonCode.LoginState.TOKEN+KeyUtil.generateDBKey();//生成唯一键
		opsForValue.set(key, value);
		logger.info("用户登录保存Redis缓存。key:"+key+" value:"+value);
		return key;
	}
	
	/**
	 * 根据KEY获取对应的值
	 * @param key
	 * @return
	 */
	public static String getRedisByKey(String key) {
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String string = opsForValue.get(key);
		logger.info("获取Redis缓存数据。key:"+key+" value:"+string);
		return string;
	}
	
	/**
	 * 根据KEY获取用户ID
	 * @param key
	 * @return
	 */
	public static Long getUserByKey(String key) {
		if(StringUtil.isEmpty(key))return null;
		StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
		ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
		String value = opsForValue.get(key);
		if(StringUtil.isEmpty(value)){
			logger.info("Redis获取用户ID。key:"+key+" value为空！ value:"+value);
			return null;
		}
		Long id = null;
		try {
			id = Long.valueOf(value);
		} catch (Exception e) {
			logger.info("Redis获取用户ID转换Long出错!ID:"+value);
			return null;
		}
		logger.info("Redis获取用户ID。key:"+key+" value:"+value);
		return id;
	}
	
}
