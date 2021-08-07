package com.mor.test.welcomelist;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthentication implements Authentication {

	private String token;
	private long loginTime;
	private long lastAccessTime;

	private String deptId; // 소속
	private String device; // 접속 장비

	private String userId;
	private String userNm;

	private Object details;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return userNm;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDetails(Object details) {
		// TODO Auto-generated method stub
		this.details = details;
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return details;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "JwtAuthentication [token=" + token + ", loginTime=" + loginTime + ", lastAccessTime=" + lastAccessTime
				+ ", deptId=" + deptId + ", device=" + device + ", userId=" + userId + ", userNm=" + userNm
				+ ", authorities=" + authorities + "]";
	}

}
