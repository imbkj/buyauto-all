package com.buyauto.entity.user;

import java.math.BigDecimal;
import java.util.Date;

public class TUserFinance {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.id
	 *
	 * @mbggenerated
	 */
	private Long id;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.user_id
	 *
	 * @mbggenerated
	 */
	private Long userId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.amount
	 *
	 * @mbggenerated
	 */
	private BigDecimal amount;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.repayment
	 *
	 * @mbggenerated
	 */
	private Integer repayment;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.port
	 *
	 * @mbggenerated
	 */
	private Float port;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.term
	 *
	 * @mbggenerated
	 */
	private Integer term;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.type
	 *
	 * @mbggenerated
	 */
	private Integer type;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.create_date
	 *
	 * @mbggenerated
	 */
	private Date createDate;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_user_finance.status
	 *
	 * @mbggenerated
	 */
	private Integer status;

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.id
	 *
	 * @return the value of t_user_finance.id
	 *
	 * @mbggenerated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.id
	 *
	 * @param id
	 *            the value for t_user_finance.id
	 *
	 * @mbggenerated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.user_id
	 *
	 * @return the value of t_user_finance.user_id
	 *
	 * @mbggenerated
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.user_id
	 *
	 * @param userId
	 *            the value for t_user_finance.user_id
	 *
	 * @mbggenerated
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.amount
	 *
	 * @return the value of t_user_finance.amount
	 *
	 * @mbggenerated
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.amount
	 *
	 * @param amount
	 *            the value for t_user_finance.amount
	 *
	 * @mbggenerated
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.repayment
	 *
	 * @return the value of t_user_finance.repayment
	 *
	 * @mbggenerated
	 */
	public Integer getRepayment() {
		return repayment;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.repayment
	 *
	 * @param repayment
	 *            the value for t_user_finance.repayment
	 *
	 * @mbggenerated
	 */
	public void setRepayment(Integer repayment) {
		this.repayment = repayment;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.port
	 *
	 * @return the value of t_user_finance.port
	 *
	 * @mbggenerated
	 */
	public Float getPort() {
		return port;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.port
	 *
	 * @param port
	 *            the value for t_user_finance.port
	 *
	 * @mbggenerated
	 */
	public void setPort(Float port) {
		this.port = port;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.term
	 *
	 * @return the value of t_user_finance.term
	 *
	 * @mbggenerated
	 */
	public Integer getTerm() {
		return term;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.term
	 *
	 * @param term
	 *            the value for t_user_finance.term
	 *
	 * @mbggenerated
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.type
	 *
	 * @return the value of t_user_finance.type
	 *
	 * @mbggenerated
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.type
	 *
	 * @param type
	 *            the value for t_user_finance.type
	 *
	 * @mbggenerated
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.create_date
	 *
	 * @return the value of t_user_finance.create_date
	 *
	 * @mbggenerated
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.create_date
	 *
	 * @param createDate
	 *            the value for t_user_finance.create_date
	 *
	 * @mbggenerated
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_user_finance.status
	 *
	 * @return the value of t_user_finance.status
	 *
	 * @mbggenerated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_user_finance.status
	 *
	 * @param status
	 *            the value for t_user_finance.status
	 *
	 * @mbggenerated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public TUserFinance() {
	}

	public TUserFinance(Long id, Long userId, BigDecimal amount, Integer repayment, Float port, Integer term,
			Integer type, Date createDate, Integer status) {
		this.id = id;
		this.userId = userId;
		this.amount = amount;
		this.repayment = repayment;
		this.port = port;
		this.term = term;
		this.type = type;
		this.createDate = createDate;
		this.status = status;
	}

}