package com.mor.test.welcomelist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mor.test.session.JwtSessionManager;
import com.mor.test.session.JwtSessionManager.USER_ROLE;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // 검쯩을 위한 구현
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();
        JwtAuthentication jwtAuthentication = (JwtAuthentication)authentication;
        System.out.println("jwtAuthenticationjwtAuthentication:"+jwtAuthentication);
        

        UserDetails accountContext = userDetailsService.loadUserByUsername(jwtAuthentication.getToken());

        // password 일치하지 않으면!
//        if(!passwordEncoder.matches(password,accountContext.getAccount().getPassword())){
//            throw new BadCredentialsException("BadCredentialsException");
//        }

        List<GrantedAuthority> roles= new ArrayList<GrantedAuthority>();
		roles.add(USER_ROLE.ADMIN.get());
		roles.add(USER_ROLE.USER.get());
		
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                		accountContext.getUsername(),
                null,
                roles);

        return authenticationToken;
    }

    // 토큰 타입과 일치할 때 인증
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
