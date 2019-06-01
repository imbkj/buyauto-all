package com.buyauto.dao.orders;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.buyauto.entity.products.TProducts;
import com.buyauto.entity.products.TProductsImage;

public class TOrdersDao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private Long userId;
    private Long companyId;
    private Long productId;
    private Long carId;
    private String configure;
    private BigDecimal configurePrice;
    private BigDecimal amount;
    private String takeLocation;
    private String deliverTime;
    private String customer;
    private String customerTel;
    private BigDecimal deposit;
    private Integer oldStatus;
    private Integer payStatus;
    private Integer status;
    private String cancelReason;
    private String message;
    private Date createDate;
    private Date updateDate;
    private Integer resubmit;
    private Integer count;
    private Integer version;
    private Integer takeWay;
    private Date completeTime;
    private String creatTime;
    private BigDecimal premiums;
    private BigDecimal carPrice;
	//产品
	private TProducts product;
	//产品图片
	private TProductsImage productImage;
	//车辆品牌
	private String carBrandName;
	//经销商名称
	private String companyName;
	//物流字段
	private String logisticsInfo;
	//对公账号
	private String accountNumber;
	
    private Long supplierId;
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	

	public Long getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	
	

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

	public BigDecimal getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(BigDecimal carPrice) {
		this.carPrice = carPrice;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}


	/**
	 * @return the carId
	 */
	public Long getCarId() {
		return carId;
	}

	/**
	 * @param carId the carId to set
	 */
	public void setCarId(Long carId) {
		this.carId = carId;
	}

	/**
	 * @return the configure
	 */
	public String getConfigure() {
		return configure;
	}

	/**
	 * @param configure the configure to set
	 */
	public void setConfigure(String configure) {
		this.configure = configure;
	}

	/**
	 * @return the configurePrice
	 */
	public BigDecimal getConfigurePrice() {
		return configurePrice;
	}

	/**
	 * @param configurePrice the configurePrice to set
	 */
	public void setConfigurePrice(BigDecimal configurePrice) {
		this.configurePrice = configurePrice;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the takeLocation
	 */
	public String getTakeLocation() {
		return takeLocation;
	}

	/**
	 * @param takeLocation the takeLocation to set
	 */
	public void setTakeLocation(String takeLocation) {
		this.takeLocation = takeLocation;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * @return the customerTel
	 */
	public String getCustomerTel() {
		return customerTel;
	}

	/**
	 * @param customerTel the customerTel to set
	 */
	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	/**
	 * @return the deposit
	 */
	public BigDecimal getDeposit() {
		return deposit;
	}

	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	
	/**
	 * @return the oldStatus
	 */
	public Integer getOldStatus() {
		return oldStatus;
	}

	/**
	 * @param oldStatus the oldStatus to set
	 */
	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the cancelReason
	 */
	public String getCancelReason() {
		return cancelReason;
	}

	/**
	 * @param cancelReason the cancelReason to set
	 */
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the payStatus
	 */
	public Integer getPayStatus() {
		return payStatus;
	}

	/**
	 * @param payStatus the payStatus to set
	 */
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the resubmit
	 */
	public Integer getResubmit() {
		return resubmit;
	}

	/**
	 * @param resubmit the resubmit to set
	 */
	public void setResubmit(Integer resubmit) {
		this.resubmit = resubmit;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the product
	 */
	public TProducts getProduct() {
		return product;
	}

	public TProductsImage getProductImage() {
		return productImage;
	}

	public void setProductImage(TProductsImage productImage) {
		this.productImage = productImage;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(TProducts product) {
		this.product = product;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the deliverTime
	 */
	public String getDeliverTime() {
		return deliverTime;
	}

	/**
	 * @param deliverTime the deliverTime to set
	 */
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}

	/**
	 * @return the takeWay
	 */
	public Integer getTakeWay() {
		return takeWay;
	}

	/**
	 * @param takeWay the takeWay to set
	 */
	public void setTakeWay(Integer takeWay) {
		this.takeWay = takeWay;
	}

	/**
	 * @return the completeTime
	 */
	public Date getCompleteTime() {
		return completeTime;
	}

	/**
	 * @param completeTime the completeTime to set
	 */
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	/**
	 * @return the carBrandName
	 */
	public String getCarBrandName() {
		return carBrandName;
	}

	/**
	 * @param carBrandName the carBrandName to set
	 */
	public void setCarBrandName(String carBrandName) {
		this.carBrandName = carBrandName;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * @return the logisticsInfo
	 */
	public String getLogisticsInfo() {
		return logisticsInfo;
	}

	/**
	 * @param logisticsInfo the logisticsInfo to set
	 */
	public void setLogisticsInfo(String logisticsInfo) {
		this.logisticsInfo = logisticsInfo;
	}

	public BigDecimal getPremiums() {
		return premiums;
	}

	public void setPremiums(BigDecimal premiums) {
		this.premiums = premiums;
	}

	
	
}
