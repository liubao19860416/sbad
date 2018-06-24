package com.yh.loan.front.credit.utils;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.apache.commons.lang3.StringUtils;

/**
 * JSR303的校验帮助类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年12月14日
 */
public class JSR303Util {

    /**
     * 如果返回null则表示没有错误
     */
    public static String check(Object obj) {
        if (null == obj) {
            return "入参对象不能为空!";
        }
        Set<ConstraintViolation<Object>> validResult = Validation.buildDefaultValidatorFactory().getValidator().validate(obj);
        if (null != validResult && validResult.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<ConstraintViolation<Object>> iterator = validResult.iterator(); iterator.hasNext();) {
                ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
                if(StringUtils.isNotBlank(constraintViolation.getMessage())) {
                    sb.append(constraintViolation.getMessage()).append("、");
                } else {
                    sb.append(constraintViolation.getPropertyPath().toString()).append("不合法、");
                }
            }
            if (sb.lastIndexOf("、") == sb.length() - 1) {
                sb.delete(sb.length() - 1, sb.length());
            }
            return sb.toString();
        }
        return null;
    }

}