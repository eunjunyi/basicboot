package com.mor.test.comm.config;

import javax.servlet.http.HttpServlet;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mor.test.comm.servletlistener.HelloStateServlet;

@Configuration
public class WebConfig {
	@Bean	
    public ServletRegistrationBean<HttpServlet> stateServlet() {
	   ServletRegistrationBean servRegBean = new ServletRegistrationBean(new HelloStateServlet());
	   servRegBean.setServlet(new HelloStateServlet());
	   servRegBean.addUrlMappings("/state/*");
	   servRegBean.setLoadOnStartup(1);
	   return servRegBean;
    }   
}
