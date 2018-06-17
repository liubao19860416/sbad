package com.yhjr.basic.aop;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yhjr.basic.exception.AbsErrorCodeConstant;
import com.yhjr.basic.exception.RequestLimitException;
import com.yhjr.basic.service.RedisCacheService;


/**
 */
@Aspect
@Order(5)
@Component
public class RequestLimitAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLimitAspect.class);

    @Autowired(required=false)
    private RedisCacheService redisCacheService;
    
    /**
     * 将注解信息直接注入到请求参数中
     */
    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint , RequestLimit limit) throws RequestLimitException {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                request = (HttpServletRequest) args[i];
                break;
            }
        }
        if (request == null) {
            request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
            if (request == null) {
                throw new RequestLimitException(AbsErrorCodeConstant.ERROR_CODE_10005);
            }
        }
        //String ip = request.getLocalAddr();
        String requestIp = com.yhjr.basic.utils.CommonUtils.getRemoteAddr(request);
        String requestURI = request.getRequestURI().toString();
        String redisKey = "req_limit:".concat(requestURI).concat(requestIp);
        
        long requestCount = 0;
        long limitCount = limit.count();
        Long limitTime = limit.time();
        Long ttlByKey = redisCacheService.ttlByKey(redisKey);
        if(ttlByKey>0&&ttlByKey<=limitTime){
            requestCount = redisCacheService.incrementBy(redisKey);
        }else{
            requestCount = redisCacheService.incrementExBy(redisKey, limitTime.intValue());
        }
        
        LOGGER.info("用户IP[{}]访问地址[{}]当前次数为[{}],限定次数为[{}]",requestIp,requestURI,requestCount,limitCount);
        if (requestCount > limitCount) {
            LOGGER.error("@@@@@@@@@@@@@@@@@");
            LOGGER.error("@@(URI):" + requestURI);
            LOGGER.error("@@拦截频繁访问数据的ip[{}]访问接口数据!",requestIp);
            LOGGER.error("@@@@@@@@@@@@@@@@@");
            throw new RequestLimitException(AbsErrorCodeConstant.ERROR_CODE_10005);
        }
    }
    
}