package com.mor.test.session.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mor.test.session.JwtSessionManager;
import com.mor.test.session.JwtSessionManager.USER_ROLE;

import lombok.ToString;


@RedisHash("session") 
@ToString 
public class SessionDto  implements UserDetails {

	@Id
	private String token;
	
	private String deptId; //소속
	private String device; //접속 장비
	
	private String userId;
	private String userNm;
	
	private String password;
	private String memberName;
	private String email;

	/*권한*/
	private String userRole;
	private int passwordLock;
	private String regDate;
	private String modDate;
	private String passwordChgDate;
	private String status;
	
	private Collection<? extends GrantedAuthority> authorities;
	   
	private boolean isEnabled = true;
	
	private String username;
	
	private boolean isCredentialsNonExpired = true;
	
	private boolean isAccountNonExpired = true;
	
	private boolean isAccountNonLocked = true;

	public static Collection<? extends GrantedAuthority> convertRoles(String strRoles){
		String[] aryRoles = strRoles.split("\\|");
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		System.out.println("strRoles:"+strRoles);
		for(String role :aryRoles) {
			System.out.println("role:"+role);
			try {
				list.add(USER_ROLE.valueOf(role).get());
			}catch(Throwable e) {e.printStackTrace();}
		}
		return list;
		
	}
	
	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
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



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getMemberName() {
		return memberName;
	}



	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getUserRole() {
		return userRole;
	}



	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}



	public int getPasswordLock() {
		return passwordLock;
	}



	public void setPasswordLock(int passwordLock) {
		this.passwordLock = passwordLock;
	}



	public String getRegDate() {
		return regDate;
	}



	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}



	public String getModDate() {
		return modDate;
	}



	public void setModDate(String modDate) {
		this.modDate = modDate;
	}



	public String getPasswordChgDate() {
		return passwordChgDate;
	}



	public void setPasswordChgDate(String passwordChgDate) {
		this.passwordChgDate = passwordChgDate;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}



	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}



	public boolean isEnabled() {
		return isEnabled;
	}



	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}



	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}



	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}



	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}



	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}



	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}



	@Override
	public String toString() {
		return "SessionDto [token=" + token + ", strRoles="+userRole + ", roles="+(authorities!=null?authorities.size():0)
				+ ", deptId=" + deptId + ", device=" + device + ", userId=" + userId + ", userNm=" + userNm + "]";
	}
	
	
	
}
