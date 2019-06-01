package com.buyauto.dao.user;

import java.math.BigDecimal;
import java.util.Date;

public class TUserFinanceDao {
	private Long id;
	private String name;
	private String phone;
	private BigDecimal amount;
	private Integer repayment;
	private Float port;
	private Integer term;
	private Integer type;
	private Date createDate;
	private Integer status;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getRepayment() {
		return repayment;
	}
	public void setRepayment(Integer repayment) {
		this.repayment = repayment;
	}
	public Float getPort() {
		return port;
	}
	public void setPort(Float port) {
		this.port = port;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}