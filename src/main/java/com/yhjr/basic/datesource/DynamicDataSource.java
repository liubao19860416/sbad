package com.yhjr.basic.datesource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceType = DataSourceContextHolder.get();
        if(StringUtils.isBlank(dataSourceType)){
            dataSourceType=DataSourceType.MYSQL.getType();
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("determineCurrentLookupKey获取的默认[dataSourceType={}]", dataSourceType);
            }
        }else{
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("determineCurrentLookupKey获取的当前[dataSourceType={}]", dataSourceType);
            }
        }
        return dataSourceType;
    }

}