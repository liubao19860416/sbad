package com.yhjr.basic.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSON;
import com.yhjr.basic.service.RedisCacheService;

import redis.clients.jedis.JedisCluster;

/**
 *  RedisCacheService服务定义接口实现类
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
@Configuration("redisCacheService")
@ConditionalOnBean(JedisCluster.class)
public class RedisCacheServiceImpl implements RedisCacheService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheServiceImpl.class);
    
    @Value("${redis.cache.prefix:hjxh}")
    private String cachePrefix;
    
    @Autowired(required=false)
    private JedisCluster jedisCluster;
    
    @Override
    public <T> String addByKey(String key, T value){
        key=cachePrefix+key;
        String object2JsonString = null;
        if(value instanceof String){
            object2JsonString =value.toString();
        }else{
            object2JsonString = JSON.toJSONString(value);
        }
        String result = jedisCluster.set(key, object2JsonString);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addByKey信息为:[key={},value={},result={}]",key,value,result);
        }
        return result;
    }
    
    @Override
    public <T> String addExByKey(String key, int seconds, T value){
        key=cachePrefix+key;
        String object2JsonString=null;
        if(value instanceof String){
            object2JsonString =value.toString();
        }else{
            object2JsonString = JSON.toJSONString(value);
        }
        String result = jedisCluster.setex(key, seconds, object2JsonString);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addExByKey信息为:[key={},value={},result={}]",key,value,result);
        }
        return result;
    }
    
    @Override
    public <T> String addExAtByKey(String key, long unixTime, T value){
        String result = addByKey(key, value);
        Long expireAt = expireAtByKey(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addExAtByKey信息为:[key={},unixTime={},result={},expireAt={}]",key,unixTime,result,expireAt);
        }
        return result;
    }
    
    @Override
    public <T> Long addListKey(Map<String, T> map){
        Long sum = (long) 0;
        Iterator<Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, T> entry = iterator.next();
            String key = entry.getKey();
            //key=cachePrefix+key;
            T value = entry.getValue();
            addByKey(key, value);
            sum = sum + 1;
        }
        return sum;
    }
    
    @Override
    public Long expireAtByKey(String key, long unixTime) {
        key=cachePrefix+key;
        Long result = jedisCluster.expireAt(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("expireAtByKey信息为:[key={},unixTime={},result={}]",key,unixTime,result);
        }
        return result;
    }
    
    @Override
    public Long expireByKey(String key, int seconds) {
        key=cachePrefix+key;
        Long result = jedisCluster.expire(key, seconds);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("expireByKey信息为:[key={},seconds={},result={}]",key,seconds,result);
        }
        return result;
    }
    
    @Override
    public Long incrementBy(String key){
        return incrementBy(key,null);
    }
    
    @Override
    public Long incrementExBy(String key,  int seconds){
        Long result = incrementBy(key,null);
        Long expireByKey = expireByKey(key, seconds);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementExBy信息为:[key={},seconds={},expireByKey={},result={}]",cachePrefix+key,seconds,expireByKey,result);
        }
        return result;
    }
    
    @Override
    public Long incrementExAtBy(String key, long unixTime){
        Long result = incrementBy(key,null);
        Long expireAtByKey = expireAtByKey(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementExAtBy信息为:[key={},unixTime={},expireAtByKey={},result={}]",cachePrefix+key,unixTime,expireAtByKey,result);
        }
        return result;
    }
    
    private Long incrementBy(String key, Long defaultValue) {
        key=cachePrefix+key;
        long result = 0L;
        if(null==defaultValue){
            result = jedisCluster.incr(key);
        }else{
            result = jedisCluster.incrBy(key, defaultValue);
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementBy defaultValue信息为:[key={},defaultValue={},result={}]",key,defaultValue,result);
        }
        return result;
    }
    
    @Override
    public Long getLong(String key) {
        key=cachePrefix+key;
        String result = jedisCluster.get(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getLong信息为:[key={},result={}]",key,result);
        }
        return result==null?0:Long.valueOf(result);
    }
    
    @Override
    public String getString(String key){
        key=cachePrefix+key;
        String result = jedisCluster.get(key);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("getObject信息为:[key={},result={}]",key,result);
        }
        return result;
    }
    
    @Override
    public  <V> V getObject(String key,Class<V> clazz) {
        key=cachePrefix+key;
        String value = jedisCluster.get(key);
        V result = JSON.parseObject(value, clazz);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getObjectClass信息为:[key={},result={}]",key,result);
        }
        return result;
    }

    @Override
    public Boolean existsByKey(String key){
        key=cachePrefix+key;
        Boolean result = jedisCluster.exists(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("existsByKey信息为:[key={},result={}]",key,result);
        }
        return result==null?false:result;
    }
    
    @Override
    public Long ttlByKey(String key) {
        key=cachePrefix+key;
        long result = jedisCluster.ttl(key);
        if(result==-1){
            result=Integer.MAX_VALUE;
        }else if(result==-2){
            result=0;
        }else if(result>0){
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("ttlByKey信息为:[key={},result={}]",key,result);
        }
        return result;
    }
    
    @Override
    public Long deleteByKey(String key) {
        key=cachePrefix+key;
        Long result = jedisCluster.del(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("deleteByKey信息为:[key={},result={}]",key,result);
        }
        return result;
    }

    @Override
    public Long batchDelete(List<String> keyList) {
        Long sum = (long) 0;
        Long result = (long) 0;
        for (int i = 0; i < keyList.size(); i++) {
            //result = deleteByKey(cachePrefix+keyList.get(i));
            result = deleteByKey(keyList.get(i));
            sum = sum + result;
        }
        return sum;
    }
    
}