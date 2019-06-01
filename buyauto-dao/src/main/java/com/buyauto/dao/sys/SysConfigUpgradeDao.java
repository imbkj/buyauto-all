package com.buyauto.dao.sys;

import java.io.Serializable;

public class SysConfigUpgradeDao implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9198763732791196273L;

	public Long upgradeId;//ID
	
	public String category;//区分升降级 1：升级 2：降级
	
	public String month;//考核月数
	
	public String totalSales;//总销售数量
	
	public String monthlySales;//月销售数量
	
	public String totalAmount;//销售总额
	
	public String monthlyAmount;//月销售额

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(String totalSales) {
		this.totalSales = totalSales;
	}

	public String getMonthlySales() {
		return monthlySales;
	}

	public void setMonthlySales(String monthlySales) {
		this.monthlySales = monthlySales;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(String monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public Long getUpgradeId() {
		return upgradeId;
	}

	public void setUpgradeId(Long upgradeId) {
		this.upgradeId = upgradeId;
	}
	
	
}
