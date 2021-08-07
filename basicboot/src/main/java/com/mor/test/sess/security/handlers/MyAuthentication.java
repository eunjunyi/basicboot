package com.mor.test.sess.security.handlers;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.mor.test.session.vo.SessionDto;

import lombok.Data;


//현재 로그인한 사용자 객체 저장 DTO

@Data
public class MyAuthentication extends UsernamePasswordAuthenticationToken{
	private static final long serialVersionUID = 1L;
	
	SessionDto member;
	
	public MyAuthentication(String id, String password, List<GrantedAuthority> grantedAuthorityList, SessionDto member) {
		super(id, password, grantedAuthorityList);
		this.member = member;
	}
}

