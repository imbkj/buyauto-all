package com.business.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.user.api.LogLoginApi;
import com.buyauto.entity.log.LogLogin;
import com.buyauto.mapper.log.LogLoginMapper;
import com.buyauto.util.method.KeyUtil;

/**
 * 登录日志
 */
@Service
public class LogLoginImpl implements LogLoginApi {

	@Autowired
	private LogLoginMapper logLoginMapper;
	
	/**
	 * 新增登录日志
	 */
	@Override
	public int insert(LogLogin logLogin) {
		if(logLogin.getId() == null){
			logLogin.setId(KeyUtil.generateDBKey());//主键
		}
		return logLoginMapper.insertSelective(logLogin);
	}

	/**
	 * 根据登录名进行查找登录次数
	 */
	@Override
	public Integer findCountByAccount(String account) {
		return logLoginMapper.findCountByAccount(account);
	}

}
