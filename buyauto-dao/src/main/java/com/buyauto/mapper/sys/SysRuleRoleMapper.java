package com.buyauto.mapper.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.buyauto.entity.sys.SysRuleRole;

public interface SysRuleRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    int insert(SysRuleRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    int insertSelective(SysRuleRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    SysRuleRole selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SysRuleRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sys_rule_role
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SysRuleRole record);
    
    List<SysRuleRole> queryRole(@Param("isEnable") int isEnable,
    		@Param("startNumber") int startNumber,@Param("endNumber") int endNumber);
    
    int queryRoleCount(@Param("isEnable") int isEnable);
    
    SysRuleRole queryRoleByName(@Param("roleName") String roleName);

    List<SysRuleRole> findAllRole(@Param("isEnable") int isEnable);

    /**
     * 查询经销商角色
     * @param distributor
     * @return
     */
	SysRuleRole selectDistributorRole(@Param("distributor")String distributor);
}