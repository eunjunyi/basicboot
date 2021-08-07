package com.mor.test.comm.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ConfigurationProperties(prefix="spring.datasource."+"testdb1")
@EnableJpaRepositories( //JPA 설정
  entityManagerFactoryRef="testdb1"+"EntityManagerFactory",
  transactionManagerRef="testdb1"+"TransactionManager",
  basePackages = {AbstractDatabaseConfig.BASE_ENTITY_PACKAGE_PREFIX + ".base"}
)
@MapperScan(  //Mybatis 설정
    basePackages = {
        AbstractDatabaseConfig.BASE_MAPPER_PACKAGE_PREFIX + ".base",
        "com.mor.test.sess.login.dao"
    }
)
public class PrimaryDataSource extends AbstractDatabaseConfig{
	final String name = "testdb1";
    
    @Bean(name = name + "DataSource")
    @Primary
    public DataSource dataSource() {
      return new LazyConnectionDataSourceProxy(new HikariDataSource(this));
    }
    
    /***********************************************************************/
    /* ----------------------JPA 셋팅------------------------------------- */
    /***********************************************************************/
    @Bean(name = name + "EntityManagerFactory")
    @Primary
    public EntityManagerFactory entityManagerFactory(@Qualifier(name + "DataSource") DataSource dataSource) {
      LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
      factory.setDataSource(dataSource);
      factory.setPackagesToScan(AbstractDatabaseConfig.BASE_ENTITY_PACKAGE_PREFIX + ".base", "com.mor.test.sess.login.dao" );
      factory.setPersistenceUnitName(name);        
      setConfigureEntityManagerFactory(factory);
      return factory.getObject();
    }
    
    @Bean(name = name + "TransactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier(name + "EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
      JpaTransactionManager tm = new JpaTransactionManager();
      tm.setEntityManagerFactory(entityManagerFactory);
    
      return tm;
    }
    /*----------------------------------------------------------------------------------------*/
    /*
    @ConfigurationProperties(prefix = "spring.datasource." + "testdb1") : application.yaml에서 어떤 properties를 읽을 지 지정합니다. 
    @EnableJpaRepositories(...) : Jpa에 관한 설정 및 파일의 위치는 어디에 있는 지 명시합니다. 
    @Bean(...) : 각 메소드들을 빈으로 등록하며 빈의 이름을 지정하여 동일한 클래스일지라도 @Qulifier를 통한 선택적 주입이 가능하게 합니다.
    @Primary : 첫 째 DB소스는 무엇인 지 명시합니다. 이 후 생성될 DB는 @Primary가 없어야 합니다. 
    */
    
    /***********************************************************************/
    /* -----------------mybatis 셋팅-------------------------------------- */
    /***********************************************************************/
    @Bean(name = name + "SessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier(name + "DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        setConfigureSqlSessionFactory(sessionFactoryBean, dataSource);
     
        return sessionFactoryBean.getObject();
    }
     
    @Bean(name = name + "SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier(name + "SessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    @Bean(name=name+"MytransactionManager")
    public DataSourceTransactionManager mytransactionManager(@Autowired @Qualifier(name + "DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    /*
    Mybatis의 경우 SessionFactory를 통해 SqlSession이 Datasource를 물고 있습니다. 
    */
    
    
    /***********************************************************************/
    /*###############  여러개의트랜젝션을 chanin 으로 묶어서  ###############*/
    /* 통합트랜젝션 메니저를 만들고 사용할수 있음. 트랜젝션을 하나로묶음<중요> #########*/
    /***********************************************************************/
    @Bean (name = name + "multiTransactionManager")   
    @Primary
    @Autowired
    public PlatformTransactionManager transactionManager(@Qualifier(name + "TransactionManager") PlatformTransactionManager firstTxManager, @Qualifier(name+"MytransactionManager") PlatformTransactionManager secondTxManager)
    {
        return new ChainedTransactionManager(firstTxManager, secondTxManager);
    }
    
}
