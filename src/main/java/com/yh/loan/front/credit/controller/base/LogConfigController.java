package com.yh.loan.front.credit.controller.base;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yh.loan.front.credit.exception.AbsErrorCodeConstant;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年12月29日
 */
@Validated
@RestController
@RequestMapping("/log")
public class LogConfigController extends AbsBaseController<LogConfigController>{

    @GetMapping(value = "/{logLevel}/0")
    public Object changeLogLevel(@Validated @NotNull(message="${logLevel.isEmpty}") @PathVariable(name="logLevel",required=true) String logLevel,
            @RequestParam(name="packageName",required=false) String packageName,
            HttpServletRequest request, HttpServletResponse response) {
        getLogger().error("..............当前支持的Log日志级别信息列表.....................................");
        getLogger().trace("【trace]】");
        getLogger().debug("【debug】");
        getLogger().info("【info】");
        getLogger().warn("【warn】");
        getLogger().error("【error】");
        getLogger().error("..............当前支持的Log日志级别信息列表....................................");
        
        logLevel=logLevel.toUpperCase();
        switch (logLevel) {
        case "TRACE":
        case "DEBUG":
        case "INFO":
        case "WARN":
        case "ERROR":
            break;
        default:
            return super.getFailureResultInfo(AbsErrorCodeConstant.ERROR_CODE_99001,logLevel);
        }
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            if(StringUtils.isNoneBlank(packageName)){
                Logger packageNameLogger = loggerContext.getLogger(packageName);
                packageNameLogger.setLevel(Level.valueOf(logLevel));
                packageNameLogger.setAdditive(true);
            }
            Logger rootLogger = loggerContext.getLogger("root");
            rootLogger.setLevel(Level.valueOf(logLevel));
            rootLogger.setAdditive(true);
            //loggerContext.reset();
            super.clearLogger();
        } catch (Exception e) {
            getLogger().error("动态修改日志[{}==>{}]级别出错",logLevel, packageName,e);
            return super.getFailureResultInfo(AbsErrorCodeConstant.ERROR_CODE_99001);
        }
        return super.getSuccessResultInfo(logLevel);
    }
    
    @GetMapping(value = "/infoTest/0")
    public Object logInfo0(@RequestHeader Map<String, Object> headerMap,HttpServletRequest request, HttpServletResponse response) {
        getLogger().error("..............当前測試支持的Log日志级别信息列表.....................................");
        getLogger().trace("【trace]】");
        getLogger().debug("【debug】");
        getLogger().info("【info】");
        getLogger().warn("【warn】");
        getLogger().error("【error】");
        getLogger().error("..............当前測試支持的Log日志级别信息列表....................................");
        return super.getSuccessResultInfo(headerMap);
    }
    
}