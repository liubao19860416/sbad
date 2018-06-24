package com.yh.loan.front.credit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yh.loan.front.credit.utils.DataSourceContextHolder;
import com.yh.loan.front.credit.utils.DataSourceType;

@Aspect
@Order(10)
@Component
public class DataSourceAop {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAop.class);
    
    @Pointcut("execution(* com.yh.loan.front.credit.service.impl..BaseServiceImpl.*(..)) || execution(* com.yh.loan.front.credit.service.mysql.impl..*ServiceImpl.*(..))")
    public void mysqlPointcut(){}
    
    @Pointcut("execution(* com.yh.loan.front.credit.service.impl..BaseOracleServiceImpl.*(..)) || execution(* com.yh.loan.front.credit.service.oracle.impl..*ServiceImpl.*(..))")
    public void oraclePointcut(){}

    @Before("mysqlPointcut()")
    public void setMysqlDataSourceType(JoinPoint point) {
        DataSourceContextHolder.set(DataSourceType.MYSQL);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource切换到[{}]：MYSQL模式!",point.getSignature());
        }
    }
    
    @Before("oraclePointcut()")
    public void setOracleDataSourceType(JoinPoint point) {
        DataSourceContextHolder.set(DataSourceType.ORACLE);
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource切换到[{}]：ORACLE模式!",point.getSignature());
        }
    }
    
    @After("mysqlPointcut() || oraclePointcut()")
    public void restoreDataSource(JoinPoint point) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("DataSource清除 : [{}] > {}",point.getSignature(),DataSourceContextHolder.get());
        }
        DataSourceContextHolder.remove();
    }
    
}