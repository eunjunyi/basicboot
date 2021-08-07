package com.mor.test.welcomelist;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mor.test.SessionInterceptor;

public class WebConfig implements WebMvcConfigurer {

	/** * Add Index Page */
	//@Override 
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.jsp");
	}
	
	
	@Autowired
	private SessionInterceptor sessionInterceptor;
	
	/*@Autowired
	private ABCFilter abcFilter;*/

	/** Register intercepter **/
	//@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> skipUris = new ArrayList<String>();
		String[] uris = {};//PropertiesUtil.getString("session.skip.uri").split(",");

		for (String uri : uris) {
			skipUris.add(uri);
		}
		sessionInterceptor.setSkipUris(skipUris);
		registry.addInterceptor(sessionInterceptor);
		// .addPathPatterns("/*");
		// .excludePathPatterns(skipUris);
	}
}

