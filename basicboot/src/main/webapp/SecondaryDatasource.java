package com.mor.test.datasource;

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

@Configuration
@MapperScan(basePackages = "com.mor.test.dao.secondary"  ,sqlSessionFactoryRef = "secondarySqlSessionFactory")
public class SecondaryDatasource {
 
	@Value("${spring.secondary.datasource.driverClassName}") private String driverClassName;
    @Value("${spring.secondary.datasource.jdbc-url}") private String url;
    @Value("${spring.secondary.datasource.username}") private String username;
    @Value("${spring.secondary.datasource.password}") private String password;
    
    
    @Bean(name="secondaryDataSource")
    @ConfigurationProperties(prefix="spring.secondary.datasource")
    public DataSource dataSource() {
    	System.out.println("secondaryDatasource username:"+username);
    	DataSource datasource =  DataSourceBuilder
    		        .create()
    		        .username(username)
    		        .password(password)
    		        .url(url)
    		        .driverClassName(driverClassName)
    		        .build();
    	 
    	System.out.println("secondaryDatasource datasource:"+datasource);
        return datasource;
    }
 
    @Bean(name="secondarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Autowired @Qualifier("secondaryDataSource") DataSource dataSource, ApplicationContext applicationContext)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:com/mor/test/dao/secondary/*.xml"));
        return factoryBean.getObject();
    }
 
    @Bean(name="secondarySqlSession")
    public SqlSessionTemplate sqlSession(@Autowired @Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
 
    @Bean(name="secondarytransactionManager")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
 
}
