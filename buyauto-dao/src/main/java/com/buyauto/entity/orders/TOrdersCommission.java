package com.buyauto.entity.orders;

import java.math.BigDecimal;
import java.util.Date;

public class TOrdersCommission {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.id
	 *
	 * @mbggenerated
	 */
	private Long id;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.order_id
	 *
	 * @mbggenerated
	 */
	private Long orderId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.user_id
	 *
	 * @mbggenerated
	 */
	private Long userId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.beneficiary_id
	 *
	 * @mbggenerated
	 */
	private Long beneficiaryId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.father_id
	 *
	 * @mbggenerated
	 */
	private Long fatherId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.father_phone
	 *
	 * @mbggenerated
	 */
	private String fatherPhone;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.register_date
	 *
	 * @mbggenerated
	 */
	private Date registerDate;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.count
	 *
	 * @mbggenerated
	 */
	private Integer count;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.amount
	 *
	 * @mbggenerated
	 */
	private BigDecimal amount;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.commission
	 *
	 * @mbggenerated
	 */
	private BigDecimal commission;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.complete_time
	 *
	 * @mbggenerated
	 */
	private Date completeTime;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_orders_commission.create_date
	 *
	 * @mbggenerated
	 */
	private Date createDate;
	private String ratio;
	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.id
	 *
	 * @return the value of t_orders_commission.id
	 *
	 * @mbggenerated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.id
	 *
	 * @param id
	 *            the value for t_orders_commission.id
	 *
	 * @mbggenerated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.order_id
	 *
	 * @return the value of t_orders_commission.order_id
	 *
	 * @mbggenerated
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.order_id
	 *
	 * @param orderId
	 *            the value for t_orders_commission.order_id
	 *
	 * @mbggenerated
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.user_id
	 *
	 * @return the value of t_orders_commission.user_id
	 *
	 * @mbggenerated
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.user_id
	 *
	 * @param userId
	 *            the value for t_orders_commission.user_id
	 *
	 * @mbggenerated
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.beneficiary_id
	 *
	 * @return the value of t_orders_commission.beneficiary_id
	 *
	 * @mbggenerated
	 */
	public Long getBeneficiaryId() {
		return beneficiaryId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.beneficiary_id
	 *
	 * @param beneficiaryId
	 *            the value for t_orders_commission.beneficiary_id
	 *
	 * @mbggenerated
	 */
	public void setBeneficiaryId(Long beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.father_id
	 *
	 * @return the value of t_orders_commission.father_id
	 *
	 * @mbggenerated
	 */
	public Long getFatherId() {
		return fatherId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.father_id
	 *
	 * @param fatherId
	 *            the value for t_orders_commission.father_id
	 *
	 * @mbggenerated
	 */
	public void setFatherId(Long fatherId) {
		this.fatherId = fatherId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.father_phone
	 *
	 * @return the value of t_orders_commission.father_phone
	 *
	 * @mbggenerated
	 */
	public String getFatherPhone() {
		return fatherPhone;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.father_phone
	 *
	 * @param fatherPhone
	 *            the value for t_orders_commission.father_phone
	 *
	 * @mbggenerated
	 */
	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone == null ? null : fatherPhone.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.register_date
	 *
	 * @return the value of t_orders_commission.register_date
	 *
	 * @mbggenerated
	 */
	public Date getRegisterDate() {
		return registerDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.register_date
	 *
	 * @param registerDate
	 *            the value for t_orders_commission.register_date
	 *
	 * @mbggenerated
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.count
	 *
	 * @return the value of t_orders_commission.count
	 *
	 * @mbggenerated
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.count
	 *
	 * @param count
	 *            the value for t_orders_commission.count
	 *
	 * @mbggenerated
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.amount
	 *
	 * @return the value of t_orders_commission.amount
	 *
	 * @mbggenerated
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.amount
	 *
	 * @param amount
	 *            the value for t_orders_commission.amount
	 *
	 * @mbggenerated
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.commission
	 *
	 * @return the value of t_orders_commission.commission
	 *
	 * @mbggenerated
	 */
	public BigDecimal getCommission() {
		return commission;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.commission
	 *
	 * @param commission
	 *            the value for t_orders_commission.commission
	 *
	 * @mbggenerated
	 */
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.complete_time
	 *
	 * @return the value of t_orders_commission.complete_time
	 *
	 * @mbggenerated
	 */
	public Date getCompleteTime() {
		return completeTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.complete_time
	 *
	 * @param completeTime
	 *            the value for t_orders_commission.complete_time
	 *
	 * @mbggenerated
	 */
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_orders_commission.create_date
	 *
	 * @return the value of t_orders_commission.create_date
	 *
	 * @mbggenerated
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_orders_commission.create_date
	 *
	 * @param createDate
	 *            the value for t_orders_commission.create_date
	 *
	 * @mbggenerated
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public TOrdersCommission() {
	}

	public TOrdersCommission(Long id, Long orderId, Long userId, Long beneficiaryId, Long fatherId, String fatherPhone,
			Date registerDate, Integer count, BigDecimal amount, BigDecimal commission, Date completeTime,
			Date createDate,String ratio)

	{
		this.id = id;
		this.orderId = orderId;
		this.userId = userId;
		this.beneficiaryId = beneficiaryId;
		this.fatherId = fatherId;
		this.fatherPhone = fatherPhone;
		this.registerDate = registerDate;
		this.ratio = ratio;
		this.count = count;
		this.amount = amount;
		this.commission = commission;
		this.completeTime = completeTime;
		this.createDate = createDate;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
}