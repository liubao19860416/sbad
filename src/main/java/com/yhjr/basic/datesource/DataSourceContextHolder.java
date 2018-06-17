package com.yhjr.basic.datesource;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yhjr.basic.exception.BaseServiceException;


/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
public final class DataSourceContextHolder {
private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);
    
    private static final ThreadLocal<String> local = new ThreadLocal<String>();
    public static final List<DataSourceType> dataSourceTypes = new ArrayList<DataSourceType>();
    
    static{
        dataSourceTypes.add(DataSourceType.MYSQL);
        dataSourceTypes.add(DataSourceType.MYSQL2);
    }

    public static void set(DataSourceType type) {
        if(type==null){
            throw new BaseServiceException("设置指定类型参数信息DataSourceType为空!");
        }
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("设置当前模式为:{}",type.getType());
        }
        switch (type) {
        case MYSQL:
            local.set(DataSourceType.MYSQL.getType());
            break;
        case MYSQL2:
            local.set(DataSourceType.MYSQL2.getType());
            break;
        default:
            local.set(DataSourceType.MYSQL.getType());
            LOGGER.warn("设置当前模式非MYSQL非MYSQL2,设置为默认MYSQL方式");
            break;
        }
    }

    public static void remove() {
        //LOGGER.info("清除当前模式为:{}",local.get());
        //local.remove();
        local.set(null);
    }
    
    public static String get() {
        String jdbcType = local.get();
        if(StringUtils.isBlank(jdbcType)){
            jdbcType=DataSourceType.MYSQL.getType();
        }
        return jdbcType;
    }

    /**
     * 是否包含
     */
    public static boolean contains(DataSourceType dsType) {
        if(dsType==null){
            return false;
        }
        for (DataSourceType dataSourceType : DataSourceType.values()) {
            if(dataSourceType.getType().equals(dsType.getType())){
                return true;
            }
        }
        return false;
    }
}
