package com.buyauto.dao.sys;

import java.io.Serializable;
import java.util.Date;

public class SysConfigPojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6111954458927310121L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.state
     *
     * @mbggenerated
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.sc_remark
     *
     * @mbggenerated
     */
    private String scRemark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.group_name
     *
     * @mbggenerated
     */
    private String groupName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.sc_value
     *
     * @mbggenerated
     */
    private String scValue;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.sc_type
     *
     * @mbggenerated
     */
    private Integer scType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.sc_key
     *
     * @mbggenerated
     */
    private String scKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sys_config.create_time
     *
     * @mbggenerated
     */
    private Date createTime;
    
    private String imgfilePath;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.id
     *
     * @return the value of sys_config.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.id
     *
     * @param id the value for sys_config.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    
    
    public String getImgfilePath() {
		return imgfilePath;
	}

	public void setImgfilePath(String imgfilePath) {
		this.imgfilePath = imgfilePath;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.state
     *
     * @return the value of sys_config.state
     *
     * @mbggenerated
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.state
     *
     * @param state the value for sys_config.state
     *
     * @mbggenerated
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.sc_remark
     *
     * @return the value of sys_config.sc_remark
     *
     * @mbggenerated
     */
    public String getScRemark() {
        return scRemark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.sc_remark
     *
     * @param scRemark the value for sys_config.sc_remark
     *
     * @mbggenerated
     */
    public void setScRemark(String scRemark) {
        this.scRemark = scRemark == null ? null : scRemark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.group_name
     *
     * @return the value of sys_config.group_name
     *
     * @mbggenerated
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.group_name
     *
     * @param groupName the value for sys_config.group_name
     *
     * @mbggenerated
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.sc_value
     *
     * @return the value of sys_config.sc_value
     *
     * @mbggenerated
     */
    public String getScValue() {
        return scValue;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.sc_value
     *
     * @param scValue the value for sys_config.sc_value
     *
     * @mbggenerated
     */
    public void setScValue(String scValue) {
        this.scValue = scValue == null ? null : scValue.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.sc_type
     *
     * @return the value of sys_config.sc_type
     *
     * @mbggenerated
     */
    public Integer getScType() {
        return scType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.sc_type
     *
     * @param scType the value for sys_config.sc_type
     *
     * @mbggenerated
     */
    public void setScType(Integer scType) {
        this.scType = scType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.sc_key
     *
     * @return the value of sys_config.sc_key
     *
     * @mbggenerated
     */
    public String getScKey() {
        return scKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.sc_key
     *
     * @param scKey the value for sys_config.sc_key
     *
     * @mbggenerated
     */
    public void setScKey(String scKey) {
        this.scKey = scKey == null ? null : scKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sys_config.create_time
     *
     * @return the value of sys_config.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sys_config.create_time
     *
     * @param createTime the value for sys_config.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
