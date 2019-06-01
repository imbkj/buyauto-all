package com.buyauto.dao.user;

import java.math.BigDecimal;

public class TUsersRecommenderDao {
	private Long id;
	private String name;
	private String phone;
	private String cardID;
	private String type;
	private String debitCard;
	private String debitCardBank;
	private String creditCard;
	private String creditCardBank;
	private BigDecimal sumSellAmount;
	private BigDecimal sumCommission;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDebitCard() {
		return debitCard;
	}
	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard;
	}
	public String getDebitCardBank() {
		return debitCardBank;
	}
	public void setDebitCardBank(String debitCardBank) {
		this.debitCardBank = debitCardBank;
	}
	public String getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}
	public String getCreditCardBank() {
		return creditCardBank;
	}
	public void setCreditCardBank(String creditCardBank) {
		this.creditCardBank = creditCardBank;
	}
	public BigDecimal getSumSellAmount() {
		return sumSellAmount;
	}
	public void setSumSellAmount(BigDecimal sumSellAmount) {
		this.sumSellAmount = sumSellAmount;
	}
	public BigDecimal getSumCommission() {
		return sumCommission;
	}
	public void setSumCommission(BigDecimal sumCommission) {
		this.sumCommission = sumCommission;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}