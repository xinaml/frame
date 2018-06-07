package com.xinaml.frame.common.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@PropertySource({"classpath:redis.properties"}) //加载配置文件
@Configuration
public class RedisConf {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.minIdle}")
    private Integer minIdle;

    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${redis.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${redis.connectTimeOut}")
    private Integer connectTimeOut;

    @Value("${redis.soTimeOut}")
    private Integer soTimeOut;

    @Value("${redis.maxAttempts}")
    private Integer maxAttempts;

    @Bean
    public  JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setTestOnBorrow(testOnBorrow);
        return  config;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig config){
        return new JedisPool(config,host,port,connectTimeOut,password);
    }


    @Bean
    public Jedis jedis(JedisPool jedisPool) {
        Jedis jedis =jedisPool.getResource();
        return  jedis;
    }


}
