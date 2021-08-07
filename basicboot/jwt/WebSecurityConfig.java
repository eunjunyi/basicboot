package com.mor.test.welcomelist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.mor.test.session.JwtSessionManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtSessionManager jwtSessionManager;
    
    
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setPasswordEncoder(passwordEncoder);
      provider.setUserDetailsService(userDetailsService);
      return provider;
   }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
     // auth.authenticationProvider(daoAuthenticationProvider());
      auth.authenticationProvider(authenticationProvider());
    }
    
    
    @Override

    /*
     *  .authorizeRequests() : 요청에 대한 권한을 지정할 수 있다.
        .anyRequest().authenticated() : 인증이 되어야 한다는 이야기이다.
        .anonymous() : 인증되지 않은 사용자도 접근할 수 있다.
        .fullyAuthenticated() : 완전히 인증된 사용자만 접근할 수 있다.
        .hasRole() or hasAnyRole() : 특정 권한을 가지는 사용자만 접근할 수 있다.
        .hasAuthority() or hasAnyAuthority() : 특정 권한을 가지는 사용자만 접근할 수 있다.
        .hasIpAddress() : 특정 아이피 주소를 가지는 사용자만 접근할 수 있다.
        .access() : SpEL? 표현식에 의한 결과에 따라 접근할 수 있다.
        .not() : 접근 제한 기능을 해제
        .permitAll() or denyAll() : 접근을 전부 허용하거나 제한한다.
        .rememberMe() : 로그인한 사용자만 접근할 수 있다. 리멤버기능 
     */
    protected void configure(HttpSecurity http) throws Exception {
    	
    	
        http.csrf().disable();
        http.addFilterAfter(new JwtAuthenticationFilter(jwtSessionManager), SecurityContextPersistenceFilter.class);
        http.authorizeRequests()
            .antMatchers("/","/redistest", "/index.jsp", "/home", "/favicon.ico", "/resources/**", "/publish/**").permitAll()
            .antMatchers("/user/**", "/manage/**", "/admin/**", "/comment/admin/**").hasRole("ADMIN")
            .antMatchers("/user/**", "/manage/**").hasRole("MANAGER")
            .antMatchers("/user/**").hasRole("USER")
            .anyRequest().authenticated();
            
        
               // .anyRequest().authenticated()
               // .and()
               // .logout().permitAll()
               // .and()
               // .formLogin();
    }
  
//    http
//    .csrf().disable()
//    .authorizeRequests()
//        .antMatchers("/", "/home").permitAll()
//        .anyRequest().authenticated()
//        .and()
//    .formLogin()
//        .loginPage("/login")
//        .loginProcessingUrl("/auth/login")
//        .usernameParameter("username")
//        .passwordParameter("password")
//        .permitAll()
//        .and()
//    .logout()
//        .permitAll();
//


    
    
  @Bean 
  public PasswordEncoder passwordEncoder() {
      //간단하게 비밀번호 암호화 
      return new BCryptPasswordEncoder(); 
  }
}
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    private AuthenticationProvider authenticationProvider; 
//    public WebSecurityConfig(/*UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,*/ 
//            AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }
//    
//    /* * 스프링 시큐리티가 사용자를 인증하는 방법이 담긴 객체. */ 
//    @Override 
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception { 
//        /* * AuthenticationProvider 구현체 */ 
//        auth.authenticationProvider(authenticationProvider); 
//        // auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); 
//    }
//    
//    @Bean
//    public AuthenticationManager Authenticationmanagerbean () throws Exception {
//        return super.authenticationManager();
//    }
//    
//    /* * 스프링 시큐리티 룰을 무시하게 하는 Url 규칙(여기 등록하면 규칙 적용하지 않음) */ 
//    @Override 
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring() 
//        .antMatchers("/resources/**") 
//        .antMatchers("/css/**") 
//        .antMatchers("/vendor/**") 
//        .antMatchers("/js/**") 
//        .antMatchers("/favicon*/**") 
//        .antMatchers("/img/**") ; 
//    }
//
//    /* * 스프링 시큐리티 규칙 */ 
//    @Override 
//    protected void configure(HttpSecurity http) throws Exception { 
//        //http.authorizeRequests()//보호된 리소스 URI에 접근할 수 있는 권한을 설정 
//        //.antMatchers("/login*/**").permitAll() //전체 접근 허용 
//        //.antMatchers("/logout/**").permitAll() 
//        //.antMatchers("/myPage").hasRole("ADMIN")//admin이라는 롤을 가진 사용자만 접근 허용
//        //.antMatchers("/chatbot/**").permitAll() 
//        //.anyRequest().authenticated() 
//        //.and().logout() .logoutUrl("/logout") 
//        //.logoutSuccessHandler(logoutSuccessHandler())
//        //.and().csrf()//csrf 보안 설정을 비활성화
//        //.disable()//해당 기능을 사용하기 위해서는 프론트단에서 csrf토큰값 보내줘야함 
//        //.addFilter(jwtAuthenticationFilter())//Form Login에 사용되는 custom AuthenticationFilter 구현체를 등록 
//        //.addFilter(jwtAuthorizationFilter())//Header 인증에 사용되는 BasicAuthenticationFilter 구현체를 등록 
//        //.exceptionHandling() 
//        //.accessDeniedHandler(accessDeniedHandler())
//        //.authenticationEntryPoint(authenticationEntryPoint()) ; 
//    }
//
//    /* * SuccessHandler bean register */ 
//    @Bean 
//    public AuthenticationSuccessHandler authenticationSuccessHandler() { 
//        CustomAuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler(); 
//        successHandler.setDefaultTargetUrl("/index"); 
//        return successHandler; 
//    }
//    
//    /* * FailureHandler bean register */ 
//    @Bean 
//    public AuthenticationFailureHandler authenticationFailureHandler() { 
//        CustomAuthenticationFailureHandler failureHandler = new CustomAuthenticationFailureHandler(); 
//        failureHandler.setDefaultFailureUrl("/loginPage?error=error"); 
//        return failureHandler; 
//    } 
//    /* * LogoutSuccessHandler bean register */ 
//    @Bean 
//    public LogoutSuccessHandler logoutSuccessHandler() { 
//        CustomLogoutSuccessHandler logoutSuccessHandler = new CustomLogoutSuccessHandler(); 
//        logoutSuccessHandler.setDefaultTargetUrl("/loginPage?logout=logout"); 
//        return logoutSuccessHandler; 
//    } 
//    /* * AccessDeniedHandler bean register */ 
//    @Bean 
//    public AccessDeniedHandler accessDeniedHandler() { 
//        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler(); 
//        accessDeniedHandler.setErrorPage("/error/403"); return accessDeniedHandler; 
//    } 
//    /* * AuthenticationEntryPoint bean register */ 
//    @Bean 
//    public AuthenticationEntryPoint authenticationEntryPoint() { 
//        return new CustomAuthenticationEntryPoint("/loginPage?error=e"); 
//    } 
//    /* * Form Login시 걸리는 Filter bean register */ 
//    @Bean 
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception { 
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
//        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
//        jwtAuthenticationFilter.setUsernameParameter("username"); 
//        jwtAuthenticationFilter.setPasswordParameter("password"); 
//        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
//        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler()); 
//        jwtAuthenticationFilter.afterPropertiesSet(); return jwtAuthenticationFilter; 
//    }
//    /* * Filter bean register */ 
//    @Bean 
//    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception { 
//        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager()); 
//        return jwtAuthorizationFilter; 
//    } 
//    @Bean 
//    public PasswordEncoder passwordEncoder() {
//        //간단하게 비밀번호 암호화 
//        return new BCryptPasswordEncoder(); 
//    }

//}
