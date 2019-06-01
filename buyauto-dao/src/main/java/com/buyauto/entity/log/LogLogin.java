package com.buyauto.entity.log;

import java.util.Date;

public class LogLogin {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.login_name
     *
     * @mbggenerated
     */
    private String loginName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.ipaddress
     *
     * @mbggenerated
     */
    private String ipaddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.result
     *
     * @mbggenerated
     */
    private Integer result;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.result_content
     *
     * @mbggenerated
     */
    private String resultContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.client_device
     *
     * @mbggenerated
     */
    private Integer clientDevice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column log_login.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.id
     *
     * @return the value of log_login.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.id
     *
     * @param id the value for log_login.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.login_name
     *
     * @return the value of log_login.login_name
     *
     * @mbggenerated
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.login_name
     *
     * @param loginName the value for log_login.login_name
     *
     * @mbggenerated
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.ipaddress
     *
     * @return the value of log_login.ipaddress
     *
     * @mbggenerated
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.ipaddress
     *
     * @param ipaddress the value for log_login.ipaddress
     *
     * @mbggenerated
     */
    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress == null ? null : ipaddress.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.result
     *
     * @return the value of log_login.result
     *
     * @mbggenerated
     */
    public Integer getResult() {
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.result
     *
     * @param result the value for log_login.result
     *
     * @mbggenerated
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.result_content
     *
     * @return the value of log_login.result_content
     *
     * @mbggenerated
     */
    public String getResultContent() {
        return resultContent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.result_content
     *
     * @param resultContent the value for log_login.result_content
     *
     * @mbggenerated
     */
    public void setResultContent(String resultContent) {
        this.resultContent = resultContent == null ? null : resultContent.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.client_device
     *
     * @return the value of log_login.client_device
     *
     * @mbggenerated
     */
    public Integer getClientDevice() {
        return clientDevice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.client_device
     *
     * @param clientDevice the value for log_login.client_device
     *
     * @mbggenerated
     */
    public void setClientDevice(Integer clientDevice) {
        this.clientDevice = clientDevice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column log_login.create_time
     *
     * @return the value of log_login.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column log_login.create_time
     *
     * @param createTime the value for log_login.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}