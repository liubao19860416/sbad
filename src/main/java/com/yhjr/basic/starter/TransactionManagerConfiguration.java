package com.yhjr.basic.starter;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.yhjr.basic.datesource.DynamicDataSource;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
@Configuration
@EnableTransactionManagement
@Order(Ordered.LOWEST_PRECEDENCE)
//@AutoConfigureAfter({MyBatisConfiguration.class })
@ConditionalOnBean(name="dataSource",value=DynamicDataSource.class)
public class TransactionManagerConfiguration implements TransactionManagementConfigurer{
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerConfiguration.class);

    @Resource(name="dataSource")
    private DynamicDataSource dataSource;
    
    @Bean(name = "transactionManager")
    public PlatformTransactionManager txManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        transactionManager.setNestedTransactionAllowed(true);
        return transactionManager;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        LOGGER.info("初始化PlatformTransactionManager执行结束了");
        return txManager();
    }
    
}