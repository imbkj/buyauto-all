package com.buyauto.util.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.buyauto.util.method.AuthBackend;

/**
 * 前台用户信息 用户session
 */
public class UserSessionInfo implements Serializable {

	private static final long serialVersionUID = -4872757850642308505L;

	public static final String SessionKey = "UserSessionInfo";// 前台用户session的KEY

	public static final String SessionUIDKey = "UserSessionInfo_UID";

	private Long id;
	private String uName;// 姓名
	private Long companyId;// 经销商Id
	private String companyName;// 经销商名称
	private String account;// 账号名
	private String phone;// 手机
	private String email;// 邮箱
	private Integer uStatus;// 状态 0待审核,1可用,2审核不通过,3冻结
	private Integer position;// 岗位:0管理员,1销售，2财务 ,3供应商,4个人
	private String jobNumber;// 工号
	private Integer maxNumberCount;//经销商可新建员工数量
	private Integer realLevel;//个人实际等级:0个人,1销售,2经销商
	private Set<Long> authorities = new HashSet<Long>();//用户权限

	public UserSessionInfo() {
		super();
	}
	public UserSessionInfo(Long id) {
		this.id = id;
	}

	public UserSessionInfo(Long id, String uName, Long companyId, String companyName, String account, String phone, String email, Integer integer, Integer integer2,
			String jobNumber,Integer realLevel) {
		this.id = id;
		this.uName = uName;
		this.companyId = companyId;
		this.companyName = companyName;
		this.account = account;
		this.phone = phone;
		this.email = email;
		this.uStatus = integer;
		this.position = integer2;
		this.jobNumber = jobNumber;
		this.realLevel = realLevel;
	}

	/**
	 * 获取经销商ID
	 * 
	 * @return
	 */
	public Long getCompanyId() {
		// 如果是管理员,ID就是经销商ID
		if (this.position == 0) {
			return this.id;
		}
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getuStatus() {
		return uStatus;
	}

	public void setuStatus(Integer uStatus) {
		this.uStatus = uStatus;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	
	
	public Set<Long> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Long> authorities) {
		this.authorities = authorities;
	}

	
	public boolean isHaveAuthority(AuthBackend authBackend) {
		return authorities.contains(authBackend.getValue());
	}

	public boolean hasAuth(String code) {
		long l = Long.parseLong(code);
		return authorities.contains(l);
	}
	@Override
	public String toString() {
		return "UserSessionInfo [id=" + id + ", uName=" + uName + ", companyId=" + companyId + ", companyName=" + companyName + ", account=" + account + ", phone=" + phone
				+ ", email=" + email + ", uStatus=" + uStatus + ", position=" + position + ", jobNumber=" + jobNumber + ", authorities=" + authorities + "]";
	}

	public Integer getMaxNumberCount() {
		return maxNumberCount;
	}

	public void setMaxNumberCount(Integer maxNumberCount) {
		this.maxNumberCount = maxNumberCount;
	}

	public Integer getRealLevel() {
		return realLevel;
	}

	public void setRealLevel(Integer realLevel) {
		this.realLevel = realLevel;
	}


}
