package com.mor.test.security.comm.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mor.test.comm.jwt.JwtAuthenticationFilter;
import com.mor.test.comm.jwt.JwtTokenProvider;
import com.mor.test.sess.security.handlers.AuthFailureHandler;
import com.mor.test.sess.security.handlers.AuthProvider;
import com.mor.test.sess.security.handlers.AuthSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter{
	
	//토큰시사용
    private final JwtTokenProvider jwtTokenProvider;
    
    private final boolean isToken = false; //로그인페이시사용(session):false, 토큰인증사용 true
    
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    private final static boolean isUseToken = true;
    
	@Autowired 
	AuthProvider authProvider;
 
	@Autowired 
	AuthSuccessHandler authSuccessHandler;
	
	@Autowired 
	AuthFailureHandler authFailureHandler;

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.authenticationProvider(authProvider);
    //}
    
	@Resource(name="userServiceImpl") 
    private UserDetailsService UserServiceImpl;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 사용자 세부 서비스를 설정하기 위한 오버라이딩이다.
        auth.userDetailsService(UserServiceImpl);
    }    
    
    @Override
	protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("##################### configure #####################");
    	if(isUseToken) {
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
    	else {
	    	//CROF 설정을 해제합니다
	    	//초기 개발시에만 설정합니다
	    	http.csrf().disable();
	    	System.out.println("################################cocococococococofog");
	    	http.authorizeRequests() 
	    		.antMatchers("/user/**").access("hasRole('ROLE_USER')")	// /user/** 경로의 경우 ROLE_USER의 권한을 가진 경우에 허용한다
		    	.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")	 // /admin/** 경로의 경우 ROLE_ADMIN의 권한을 가진 경우에 허용한다
		    	//.antMatchers("/")
		    	
		    	////누구나 접속할 수 있는 페이지이기 때문에 누구나 접근이 가능하다 (.permitAll())
		    	.antMatchers("/home"
		    			,"/content/**" 
		    			,"/login/login"
		    			,"/spring/login/login"
		    			, "/login/login-error"
		    			, "/join/join"
		    			, "/join/idCheck"
		    			, "/login/find"
		    			, "/join/insert"
		    	).permitAll()
		    	.antMatchers("/**").authenticated();  //기타 /** 의 경로는 인증을 필요로 한다
	    	
	    	http.formLogin() 
	    		//.loginPage("/")
	    		.loginPage("/login/login")  //로그인 페이지는 /, /login (같은 페이지)두 페이지 에서 로그인을 실행할 것이다
	    		.loginProcessingUrl("/login/login-processing") //로그인 버튼을 누를시 /login-processing 경로로 
	    		.usernameParameter("id")  //로그인시 파라미터로 "id", "password"를 받습니다
		    	.passwordParameter("password") //로그인시 파라미터로 "id", "password"를 받습니다
		    	//.defaultSuccessUrl("/home",true) //로그인이 성공할 경우 기본 페이지는 /home 
		    	//.failureUrl("/login-error") //로그인을 실패 할 경우 /login-error
		        .failureHandler(authFailureHandler) //로그인 실패시 수행하는 클래스
		        .successHandler(authSuccessHandler); // 로그인 성공시 수행하는 클래스
	    	
	    	http.logout() 
		    	.logoutRequestMatcher(new AntPathRequestMatcher("/login/logout")) //logout 경로로 요청이 들어올 경우 /로 리다이렉트 하고 세션 초기화
		    	.logoutSuccessUrl("/login/login")  //    /로 리다이렉트 하고
		    	.invalidateHttpSession(true); // 세션 초기화
    	}
    }
        
    
    //JSP의 리소스 파일이나 자바스크립트 파일이 저장된 경로는 무시를 한다
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
        .antMatchers("/api/**", "/resources/**");
    }
  
}
