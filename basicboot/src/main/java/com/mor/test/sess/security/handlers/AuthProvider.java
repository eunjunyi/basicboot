package com.mor.test.sess.security.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mor.test.sess.security.service.SecurityService;
import com.mor.test.session.vo.SessionDto;

@Component
public class AuthProvider implements AuthenticationProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthSuccessHandler.class);
	
	@Autowired
    SecurityService securityService;

    //로그인 버튼을 누를 경우
    //실행 1
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String id = authentication.getName();
		String password = authentication.getCredentials().toString();
		return authenticate(id, password);
	}
	
	//실행 2
	private Authentication authenticate(String id, String password) throws AuthenticationException{
		
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
		
		SessionDto member = new SessionDto();
			
		member = (SessionDto)securityService.loadUserByUsername(id);
	
		if ( member == null ){
			logger.info("사용자 정보가 없습니다.");
			throw new UsernameNotFoundException(id);
		}else if(member != null && !member.getPassword().equals(password) ) {
			logger.info("비밀번호가 틀렸습니다.");
			throw new BadCredentialsException(id);
		}
	
        grantedAuthorityList.add(new SimpleGrantedAuthority(member.getUserRole()));
    
        logger.info("# AuthProvider @@@@@@@ 2 번");
        
        return new MyAuthentication(id, password, grantedAuthorityList, member);

	}

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

