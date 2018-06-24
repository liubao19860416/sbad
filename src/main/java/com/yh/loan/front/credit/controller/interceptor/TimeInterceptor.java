package com.yh.loan.front.credit.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 请求时间拦截
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月1日
 */
public class TimeInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime);
		String method = request.getMethod();
        if(RequestMethod.POST.name().equals(method)){
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            LOGGER.debug("当前{}请求方式[{}]匹配成功,需要设置编码信息...",RequestMethod.POST.name(),method);
        }
        response.setCharacterEncoding("utf-8");
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
		long startTime = (Long) request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		long usedTime = endTime - startTime;
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			LOGGER.info("@@@@@@@@(URI):" + request.getRequestURI());
			LOGGER.info("@@@@@@@@Controller层请求耗时("+ usedTime+ "ms)");
			LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex) throws Exception {
	    if( null!= ex){
            //特殊处理
            LOGGER.debug("有异常信息,继续执行...");
        }
	}
	
}