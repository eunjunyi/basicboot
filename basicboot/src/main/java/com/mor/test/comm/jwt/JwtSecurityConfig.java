package com.mor.test.comm.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mor.test.sess.security.handlers.AuthFailureHandler;
import com.mor.test.sess.security.handlers.AuthProvider;
import com.mor.test.sess.security.handlers.AuthSuccessHandler;

//@Configuration
//@EnableWebSecurity
public class JwtSecurityConfig {//extends WebSecurityConfigurerAdapter {
/*
	//토큰시사용
    private final JwtTokenProvider jwtTokenProvider;
    
    private final boolean isToken = false; //로그인페이시사용(session):false, 토큰인증사용 true
    
    
    @Autowired 
	AuthProvider authProvider;
 
	@Autowired 
	AuthSuccessHandler authSuccessHandler;
	
	@Autowired 
	AuthFailureHandler authFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
    
    
    
    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
    

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
           .csrf().disable();
        //토큰 사용.
        http.authorizeRequests()
        .antMatchers("/","/redistest", "/index.jsp", "/home", "/favicon.ico", "/resources/**", "/publish/**").permitAll()
        .antMatchers("/user/**", "/manage/**", "/admin/**", "/comment/admin/**").hasRole("ADMIN")
        .antMatchers("/user/**", "/manage/**").hasRole("MANAGER")
        .antMatchers("/user/**").hasRole("USER")
        .anyRequest().authenticated() //그외 모두 인증필요
        .and().addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);	
       
    }
    
    //JSP의 리소스 파일이나 자바스크립트 파일이 저장된 경로는 무시를 한다
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers("/api/**", "/resources/**");
    }
    */
}