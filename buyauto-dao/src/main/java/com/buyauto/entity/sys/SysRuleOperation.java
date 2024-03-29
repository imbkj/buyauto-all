package com.buyauto.entity.sys;

import java.util.Date;

public class SysRuleOperation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.oper_name
     *
     * @mbggenerated
     */
    private String operName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.oper_desc
     *
     * @mbggenerated
     */
    private String operDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.create_user
     *
     * @mbggenerated
     */
    private Long createUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.update_user
     *
     * @mbggenerated
     */
    private Long updateUser;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.is_enable
     *
     * @mbggenerated
     */
    private Short isEnable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.moudle_id
     *
     * @mbggenerated
     */
    private Long moudleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_rule_operation.top_num
     *
     * @mbggenerated
     */
    private Long topNum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.id
     *
     * @return the value of sys_rule_operation.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.id
     *
     * @param id the value for sys_rule_operation.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.oper_name
     *
     * @return the value of sys_rule_operation.oper_name
     *
     * @mbggenerated
     */
    public String getOperName() {
        return operName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.oper_name
     *
     * @param operName the value for sys_rule_operation.oper_name
     *
     * @mbggenerated
     */
    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.oper_desc
     *
     * @return the value of sys_rule_operation.oper_desc
     *
     * @mbggenerated
     */
    public String getOperDesc() {
        return operDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.oper_desc
     *
     * @param operDesc the value for sys_rule_operation.oper_desc
     *
     * @mbggenerated
     */
    public void setOperDesc(String operDesc) {
        this.operDesc = operDesc == null ? null : operDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.create_user
     *
     * @return the value of sys_rule_operation.create_user
     *
     * @mbggenerated
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.create_user
     *
     * @param createUser the value for sys_rule_operation.create_user
     *
     * @mbggenerated
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.create_time
     *
     * @return the value of sys_rule_operation.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.create_time
     *
     * @param createTime the value for sys_rule_operation.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.update_user
     *
     * @return the value of sys_rule_operation.update_user
     *
     * @mbggenerated
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.update_user
     *
     * @param updateUser the value for sys_rule_operation.update_user
     *
     * @mbggenerated
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.update_time
     *
     * @return the value of sys_rule_operation.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.update_time
     *
     * @param updateTime the value for sys_rule_operation.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.is_enable
     *
     * @return the value of sys_rule_operation.is_enable
     *
     * @mbggenerated
     */
    public Short getIsEnable() {
        return isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.is_enable
     *
     * @param isEnable the value for sys_rule_operation.is_enable
     *
     * @mbggenerated
     */
    public void setIsEnable(Short isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.moudle_id
     *
     * @return the value of sys_rule_operation.moudle_id
     *
     * @mbggenerated
     */
    public Long getMoudleId() {
        return moudleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.moudle_id
     *
     * @param moudleId the value for sys_rule_operation.moudle_id
     *
     * @mbggenerated
     */
    public void setMoudleId(Long moudleId) {
        this.moudleId = moudleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_rule_operation.top_num
     *
     * @return the value of sys_rule_operation.top_num
     *
     * @mbggenerated
     */
    public Long getTopNum() {
        return topNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_rule_operation.top_num
     *
     * @param topNum the value for sys_rule_operation.top_num
     *
     * @mbggenerated
     */
    public void setTopNum(Long topNum) {
        this.topNum = topNum;
    }
}