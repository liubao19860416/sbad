package com.yhjr.basic.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * null值转换操作
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月13日
 */
public class NullValueFilter implements ValueFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(NullValueFilter.class);
    private static final PropertyUtilsBean propertyUtils;
    static {
        propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
    }

    @Override
    public Object process(Object obj, String paramKey, Object value) {
        if (null == value) {
            try {
                Class<?> propertyType = propertyUtils.getPropertyType(obj, paramKey);
                Class<?> propertyTypeComp = null;
                if (ClassUtils.isAssignable(propertyType, String.class)) {
                    propertyTypeComp = String.class;
                    value = StringUtils.EMPTY;
                } else if (ClassUtils.isAssignable(propertyType, Map.class)) {
                    propertyTypeComp = Map.class;
                    value = MapUtils.EMPTY_MAP;
                } else if (ClassUtils.isAssignable(propertyType, Collection.class) || propertyType.isArray()) {
                    value = ListUtils.EMPTY_LIST;
                    propertyTypeComp = Collection.class;
                } else if (ClassUtils.isAssignable(propertyType, Boolean.class)) {
                    value = Boolean.FALSE;
                    propertyTypeComp = Boolean.class;
                } else if (ClassUtils.isAssignable(propertyType, Number.class)) {
                    value = 0;
                    propertyTypeComp = Number.class;
                } else if (ClassUtils.isAssignable(propertyType, Date.class)) {
                    propertyTypeComp = Date.class;
                } else {
                    propertyTypeComp = Object.class;
                    value = ObjectUtils.NULL;
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("字段:{}对应类型为:{},转换后值为:{}", paramKey, propertyTypeComp, JSON.toJSONString(value));
                }
            } catch (SecurityException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("字段Null属性值信息转换操作异常...", e);
            }
        }
        return value;
    }
    
}
