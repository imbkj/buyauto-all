package com.buyauto.util.pojo;

import java.math.BigDecimal;

public class BaseJymxResp {

	/* 交易订单号 */
	private String id;
	/* 商户订单号 */
	private String orderId;
	/* 业务类型 */
	private String tradeType;
	/* 子类型 */
	private String childType;
	/* 支付人 */
	private String zfr;
	/* 支付帐号 */
	private String zfzh;
	/* 商品名称 */
	private String buyName;
	/* 订单金额 */
	private BigDecimal amount;
	/* 手续费 */
	private BigDecimal sxf;
	/* 退款金额 */
	private BigDecimal tkje;
	/* 收款人 */
	private String skr;
	/* 收款人帐号 */
	private String skrzh;
	/* 订单状态 */
	private String status;
	/* 支付成功时间 */
	public String successDate;
	/* 订单创建时间 */
	public String createDate;
	/* 支付渠道 */
	public String zfqd;
	/* 支付终端 */
	public String zfzd;
	/* 备注 */
	public String beizhu;
	/* 平台手续费 */
	public BigDecimal ptsxf;
	/* 付款方 */
	public String fkf;
	/* 收款方 */
	public String skf;
	/* 商户批次号 */
	public String shpch;
	/* 交易批次号 */
	public String jypch;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getZfr() {
		return zfr;
	}

	public void setZfr(String zfr) {
		this.zfr = zfr;
	}

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getSxf() {
		return sxf;
	}

	public void setSxf(BigDecimal sxf) {
		this.sxf = sxf;
	}

	public BigDecimal getTkje() {
		return tkje;
	}

	public void setTkje(BigDecimal tkje) {
		this.tkje = tkje;
	}

	public String getSkr() {
		return skr;
	}

	public void setSkr(String skr) {
		this.skr = skr;
	}

	public String getSkrzh() {
		return skrzh;
	}

	public void setSkrzh(String skrzh) {
		this.skrzh = skrzh;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(String successDate) {
		this.successDate = successDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getZfqd() {
		return zfqd;
	}

	public void setZfqd(String zfqd) {
		this.zfqd = zfqd;
	}

	public String getZfzd() {
		return zfzd;
	}

	public void setZfzd(String zfzd) {
		this.zfzd = zfzd;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	public BigDecimal getPtsxf() {
		return ptsxf;
	}

	public void setPtsxf(BigDecimal ptsxf) {
		this.ptsxf = ptsxf;
	}

	public String getFkf() {
		return fkf;
	}

	public void setFkf(String fkf) {
		this.fkf = fkf;
	}

	public String getSkf() {
		return skf;
	}

	public void setSkf(String skf) {
		this.skf = skf;
	}

	public String getShpch() {
		return shpch;
	}

	public void setShpch(String shpch) {
		this.shpch = shpch;
	}

	public String getJypch() {
		return jypch;
	}

	public void setJypch(String jypch) {
		this.jypch = jypch;
	}

	@Override
	public String toString() {
		return "BaseJymxResp [id=" + id + ", orderId=" + orderId + ", tradeType=" + tradeType + ", childType=" + childType + ", zfr=" + zfr + ", zfzh=" + zfzh
				+ ", buyName=" + buyName + ", amount=" + amount + ", sxf=" + sxf + ", tkje=" + tkje + ", skr=" + skr + ", skrzh=" + skrzh + ", status="
				+ status + ", successDate=" + successDate + ", createDate=" + createDate + ", zfqd=" + zfqd + ", zfzd=" + zfzd + ", beizhu=" + beizhu
				+ ", ptsxf=" + ptsxf + ", fkf=" + fkf + ", skf=" + skf + ", shpch=" + shpch + ", jypch=" + jypch + "]";
	}

}
