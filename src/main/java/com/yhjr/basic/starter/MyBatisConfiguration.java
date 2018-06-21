package com.yhjr.basic.starter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.pagehelper.PageHelper;
import com.yhjr.basic.config.PagehelperProperties;
import com.yhjr.basic.dao.base.UploadStatus;
import com.yhjr.basic.dao.base.UploadStatusEnumHandler;
import com.yhjr.basic.datesource.DataSourceMysqlConfig;
import com.yhjr.basic.datesource.DataSourceType;
import com.yhjr.basic.datesource.DynamicDataSource;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
@Configuration
//@MapperScan(basePackages = {"com.yh.loan.front.credit.dao","com.yhjr.credit.credit.dao.mapper"})
//@MapperScan(basePackages = {"com.yh.loan.front.*.dao","com.yhjr.credit.*.dao.mapper"})
//@MapperScan(basePackages = {"com.yh.loan.front.*.dao","com.yhjr.credit.task.dao.mapper"})
@MapperScan(basePackages = {"com.yh.loan.front.*.dao","com.yhjr.credit.*.dao.mapper"})
//@EnableConfigurationProperties(PagehelperProperties.class)
@AutoConfigureAfter({/* DataSourceMysql2Config.class,*/DataSourceMysqlConfig.class/*,PagehelperProperties.class*/ })
@AutoConfigureBefore({ TransactionManagerConfiguration.class })
public class MyBatisConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisConfiguration.class);

    @Value("${mybatis.config.location:/mybatis-config.xml}")
    private Resource configLocation;
    
    @Value("${mybatis.mapper.locations:classpath:mapper/**/*.xml}")
    private String mapperLocations;
    
    @Value("${mybatis.typealiases.package:com.yhjr.basic.dao.entity}")
    private String typeAliasesPackage;
    
    @Value("${mybatis.typehandlers.package:com.yhjr.basic.dao.base}")
    private String typeHandlersPackage;
    
    @Autowired(required=false)
    private PagehelperProperties pagehelperProperties;
    
//    @Resource(name="secondDataSource")
    @Qualifier("secondDataSource")
    @Autowired(required=false)
    private DataSource secondDataSource;
    
    @Primary
    @Autowired
    @Bean(name = "dataSource")
    public DynamicDataSource dataSource(@Qualifier("primaryDataSource")DataSource primaryDataSource/*,@Qualifier("secondDataSource") DataSource secondDataSource*/) {
        DynamicDataSource dataSource=new DynamicDataSource();
        Map<Object, Object> targetDataSources=new HashMap<>();
        targetDataSources.put(DataSourceType.MYSQL.getType(), primaryDataSource);
        if(secondDataSource!=null){
            targetDataSources.put(DataSourceType.MYSQL2.getType(), secondDataSource);
        }else{
            targetDataSources.put(DataSourceType.MYSQL2.getType(), primaryDataSource);
        }
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(primaryDataSource);
        LOGGER.debug("初始化DynamicDataSource执行了。。。");
        return dataSource;
    }
    
    @Autowired
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DynamicDataSource dataSource)  {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfigLocation(configLocation);
        sessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        sessionFactoryBean.setTypeHandlersPackage(typeHandlersPackage);
        sessionFactoryBean.setTypeHandlers(new UploadStatusEnumHandler[]{new UploadStatusEnumHandler(UploadStatus.class)});
        
        PageHelper pageHelper = new PageHelper();  
        Properties props = new Properties();  
        if(pagehelperProperties!=null){
            props.setProperty("reasonable", pagehelperProperties.getReasonable());  
            props.setProperty("supportMethodsArguments", pagehelperProperties.getSupportMethodsArguments());  
            props.setProperty("returnPageInfo", pagehelperProperties.getReturnPageInfo());  
            props.setProperty("params", pagehelperProperties.getParams());  
            pageHelper.setProperties(props);  
            sessionFactoryBean.setPlugins(new Interceptor[]{pageHelper});  
        }
        
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sessionFactoryBean.setMapperLocations(resolver.getResources(mapperLocations));
            SqlSessionFactory sqlSessionFactory = sessionFactoryBean.getObject();
            LOGGER.debug("初始化SqlSessionFactory执行结束了");
            return sqlSessionFactory;
        } catch (Exception e) {
            LOGGER.error("初始化SqlSessionFactory异常",e);
            throw new RuntimeException("初始化ResourcePatternResolver信息异常!",e);
        }
    }
    
    @Lazy
    @Autowired
    @Scope("singleton")
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        LOGGER.debug("SqlSessionTemplate初始化完成...");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
//    @Value("${spring.datasource.filters:}")
//    private String filters;
//    
//    @Value("${druid.login.username:}")
//    private String loginUsername;
//    
//    @Value("${druid.login.password:}")
//    private String loginPassword;
//    
//    @Value("${druid.parameter.exclusions:}")
//    private String parameterExclusions;
//    
//    @Value("${druid.service.patterns:}")
//    private String servicePatterns;
//    
//    @Value("${druid.url.mappings:/druid/*}")
//    private String urlMappings;
//    
//    @Value("${druid.reset.enable:false}")
//    private String resetEnable;
//    
//    @Value("${druid.profile.enable:true}")
//    private String profileEnable;
//    
//    @Bean
//    public ServletRegistrationBean druidServlet() {
//        ServletRegistrationBean reg = new ServletRegistrationBean();
//        reg.setServlet(new StatViewServlet());
//        reg.addUrlMappings(urlMappings);
//        reg.addInitParameter("loginUsername", loginUsername);
//        reg.addInitParameter("loginPassword", loginPassword);
//        //reg.addInitParameter("allow", "127.0.0.1,10.67.26.113");
//        //reg.addInitParameter("deny", "10.0.12.26");
//        reg.addInitParameter("resetEnable",resetEnable);
//        return reg;
//    }
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new WebStatFilter());
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.addInitParameter("exclusions", parameterExclusions);
//        filterRegistrationBean.addInitParameter("profileEnable", profileEnable);
//        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
//        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
//        return filterRegistrationBean;
//    }
    
}

