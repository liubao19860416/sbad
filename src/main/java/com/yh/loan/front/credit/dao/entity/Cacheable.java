package com.yh.loan.front.credit.dao.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 基础的缓存实体接口定义
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
public interface Cacheable {
    
    //获取redis的key值
    @JSONField(serialize=true,deserialize=false)
    public abstract String getRedisKey();
    //是否缓存标识
    @JSONField(serialize=true,deserialize=false)
    public abstract boolean isCachIgnore();

}
