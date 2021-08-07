package com.mor.test.welcomelist;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import com.mor.test.redis.UserRedisRepository;
import com.mor.test.session.JwtSessionManager;

@Service
public class JwtAuthenticationFilter extends GenericFilterBean {

	
	private JwtSessionManager jwtSessionManager;

    public JwtAuthenticationFilter(JwtSessionManager jwtSessionManager) {
        this.jwtSessionManager = jwtSessionManager;
    }
    
	@Autowired
    UserRedisRepository userRedisRepository;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String prejwt = request.getHeader(JwtSessionManager.HEADER_AUTH);
		System.out.println("jwtSessionManagerjwtSessionManagerjwtSessionManager:"+jwtSessionManager);
		System.out.println("userRedisRepository:"+userRedisRepository);
		jwtSessionManager.isUsable(prejwt);
    	
		System.out.println("실행 된다고한다");
		 try {
		    chain.doFilter(request, response);
		    logger.debug("Chain processed normally");
		  }
		  catch (IOException ex) {
		    throw ex;
		  }
		
		
	}
}
