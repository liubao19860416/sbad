package com.yhjr.basic.datesource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix="spring.datasource.mysql2",name={"url","username","password"})
public class DataSourceMysql2Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceMysql2Config.class);
    
    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> datasourceType;
    
    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql2")
    public DataSource dataSource() {
        LOGGER.debug("初始化secondDataSource执行了。。。");
        return DataSourceBuilder.create().type(datasourceType).build();
    }
    
}