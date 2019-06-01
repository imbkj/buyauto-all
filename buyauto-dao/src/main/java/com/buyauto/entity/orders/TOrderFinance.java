package com.buyauto.entity.orders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TOrderFinance implements Serializable {
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 8938948335960990624L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.order_id
     *
     * @mbggenerated
     */
    private Long orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.user_id
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.loan_amount
     *
     * @mbggenerated
     */
    private BigDecimal loanAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.term
     *
     * @mbggenerated
     */
    private Integer term;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.user_phone
     *
     * @mbggenerated
     */
    private String userPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.create_date
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_order_finance.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.id
     *
     * @return the value of t_order_finance.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.id
     *
     * @param id the value for t_order_finance.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.order_id
     *
     * @return the value of t_order_finance.order_id
     *
     * @mbggenerated
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.order_id
     *
     * @param orderId the value for t_order_finance.order_id
     *
     * @mbggenerated
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.user_id
     *
     * @return the value of t_order_finance.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.user_id
     *
     * @param userId the value for t_order_finance.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.loan_amount
     *
     * @return the value of t_order_finance.loan_amount
     *
     * @mbggenerated
     */
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.loan_amount
     *
     * @param loanAmount the value for t_order_finance.loan_amount
     *
     * @mbggenerated
     */
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.term
     *
     * @return the value of t_order_finance.term
     *
     * @mbggenerated
     */
    public Integer getTerm() {
        return term;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.term
     *
     * @param term the value for t_order_finance.term
     *
     * @mbggenerated
     */
    public void setTerm(Integer term) {
        this.term = term;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.user_phone
     *
     * @return the value of t_order_finance.user_phone
     *
     * @mbggenerated
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.user_phone
     *
     * @param userPhone the value for t_order_finance.user_phone
     *
     * @mbggenerated
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.create_date
     *
     * @return the value of t_order_finance.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.create_date
     *
     * @param createDate the value for t_order_finance.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_order_finance.status
     *
     * @return the value of t_order_finance.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_order_finance.status
     *
     * @param status the value for t_order_finance.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}