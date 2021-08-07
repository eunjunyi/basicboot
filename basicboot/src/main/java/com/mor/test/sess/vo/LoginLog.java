package com.mor.test.sess.vo;
import com.mor.test.session.vo.SessionDto;

import lombok.Data;

@Data
public class LoginLog extends SessionDto {
	private String loginIp;
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	private String loginDate;

}
