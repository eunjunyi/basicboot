package com.mor.test.comm.datasource;

import java.io.IOException;

import javax.sql.DataSource;

import org.hibernate.dialect.MyISAMStorageEngine;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.google.common.collect.ImmutableMap;
import com.zaxxer.hikari.HikariConfig;

/**
 * DB2                  org.hibernate.dialect.DB2Dialect
 * DB2 AS/400           org.hibernate.dialect.DB2400Dialect
 * DB2 OS390            org.hibernate.dialect.DB2390Dialect
 * PostgreSQL           org.hibernate.dialect.PostgreSQLDialect
 * MySQL                org.hibernate.dialect.MySQLDialect
 * MySQL with InnoDB    org.hibernate.dialect.MySQLInnoDBDialect
 * MySQL with MyISAM    org.hibernate.dialect.MySQLMyISAMDialect
 * Oracle (any version) org.hibernate.dialect.OracleDialect
 * Oracle 9i/10g        org.hibernate.dialect.Oracle9Dialect
 * Sybase               org.hibernate.dialect.SybaseDialect
 * Sybase Anywhere      org.hibernate.dialect.SybaseAnywhereDialect
 * Microsoft SQL Server org.hibernate.dialect.SQLServerDialect
 * SAP DB               org.hibernate.dialect.SAPDBDialect
 * Informix             org.hibernate.dialect.InformixDialect
 * HypersonicSQL        org.hibernate.dialect.HSQLDialect
 * Ingres               org.hibernate.dialect.IngresDialect
 * Progress             org.hibernate.dialect.ProgressDialect
 * Mckoi SQL            org.hibernate.dialect.MckoiDialect
 * Interbase            org.hibernate.dialect.InterbaseDialect
 * Pointbase            org.hibernate.dialect.PointbaseDialect
 * FrontBase            org.hibernate.dialect.FrontbaseDialect
 * Firebird             org.hibernate.dialect.FirebirdDialect
 * 
 * 

 * @author junyi
 *
 */
public class AbstractDatabaseConfig extends HikariConfig {
	
	public static final String BASE_MAPPER_PACKAGE_PREFIX = "com.mor.test.dao";
	public static final String BASE_MAPPER_PACKAGE_PATH_PREFIX = BASE_MAPPER_PACKAGE_PREFIX.replaceAll("\\.","/");
    public static final String BASE_ENTITY_PACKAGE_PREFIX = "com.mor.test.domain";
    
    //JAP 설정
	protected void setConfigureEntityManagerFactory(LocalContainerEntityManagerFactoryBean factory) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(ImmutableMap.of(
                "hibernate.hbm2ddl.auto", "update",
       //         "hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialectt",  //DB타입마다 정의해줘야함
                "hibernate.show_sql", "true",
                "hibernate.format_sql", "true"
        ));
        factory.afterPropertiesSet();
    }
	
 
	//MYBATIS 설정 com.mor.test.sess.login.dao
    protected void setConfigureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, DataSource dataSource) throws IOException {
       System.out.println("BASE_MAPPER_PACKAGE_PATH_PREFIX:"+BASE_MAPPER_PACKAGE_PATH_PREFIX);
       
    	sessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocations = resolver.getResources("classpath:"+BASE_MAPPER_PACKAGE_PATH_PREFIX+"/base/*.xml");
        sessionFactoryBean.setMapperLocations(mapperLocations);
    }
}
