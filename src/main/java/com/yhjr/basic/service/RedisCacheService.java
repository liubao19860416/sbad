package com.yhjr.basic.service;

import java.util.List;
import java.util.Map;

/**
 * RedisCacheService服务定义接口类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public interface RedisCacheService {

    <T> String addByKey(String key, T object);

    <T> String addExByKey(String key, int seconds, T object);
    
    <T> String addExAtByKey(String key, long unixTime, T object);
    
    <T> Long addListKey(Map<String, T> map) ;

    Long incrementBy(String key);
    
    Long incrementExBy(String key, int seconds);
    
    Long incrementExAtBy(String key, long unixTime);
    
    Long getLong(String key);
    
    String getString(String key) ;

    <T> T getObject(String key, Class<T> clazz);

    Long deleteByKey(String key);

    Long batchDelete(List<String> keyList) ;

    Long expireByKey(String key, int seconds);
    
    Long expireAtByKey(String key, long unixTime) ;

    Long ttlByKey(String key);
    
    Boolean existsByKey(String key) ;

}
