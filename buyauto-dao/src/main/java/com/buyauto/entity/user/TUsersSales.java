package com.buyauto.entity.user;

import java.util.Date;

public class TUsersSales {
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.id
	 *
	 * @mbggenerated
	 */
	private Long id;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.user_id
	 *
	 * @mbggenerated
	 */
	private Long userId;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.name
	 *
	 * @mbggenerated
	 */
	private String name;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.cardID
	 *
	 * @mbggenerated
	 */
	private String cardid;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.card_file
	 *
	 * @mbggenerated
	 */
	private String cardFile;
	private String cardBFile;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.debit_card
	 *
	 * @mbggenerated
	 */
	private String debitCard;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.debit_card_bank
	 *
	 * @mbggenerated
	 */
	private String debitCardBank;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.debit_card_file
	 *
	 * @mbggenerated
	 */
	private String debitCardFile;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.credit_card
	 *
	 * @mbggenerated
	 */
	private String creditCard;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.credit_card_bank
	 *
	 * @mbggenerated
	 */
	private String creditCardBank;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.credit_card_file
	 *
	 * @mbggenerated
	 */
	private String creditCardFile;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.create_date
	 *
	 * @mbggenerated
	 */
	private Date createDate;

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to
	 * the database column t_users_sales.status
	 *
	 * @mbggenerated
	 */
	private Integer status;

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.id
	 *
	 * @return the value of t_users_sales.id
	 *
	 * @mbggenerated
	 */
	public Long getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.id
	 *
	 * @param id
	 *            the value for t_users_sales.id
	 *
	 * @mbggenerated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.user_id
	 *
	 * @return the value of t_users_sales.user_id
	 *
	 * @mbggenerated
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.user_id
	 *
	 * @param userId
	 *            the value for t_users_sales.user_id
	 *
	 * @mbggenerated
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.name
	 *
	 * @return the value of t_users_sales.name
	 *
	 * @mbggenerated
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.name
	 *
	 * @param name
	 *            the value for t_users_sales.name
	 *
	 * @mbggenerated
	 */
	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.cardID
	 *
	 * @return the value of t_users_sales.cardID
	 *
	 * @mbggenerated
	 */
	public String getCardid() {
		return cardid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.cardID
	 *
	 * @param cardid
	 *            the value for t_users_sales.cardID
	 *
	 * @mbggenerated
	 */
	public void setCardid(String cardid) {
		this.cardid = cardid == null ? null : cardid.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.card_file
	 *
	 * @return the value of t_users_sales.card_file
	 *
	 * @mbggenerated
	 */
	public String getCardFile() {
		return cardFile;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.card_file
	 *
	 * @param cardFile
	 *            the value for t_users_sales.card_file
	 *
	 * @mbggenerated
	 */
	public void setCardFile(String cardFile) {
		this.cardFile = cardFile == null ? null : cardFile.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.debit_card
	 *
	 * @return the value of t_users_sales.debit_card
	 *
	 * @mbggenerated
	 */
	public String getDebitCard() {
		return debitCard;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.debit_card
	 *
	 * @param debitCard
	 *            the value for t_users_sales.debit_card
	 *
	 * @mbggenerated
	 */
	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard == null ? null : debitCard.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.debit_card_bank
	 *
	 * @return the value of t_users_sales.debit_card_bank
	 *
	 * @mbggenerated
	 */
	public String getDebitCardBank() {
		return debitCardBank;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.debit_card_bank
	 *
	 * @param debitCardBank
	 *            the value for t_users_sales.debit_card_bank
	 *
	 * @mbggenerated
	 */
	public void setDebitCardBank(String debitCardBank) {
		this.debitCardBank = debitCardBank == null ? null : debitCardBank.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.debit_card_file
	 *
	 * @return the value of t_users_sales.debit_card_file
	 *
	 * @mbggenerated
	 */
	public String getDebitCardFile() {
		return debitCardFile;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.debit_card_file
	 *
	 * @param debitCardFile
	 *            the value for t_users_sales.debit_card_file
	 *
	 * @mbggenerated
	 */
	public void setDebitCardFile(String debitCardFile) {
		this.debitCardFile = debitCardFile == null ? null : debitCardFile.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.credit_card
	 *
	 * @return the value of t_users_sales.credit_card
	 *
	 * @mbggenerated
	 */
	public String getCreditCard() {
		return creditCard;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.credit_card
	 *
	 * @param creditCard
	 *            the value for t_users_sales.credit_card
	 *
	 * @mbggenerated
	 */
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard == null ? null : creditCard.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.credit_card_bank
	 *
	 * @return the value of t_users_sales.credit_card_bank
	 *
	 * @mbggenerated
	 */
	public String getCreditCardBank() {
		return creditCardBank;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.credit_card_bank
	 *
	 * @param creditCardBank
	 *            the value for t_users_sales.credit_card_bank
	 *
	 * @mbggenerated
	 */
	public void setCreditCardBank(String creditCardBank) {
		this.creditCardBank = creditCardBank == null ? null : creditCardBank.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.credit_card_file
	 *
	 * @return the value of t_users_sales.credit_card_file
	 *
	 * @mbggenerated
	 */
	public String getCreditCardFile() {
		return creditCardFile;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.credit_card_file
	 *
	 * @param creditCardFile
	 *            the value for t_users_sales.credit_card_file
	 *
	 * @mbggenerated
	 */
	public void setCreditCardFile(String creditCardFile) {
		this.creditCardFile = creditCardFile == null ? null : creditCardFile.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.create_date
	 *
	 * @return the value of t_users_sales.create_date
	 *
	 * @mbggenerated
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.create_date
	 *
	 * @param createDate
	 *            the value for t_users_sales.create_date
	 *
	 * @mbggenerated
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the
	 * value of the database column t_users_sales.status
	 *
	 * @return the value of t_users_sales.status
	 *
	 * @mbggenerated
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the
	 * value of the database column t_users_sales.status
	 *
	 * @param status
	 *            the value for t_users_sales.status
	 *
	 * @mbggenerated
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public TUsersSales() {
	}

	public TUsersSales(Long id, Long userId, String name, String cardid, String cardFile,String cardBFile, String debitCard,
			String debitCardBank, String debitCardFile, String creditCard, String creditCardBank, String creditCardFile,
			Date createDate, Integer status) {
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.cardid = cardid;
		this.cardFile = cardFile;
		this.debitCard = debitCard;
		this.debitCardBank = debitCardBank;
		this.debitCardFile = debitCardFile;
		this.creditCard = creditCard;
		this.creditCardBank = creditCardBank;
		this.creditCardFile = creditCardFile;
		this.createDate = createDate;
		this.status = status;
		this.cardBFile=cardBFile;
	}

	public String getCardBFile() {
		return cardBFile;
	}

	public void setCardBFile(String cardBFile) {
		this.cardBFile = cardBFile;
	}
}