package com.mor.test.comm.jwt;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private final long TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 10; // 10시간

    @Value("spring.jwt.secret")
    private String secretKey;
    

    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Qualifier("userUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public long betweenMillis() {
		
		int closeTime = 4;
		LocalDateTime nowtime = LocalDateTime.now(); 
		//Asia/Seoul
		int hour = nowtime.getHour(); 
		int plusDay= 0;
		if(hour >closeTime) plusDay++;
		LocalDateTime endtime =	LocalDateTime.of(nowtime.plusDays(plusDay).toLocalDate(),LocalTime.of(closeTime,0,0));
		Duration duration = Duration.between(nowtime, endtime);
		return duration.toMillis();
	}

    // Jwt 토큰 생성
    public String createToken(Map<String,String> inMap, List roles) {
    	
    	//필수정보세팅.
		//claims.put("user_id", userId);
		//claims.put("user_nm", userinfo.get("user_nm"));
    			
    	String userId = inMap.get("user_id");
    	
        Claims claims = Jwts.claims().setSubject(userId);
        if(roles!=null) claims.put("roles", roles);
        claims.put("user", inMap);
        
        Date now = new Date();
        return Jwts.builder()
        		.setHeaderParam("typ", "JWT")
        		.setHeaderParam("alg", SignatureAlgorithm.HS512)
        		.setSubject(userId)
                .setClaims(claims) // 데이터
                .setIssuedAt(new Date())   // 토큰 발행 일자
                .setExpiration(new Date(now.getTime() + betweenMillis())) // 만료 기간
                .signWith(SignatureAlgorithm.HS512, secretKey) // 암호화 알고리즘, secret 값 
                .compact(); // Token 생성
    }
    
    public Claims getCliamsBody(String jwt) {
		Jws<Claims> claims = null;
		try {
			claims = Jwts.parser()
					 .setSigningKey(secretKey)
					 .parseClaimsJws(jwt);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e) ;
		}
		return claims!=null?claims.getBody():null;
	}
    

    // 인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
       // UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    	 UserDetails userDetails = userDetailsService.loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt Token에서 User PK 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("Authorization");
    }

    // Jwt Token의 유효성 및 만료 기간 검사
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}