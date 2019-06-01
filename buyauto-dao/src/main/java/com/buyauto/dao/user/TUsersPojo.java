package com.buyauto.dao.user;

import java.io.Serializable;


public class TUsersPojo implements Serializable {
    private Long id;
    private String companyName;//公司名
    private String name;//姓名
    private Integer position;//岗位:0管理员,1销售，2财务
    private String account;//账号
    private Integer status;//0待审核,1可用,2审核不通过,3冻结
    private Integer maxNumberCount;//经销商可新建员工数量
    
    public TUsersPojo(){}
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getMaxNumberCount() {
		return maxNumberCount;
	}

	public void setMaxNumberCount(Integer maxNumberCount) {
		this.maxNumberCount = maxNumberCount;
	}
   
}