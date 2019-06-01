package com.buyauto.util.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.buyauto.util.method.AuthBackend;
import com.buyauto.util.method.NHDateUtils;

/**
 * 
 * @Title: UserSessionInfo.java
 * @Package com.hoyoda.blade.common.util
 * @Description: session中存放的用户stable信息
 * @author LeoZhang
 * @date 2014-6-3 下午12:02:18
 * @version V2.0
 */
public class UserSessionInfoBackend implements Serializable {

	private static final long serialVersionUID = 1568932092933110922L;
	public static final String SessionKey = "UserSessionInfoBackend";
	private static final String __Admin = "admin";
	private Date lastLoginTime;
	private long id;
	private String name;
	private Set<Long> authorities = new HashSet<Long>();
	private Long lenId;

	public UserSessionInfoBackend() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isHaveAuthority(AuthBackend[] authBackend) {
		for (AuthBackend a : authBackend) {
			// 如果有一个不包含，直接返回false
			if (!authorities.contains(a.getValue())) {
				return false;
			}
		}
		// 如果都没有返回false，就返回true
		return true;
	}

	public boolean isAdmin() {
		return __Admin.equals(this.name);
	}

	public Long getLenId() {
		return lenId;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public String getLastLoginTimeCn() {
		if (lastLoginTime == null) {
			return "首次登陆";
		}
		return NHDateUtils.getHYDYMDHMSSFHH().format(lastLoginTime);
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLenId(Long lenId) {
		this.lenId = lenId;
	}

}
