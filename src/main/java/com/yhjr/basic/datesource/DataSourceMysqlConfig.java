package com.yhjr.basic.datesource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
@Configuration
public class DataSourceMysqlConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceMysqlConfig.class);
    
    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> datasourceType;
    
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        LOGGER.debug("初始化primaryDataSource执行了。。。");
        return DataSourceBuilder.create().type(datasourceType).build();
    }

}