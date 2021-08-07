package com.mor.test.comm.datasource.asis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

//@Configuration
//@MapperScan(basePackages = "com.mor.test.dao.primary" ,sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDatasource {
 
	@Value("${spring.primary.datasource.driverClassName}") private String driverClassName;
    @Value("${spring.primary.datasource.jdbc-url}") private String url;
    @Value("${spring.primary.datasource.username}") private String username;
    @Value("${spring.primary.datasource.password}") private String password;
    
    @Primary
    @Bean(name="primaryDataSource")
    @ConfigurationProperties(prefix="spring.primary.datasource")
    public DataSource dataSource() {
    	DataSource datasource = DataSourceBuilder.create().build();
    	
    	System.out.println("PrimaryDatasource username:"+username);
    	/*DataSource datasource =  DataSourceBuilder
    		        .create()
    		        .username(username)
    		        .password(password)
    		        .url(url)
    		        .driverClassName(driverClassName)
    		        .build();*/
    	 
    	System.out.println("primaryDataSource datasource:"+datasource);
        return datasource;
    }
 
    @Primary
    @Bean(name="primarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("primaryDataSource") DataSource dataSource, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:com/mor/test/dao/primary/*.xml"));
        return factoryBean.getObject();
    }
 
    @Primary
    @Bean(name="primarySqlSession")
    public SqlSessionTemplate sqlSession(@Autowired @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
 
    @Primary
    @Bean(name="primarytransactionManager")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
}
