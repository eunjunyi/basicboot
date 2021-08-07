package com.mor.test.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mor.test.comm.config.MyConfig;
import com.mor.test.comm.jwt.JwtTokenProvider;
import com.mor.test.comm.redis.UserRedisRepository;
import com.mor.test.sess.security.service.SecurityService;
import com.mor.test.session.JwtSessionManager;
import com.mor.test.session.vo.SessionDto;

@Controller
public class SessonTestController {
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
//		@Value("${spring.datasource.driverClassName}") private String driverClassName;
//	    @Value("${spring.datasource.jdbc-url}") private String url;
//	    @Value("${spring.datasource.username}") private String username;
	    
	    @Autowired
	    MyConfig config;
	
	    @Autowired
	    UserRedisRepository userRedisRepository;
	    
	    @Autowired
	    JwtSessionManager jwtSessionManager;
	    
	    @Autowired
	    JwtTokenProvider jwtTokenProvider;
	    
	    @Autowired
	    SecurityService securityService;
		/*
		 * @Autowired BaseAjaxDao dao;
		 * 
		 * @Autowired PrimaryAjaxDao primaryAjaxDao;
		 * 
		 * @Autowired SecondaryAjaxDao secondaryAjaxDao;
		 */
	
        @RequestMapping("/user/check")
		@ResponseBody
		public Map usercheck(@RequestParam Map<String, String> param) {
		    System.out.println("user check:"+param);
		    boolean token = jwtSessionManager.isUsable();
		    System.out.println("tokentokentokentoken:"+token);
		
			Map retMap = new HashMap();
			retMap.put("result","user check");
			return retMap;
		}
	    
	    @RequestMapping("/manage/check")
		@ResponseBody
		public Map managecheck(@RequestParam Map<String, String> param) {
		    System.out.println("manage check:"+param);
		    boolean token = jwtSessionManager.isUsable();
		System.out.println("tokentokentokentoken:"+token);
		
			Map retMap = new HashMap();
			retMap.put("result","manage check");
			return retMap;
		}
	    @RequestMapping("/admin/check")
		@ResponseBody
		public Map admincheck(@RequestParam Map<String, String> param) {
		    System.out.println("admin check:"+param);
		    boolean token = jwtSessionManager.isUsable();
		    System.out.println("tokentokentokentoken:"+token);
		
			Map retMap = new HashMap();
			retMap.put("result","admin check");
			return retMap;
		}
	    
	    @RequestMapping("/redistest")
		@ResponseBody
		public SessionDto login(@RequestParam Map<String, String> param) {
		    System.out.println("redistest:"+param);
		    Map userinfo = new HashMap<String,String>(); 
		    userinfo.put("user_id",param.get("user_id"));
		    userinfo.put("device","device");
		    userinfo.put("user_nm","방가방");
		    userinfo.put("dept_cd","D00001");
		    String userId = (String)userinfo.get("user_id");

		    
		    
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String prejwt = request.getHeader(JwtSessionManager.HEADER_AUTH);
			System.out.println("prejwt111:"+prejwt);
			
			if(prejwt!=null) {
				Optional<SessionDto> sesOption =userRedisRepository.findById(prejwt);
				System.out.println("isUsable(prejwt):"+sesOption.isPresent());
				//사전로그인체크 ///
				if(prejwt!=null && sesOption.isPresent()) {
					System.out.println("이미 로그인");
					return (SessionDto)SecurityContextHolder.getContext().getAuthentication().getDetails();
				}
			}
			//Bearer
			
			
			System.out.println("아니야");
			//필수정보세팅.
			Map claims = new HashMap();
			claims.put("user_id", userId);
			claims.put("user_nm", userinfo.get("user_nm"));
			
			
			//test
			SessionDto dto = (SessionDto)securityService.loadUserByUsername(userId);
			System.out.println("#####SessionDto########"+dto);
			
			
			//소멸시간 밤12시로 세팅
			String jwt = jwtTokenProvider.createToken(claims, null);
			System.out.print("jwt:"+jwt);
			//mysql 디비에서 아이디패스워드 인증.및 권한획득.
			SessionDto sessionDto = new SessionDto();
			sessionDto.setToken(jwt);
			sessionDto.setDeptId((String)userinfo.get("dept_cd")); //부서
			sessionDto.setDevice((String)userinfo.get("device")); //접속장비
			sessionDto.setUserNm((String)userinfo.get("user_nm")); //부서
			sessionDto.setUserId((String)userinfo.get("user_id")); //userId
			sessionDto.setUserId((String)userinfo.get("user_id")); //userId
			sessionDto.setUserRole("USER|MANAGER"); //userId
			
			System.out.print("sessionDto:"+sessionDto);
			
			userRedisRepository.save(sessionDto);
		    
		    //String token = jwtSessionManager.login(userinfo);
			
			Optional<SessionDto> uu = userRedisRepository.findById(jwt);
			System.out.print("UserDto:"+uu.get());
			return uu.get();
		}
	    
	    
	    @RequestMapping("/check")
		@ResponseBody
		public SessionDto check(@RequestParam Map<String, String> param) {
		    System.out.println("check:"+param);
		    boolean token = jwtSessionManager.isUsable();
		System.out.println("tokentokentokentoken:"+token);
		
		/*
		 * Authentication authentication =
		 * SecurityContextHolder.getContext().getAuthentication();
		 * if(authentication!=null) { JwtAuthentication jwtauthentication =
		 * (JwtAuthentication)authentication; SessionDto dto =
		 * (SessionDto)jwtauthentication.getDetails(); System.out.print("dto:"+dto);
		 * return dto; }
		 */
		    
			return new SessionDto();
			
		}
	    
	    
	    @RequestMapping("/logout1")
		@ResponseBody
		public String logout(@RequestParam Map<String, String> param) {
		    System.out.println("redistest:"+param);
		    
		    jwtSessionManager.logout();
		    
			return "";
		}
	    
	    @RequestMapping("/deleteAll")
		@ResponseBody
		public Map deleteAll(@RequestParam Map<String, String> param) {
		    System.out.println("deleteAll:"+param);
		    Map<String,String> retMap = new HashMap();
		    Iterable<SessionDto> iter = userRedisRepository.findAll();
		    System.out.println("iteriteriteriter:"+iter);
		    iter.forEach(s->{System.out.println("========>"+s.getUserId()+"  "+s.getAuthorities());});
		    
		    userRedisRepository.deleteAll();
		    //Iterable<SessionDto> iter = userRedisRepository.findAll();
		   // iter.forEach(s->userRedisRepository.deleteAll(););
		    
		    retMap.put("RES", "GOOD");
			return retMap;
		}
	    
	    @RequestMapping("/findall")
		@ResponseBody
		public Map findall(@RequestParam Map<String, String> param) {
		    System.out.println("findall:"+param);
		    Map<String,String> retMap = new HashMap();
		    Iterable<SessionDto> iter = userRedisRepository.findAll();
		    System.out.println("iteriteriteriter:"+iter);
		    iter.forEach(s->{System.out.println("========>"+s.getUserId()+"  "+s);});
		    
		    retMap.put("RES", "GOOD");
			return retMap;
		}
	    
		/*
		 * @RequestMapping("/insert")
		 * 
		 * @ResponseBody public Map insertTEST() {
		 * System.out.println("driverClassName:"+driverClassName);
		 * System.out.println("url:"+url); System.out.println("username:"+username);
		 * logger.debug("############## \n" +config.getList());
		 * 
		 * Map inMap = new HashMap(); inMap.put("title","베이스타이틀1");
		 * inMap.put("auth","베이스1"); inMap.put("content","베이스내용1");
		 * dao.insertAjaxTest(inMap);
		 * 
		 * inMap.put("title","프라이머리타이틀1"); inMap.put("auth","프라이1");
		 * inMap.put("content","프라이머리내용이다1"); primaryAjaxDao.insertAjaxTest(inMap);
		 * 
		 * inMap.put("title","세컨더리타이틀1"); inMap.put("auth","세컨더리1");
		 * inMap.put("content","세컨더리내용이다1"); primaryAjaxDao.insertAjaxTest(inMap);
		 * 
		 * if(true) throw new RuntimeException("dhfb"); Map retmaP = new HashMap();
		 * retmaP.put("이름","헬로"); return retmaP; }
		 * 
		 * 
		 * @RequestMapping("/hello1")
		 * 
		 * @ResponseBody public Map getHELLOW1() {
		 * logger.debug("driverClassName:"+driverClassName); logger.debug("url:"+url);
		 * logger.debug("username:"+username); logger.debug("##############\n"
		 * +config.getList());
		 * 
		 * List<Map> list = dao.listAjaxTest(new HashMap());
		 * System.out.println("########  list  ######\n" +list);
		 * 
		 * List<Map> list1 = primaryAjaxDao.listAjaxTest(new HashMap());
		 * System.out.println("########  primaryAjaxDao  ######\n" +list);
		 * 
		 * List<Map> list2 = secondaryAjaxDao.listAjaxTest(new HashMap());
		 * System.out.println("########  secondaryAjaxDao  ######\n" +list); Map retmaP
		 * = new HashMap(); retmaP.put("이름","헬로1"); return retmaP; }
		 * 
		 * @RequestMapping("/index") public String index(Model modle) {
		 * System.out.println("driverClassName:"+driverClassName);
		 * System.out.println("url:"+url); System.out.println("username:"+username);
		 * System.out.println("############## \n" +config.getList());
		 * modle.addAttribute("name","헬로1"); return "/index"; }
		 * 
		 * 세션체인 필터
		 * 1.SecurityContextPersistenceFilter : SecurityContextRepository에서 SecurityContext를 가져오거나 저장하는 역할을 한다. (SecurityContext는 밑에)
		   2. LogoutFilter : 설정된 로그아웃 URL로 오는 요청을 감시하며, 해당 유저를 로그아웃 처리
		   3. (UsernamePassword)AuthenticationFilter : (아이디와 비밀번호를 사용하는 form 기반 인증) 설정된 로그인 URL로 오는 요청을 감시하며, 유저 인증 처리
		   4. AuthenticationManager를 통한 인증 실행
		   5.인증 성공 시, 얻은 Authentication 객체를 SecurityContext에 저장 후 AuthenticationSuccessHandler 실행
		   6.인증 실패 시, AuthenticationFailureHandler 실행
		   7. DefaultLoginPageGeneratingFilter : 인증을 위한 로그인폼 URL을 감시한다.
		   8. BasicAuthenticationFilter : HTTP 기본 인증 헤더를 감시하여 처리한다.
		   9. RequestCacheAwareFilter : 로그인 성공 후, 원래 요청 정보를 재구성하기 위해 사용된다.
	       10. SecurityContextHolderAwareRequestFilter : HttpServletRequestWrapper를 상속한 SecurityContextHolderAwareRequestWapper 클래스로 HttpServletRequest 정보를 감싼다. SecurityContextHolderAwareRequestWrapper 클래스는 필터 체인상의 다음 필터들에게 부가정보를 제공한다.
		   11. AnonymousAuthenticationFilter : 이 필터가 호출되는 시점까지 사용자 정보가 인증되지 않았다면 인증토큰에 사용자가 익명 사용자로 나타난다.
		   12. SessionManagementFilter : 이 필터는 인증된 사용자와 관련된 모든 세션을 추적한다.
		   13. ExceptionTranslationFilter : 이 필터는 보호된 요청을 처리하는 중에 발생할 수 있는 예외를 위임하거나 전달하는 역할을 한다.
		   14. FilterSecurityInterceptor : 이 필터는 AccessDecisionManager 로 권한부여 처리를 위임함으로써 접근 제어 결정을 쉽게해준다.
			
			
			출처: https://sjh836.tistory.com/165 [빨간색코딩]
		 */
	
	    
}
