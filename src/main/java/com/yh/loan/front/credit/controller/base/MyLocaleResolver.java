package com.yh.loan.front.credit.controller.base;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化
 */
public class MyLocaleResolver implements LocaleResolver {
    
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        Locale locale = Locale.getDefault();
        if (StringUtils.isNoneBlank(lang)) {
            String[] split = lang.split("_");
            if(StringUtils.equalsIgnoreCase("zh", split[0])&&StringUtils.equalsIgnoreCase("CN", split[1])){
                locale = new Locale(split[0], split[1]);
            }else{
                locale = new Locale("en", "US");
            }
        }
        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }
    
}
