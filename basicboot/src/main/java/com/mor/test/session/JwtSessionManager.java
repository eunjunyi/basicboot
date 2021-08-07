package com.mor.test.session;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mor.test.comm.jwt.JwtTokenProvider;
import com.mor.test.comm.redis.UserRedisRepository;
import com.mor.test.session.vo.SessionDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtSessionManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public static final String HEADER_AUTH = "Authorization";
	
	@Autowired
    JwtTokenProvider jwtTokenProvider;
	
	public enum USER_ROLE{
		ADMIN (new GrantedAuthority() {
			public String getAuthority() {return "ROLE_ADMIN";}
		}),
		USER (new GrantedAuthority() {
			public String getAuthority() {return "ROLE_USER";}
		}),
		MANAGER (new GrantedAuthority() {
			public String getAuthority() {return "ROLE_MANAGER";}
		});
		
		private final GrantedAuthority role;
		USER_ROLE(GrantedAuthority role) {
			this.role =role;
		}
		public GrantedAuthority get() {
			return this.role;
		}
		
		public String  toString() {return role.getAuthority();}
	}
	
	@Autowired
	UserRedisRepository userRedisRepository;
	
	//@Autowired
	//AuthenticationManager authenticationManager;
	
	
	/**
	 * 사용자 세션 관리
	 * 추후 디비로 관리 될수  있음.
	 * String token
	 */
	
	public String login(Map userinfo) {
		
		String userId = (String)userinfo.get("user_id");
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String prejwt = request.getHeader(JwtSessionManager.HEADER_AUTH);
		System.out.println("isUsable(prejwt):"+isSessionUsable(prejwt));
		//Bearer
		//사전로그인체크 ///
		if(prejwt!=null && isSessionUsable(prejwt)) {
			System.out.println("이미 로그인");
			return prejwt;
		}
		//필수정보세팅.
		Map claims = new HashMap();
		claims.put("user_id", userId);
		claims.put("user_nm", userinfo.get("user_nm"));
				 
		//소멸시간 밤12시로 세팅
		String jwt = jwtTokenProvider.createToken(claims, null);
				
		
		SessionDto sessionDto = new SessionDto();
		sessionDto.setToken(jwt);
		sessionDto.setDeptId((String)userinfo.get("dept_cd")); //부서
		sessionDto.setDevice((String)userinfo.get("device")); //접속장비
		sessionDto.setUserNm((String)userinfo.get("user_nm")); //부서
		sessionDto.setUserId((String)userinfo.get("user_id")); //userId
		
		userRedisRepository.save(sessionDto);
		
		return jwt;
	}
	
	//$ redis-cli
	//> CONFIG SET protected-mode no


	//로그아웃처리.
	public void logout(String jwt) {
		logger.debug("\nlogout#############################");
		userRedisRepository.deleteById(jwt);
	}
	
	public void logout() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader(HEADER_AUTH);//"Authorization"
		logout(jwt);
	}
	
	
	public boolean isNotUsable(String jwt) {
		return !isSessionUsable( jwt);
	}
	
	public boolean isSessionUsable(String jwt) {
		try{
			logger.debug("\nGLOBAL_SESSION_MAP.containsKey(jwt):"+jwt);
			
			Optional<SessionDto> userOption =  userRedisRepository.findById(jwt);
			
			if( jwtTokenProvider.validateToken(jwt) || !userOption.isPresent()) {  //&& redis 에 미존재 token인 경우.
				return false;
			}
			
			SessionDto sessionDto = userOption.get();
			
			
			return true;
		}catch (Throwable e) {
			e.printStackTrace();
			return false;
			
			/* 1) ExpiredJwtException : JWT를 생성할 때 지정한 유효기간 초과할 때.
				2) UnsupportedJwtException : 예상하는 형식과 일치하지 않는 특정 형식이나 구성의 JWT일 때
				3) MalformedJwtException : JWT가 올바르게 구성되지 않았을 때
				4) SignatureException :  JWT의 기존 서명을 확인하지 못했을 때
				5) IllegalArgumentException*/
			/*
			 * if(e.getCause() !=null) { if(e.getCause() instanceof ExpiredJwtException) {
			 * throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString());
			 * }else if(e.getCause() instanceof UnsupportedJwtException) { throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString());
			 * }else if(e.getCause() instanceof MalformedJwtException) { throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString());
			 * }else if(e.getCause() instanceof SignatureException) { throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString());
			 * }else { throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString()); }
			 * } throw new
			 * BusinessException("A001","계정 권한이 유효하지 않습니다.\\n다시 로그인을 해주세요",e.toString());
			 */
		}
	}
	
	public Map<String, Object> getCliam(String key) {
		Claims claims = jwtTokenProvider.getCliamsBody(key);
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (HashMap<String, Object>)claims.get(key);
		return value;
	}
	
	
	public Claims getCliams() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String jwt = request.getHeader(HEADER_AUTH);//"Authorization"
		return jwtTokenProvider.getCliamsBody(jwt);
	}
	
	
    //유효기간 확인
    private Boolean isTokenExpired() {
        return extractExpiration().before(new Date());
    }
    
    public Date extractExpiration() {
        return extractClaim(Claims::getExpiration);
    }
    
    public <T> T extractClaim(Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getCliams());
    }
    
    public boolean isUsable() {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    	String prejwt = request.getHeader(JwtSessionManager.HEADER_AUTH);
    	return isSessionUsable(prejwt);
    }

}
