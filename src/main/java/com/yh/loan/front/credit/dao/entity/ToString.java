package com.yh.loan.front.credit.dao.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * ToString定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月13日
 */
public abstract class ToString implements Serializable{
    private static final long serialVersionUID = -4828198874021663315L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
