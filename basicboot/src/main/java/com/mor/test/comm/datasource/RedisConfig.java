package com.mor.test.comm.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {
	
	@Value("${spring.redis.host}") 
	private String redisHost; 
	
	@Value("${spring.redis.port}") 
	private int redisPort;

		
	@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
		System.out.println("redisHost:"+redisHost);
		System.out.println("redisPort:"+redisPort);
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
		return lettuceConnectionFactory;
    }
	@Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
} 
