package com.yhjr.basic.config;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisCluster配置文件
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月1日
 */
@Configuration
//@AutoConfigureAfter(RedisClusterProperties.class)
@ConditionalOnBean(RedisClusterProperties.class)
//@EnableConfigurationProperties(RedisClusterProperties.class)
public class JedisClusterConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConfig.class);
    
    @Autowired
    private RedisClusterProperties redisClusterProperties;
    
    @Bean
    @ConditionalOnMissingBean
    public JedisCluster jedisCluster() {
        String[] serverArray = redisClusterProperties.getClusterNodes().split(",");
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
         for (String ipPort : serverArray) {
             String[] ipPortPair = ipPort.split(":");
             jedisClusterNodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
         }
         JedisCluster jedisCluster = null;
         if(StringUtils.isBlank(redisClusterProperties.getPassword())){
             jedisCluster = new JedisCluster(jedisClusterNodes,redisClusterProperties.getConnectionTimeout(),
                     redisClusterProperties.getSoTimeout(),redisClusterProperties.getMaxAttempts(),jedisPoolConfig());
         }else{
             //带密码认证的配置方式
             jedisCluster = new JedisCluster(jedisClusterNodes, redisClusterProperties.getConnectionTimeout(), 
                     redisClusterProperties.getSoTimeout(), redisClusterProperties.getMaxAttempts(), redisClusterProperties.getPassword(), jedisPoolConfig());
         }
        LOGGER.debug("获取JedisCluster执行结束。。。");
         return jedisCluster;
    }
    
    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(redisClusterProperties.getPoolMaxWait());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisClusterProperties.getPoolTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisClusterProperties.getPoolMinEvictableIdleTimeMillis());
        jedisPoolConfig.setTestOnBorrow(redisClusterProperties.isPoolTestOnBorrow());
        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPoolMaxIdle());
        jedisPoolConfig.setMinIdle(redisClusterProperties.getPoolMinIdle());
        jedisPoolConfig.setBlockWhenExhausted(redisClusterProperties.isBlockWhenExhausted());
        jedisPoolConfig.setJmxEnabled(redisClusterProperties.isJmxEnabled());
        jedisPoolConfig.setTestOnReturn(redisClusterProperties.isTestOnReturn());
        jedisPoolConfig.setTestWhileIdle(redisClusterProperties.isTestWhileIdle());
        jedisPoolConfig.setLifo(redisClusterProperties.isLifo());
        LOGGER.debug("获取JedisPoolConfig执行结束。。。");
        return jedisPoolConfig;
    }

}