package com.buyauto.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.buyauto.dao.sys.SysRuleUserDao;

public interface SysRuleUserDaoMapper {
	
	List<SysRuleUserDao> queryRuleUser(@Param("isTrue") Integer isTrue,
	    		@Param("startNumber") int startNumber,@Param("endNumber") int endNumber);
	    
    int queryRuleUserCount(@Param("isTrue") int isTrue);
}