package com.yhjr.basic.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yhjr.basic.datesource.DataSourceContextHolder;
import com.yhjr.basic.datesource.DataSourceType;

/**
 * @target针对类的注解,@annotation是针对方法的注解
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2018年6月6日
 */
@Aspect
@Order(10)
@Component
public class DataSourceAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAop.class);
    
    @Pointcut("@within(com.yhjr.basic.datesource.MysqlNode1)") 
    //@Pointcut("execution(* com.yh.loan.front.credit.service.impl..BaseServiceImpl.*(..)) || execution(* com.yh.loan.front.credit.service.mysql.impl..*ServiceImpl.*(..)) || @within(com.yhjr.basic.datesource.MysqlNode1")
    public void mysqlPointcut(){}
    
    @Pointcut("@within(com.yhjr.basic.datesource.MysqlNode2)") 
    //@Pointcut("execution(* com.yh.loan.front.credit.service.oracle.impl..*ServiceImpl.*(..)) || execution(* com.yh.loan.front.credit.service.impl..BaseOracleServiceImpl.*(..)) || @within(com.yhjr.basic.datesource.MysqlNode2)")
    //@Pointcut("execution(* com.yh.loan.front.credit.service.impl..BaseOracleServiceImpl.*(..)) || execution(* com.yh.loan.front.credit.service.oracle.impl..*ServiceImpl.*(..))")
    public void mysql2Pointcut(){}

    @Before("mysqlPointcut()")
    public void setMysqlDataSourceType(JoinPoint point) {
        DataSourceContextHolder.set(DataSourceType.MYSQL);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource切换到[{}]：MYSQL模式!",point.getSignature());
        }
    }
    
    @Before("mysql2Pointcut()")
    public void setOracleDataSourceType(JoinPoint point) {
        DataSourceContextHolder.set(DataSourceType.MYSQL2);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource切换到[{}]：MYSQL2模式!",point.getSignature());
        }
    }
    
    @After("mysqlPointcut() || mysql2Pointcut()")
    public void restoreDataSource(JoinPoint point) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource清除 : [{}] > {}",point.getSignature(),DataSourceContextHolder.get());
        }
        DataSourceContextHolder.remove();
    }
    
}