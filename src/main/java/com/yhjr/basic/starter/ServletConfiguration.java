package com.yhjr.basic.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年10月31日
 */
@Configuration
@ConditionalOnProperty(name={"druid.login.username","druid.login.password","druid.url.mappings"})
public class ServletConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletConfiguration.class);

    @Value("${spring.datasource.filters:}")
    private String filters;
    
    @Value("${druid.login.username:}")
    private String loginUsername;
    
    @Value("${druid.login.password:}")
    private String loginPassword;
    
    @Value("${druid.parameter.exclusions:}")
    private String parameterExclusions;
    
    @Value("${druid.service.patterns:}")
    private String servicePatterns;
    
    @Value("${druid.url.mappings:/druid/*}")
    private String urlMappings;
    
    @Value("${druid.reset.enable:false}")
    private String resetEnable;
    
    @Value("${druid.profile.enable:true}")
    private String profileEnable;
    
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings(urlMappings);
        reg.addInitParameter("loginUsername", loginUsername);
        reg.addInitParameter("loginPassword", loginPassword);
        //reg.addInitParameter("allow", "127.0.0.1,10.67.26.113");
        //reg.addInitParameter("deny", "10.0.12.26");
        reg.addInitParameter("resetEnable",resetEnable);
        LOGGER.info("初始化ServletRegistrationBean執行結束了。。。");
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", parameterExclusions);
        filterRegistrationBean.addInitParameter("profileEnable", profileEnable);
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        LOGGER.info("初始化FilterRegistrationBean執行結束了。。。");
        return filterRegistrationBean;
    }
    
}

