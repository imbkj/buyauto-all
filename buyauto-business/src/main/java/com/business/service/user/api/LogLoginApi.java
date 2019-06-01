package com.business.service.user.api;

import com.buyauto.entity.log.LogLogin;

/**
 * 登录日志
 */
public interface LogLoginApi {

	/**
	 * 新增登录日志
	 * @param logLogin
	 * @return
	 */
	public int insert(LogLogin logLogin);

	/**
	 * 根据登录名进行查找登录次数
	 * @param phone
	 * @return
	 */
	public Integer findCountByAccount(String account);
}
