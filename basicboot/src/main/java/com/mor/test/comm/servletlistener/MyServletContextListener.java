package com.mor.test.comm.servletlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Value;

public class MyServletContextListener implements ServletContextListener {
	

     
	  public void contextInitialized(ServletContextEvent e) {
		  
		
		  
	      System.out.println("MyServletContextListener Context Initialized");
	  }

	  public void contextDestroyed(ServletContextEvent e) {
		  
	    System.out.println("MyServletContextListener Context Destroyed");
	  }
}
