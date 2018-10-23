package com.yhjr.basic.controller.base;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yhjr.basic.controller.povo.ResultInfo;
import com.yhjr.basic.exception.AbsErrorCodeConstant;


/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年11月6日
 */
@Controller
public class  MyGlobalController extends AbsBaseController<MyGlobalController>{

	public static final String INDEX = "/index";
	public static final String ERROR_PATH = "/error/";
	public static final String ERROR_400 = ERROR_PATH+"400";
	public static final String ERROR_401 = ERROR_PATH+"401";
	public static final String ERROR_404 = ERROR_PATH+"404";
	public static final String ERROR_405 = ERROR_PATH+"405";
	public static final String ERROR_500 = ERROR_PATH+"500";
	public static final String NOAUTH_IP = ERROR_PATH+"noauthip";
	public static final String NOAUTH_FREQUENTIP = ERROR_PATH+"frequentip";
	
	@RequestMapping(INDEX)
	public String handleIndex() {
		return INDEX;
	}
	
	@RequestMapping("/")
	public String handleHome() {
	    return INDEX;
	}
	
	@RequestMapping(ERROR_PATH)
	public String handleError() {
		return ERROR_PATH+"error";
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_400,produces="application/json;charset=UTF-8")
	public Object handleError400(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_400,  
				getMessage(AbsErrorCodeConstant.ERROR_CODE_400));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_401,produces="application/json;charset=UTF-8")
	public Object handleError401(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_401,  
				getMessage(AbsErrorCodeConstant.ERROR_CODE_401));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_404,produces="application/json;charset=UTF-8")
	public Object handleError404(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_404,  
				getMessage(AbsErrorCodeConstant.ERROR_CODE_404));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_405,produces="application/json;charset=UTF-8")
	public Object handleError405(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_405,  
				getMessage(AbsErrorCodeConstant.ERROR_CODE_405));
	}
	
	@ResponseBody
	@RequestMapping(value = ERROR_500,produces="application/json;charset=UTF-8")
	public Object handleError500(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_500,  
				getMessage(AbsErrorCodeConstant.ERROR_CODE_500));
	}
	
	@ResponseBody
	@RequestMapping(value = NOAUTH_IP)
	public Object handleNoauthIp(HttpServletResponse response) {
	    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	    return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_10004,  
	            getMessage(AbsErrorCodeConstant.ERROR_CODE_10004));
	}
	
	@ResponseBody
	@RequestMapping(value = NOAUTH_FREQUENTIP)
	public Object handleNoauthFrequentIp(HttpServletResponse response) {
	    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	    return new ResultInfo<String>(AbsErrorCodeConstant.ERROR_CODE_10005,  
	            getMessage(AbsErrorCodeConstant.ERROR_CODE_10005));
	}
	
}