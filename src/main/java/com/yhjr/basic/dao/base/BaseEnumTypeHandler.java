package com.yhjr.basic.dao.base;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;  
  
public class BaseEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {  
  
    private Class<E> type;  
  
    public BaseEnumTypeHandler() {}  
      
    public BaseEnumTypeHandler(Class<E> type) {  
        if(type == null) {  
            throw new IllegalArgumentException("Type argument cannot be null");  
        } else {  
            this.type = type;  
        } 
    }  
  
    @Override  
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {  
        if (jdbcType == null) {  
            ps.setString(i, parameter.toString());  
        } else {  
            ps.setObject(i, parameter.name(), jdbcType.TYPE_CODE);  
        }
    }  
  
    @Override  
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {  
        return EnumUtils.getEnum(type,rs.getString(columnName));  
    }  
    
    /**
     * EnumUtils
     */
    @Override  
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {  
        return EnumUtils.getEnum(type,rs.getString(columnIndex));  
    }  
  
    @Override  
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {  
        return get(type, cs.getString(columnIndex));  
    }  
  
    @SuppressWarnings("hiding")
    private <E extends Enum<E>> E get(Class<E> type, String v) {  
        if (v == null) return null;  
        if (StringUtils.isNumeric(v)) {  
            return get(type, Integer.parseInt(v));  
        } else {  
            return Enum.valueOf(type, v);  
        }  
    }  
  
    @SuppressWarnings({ "unchecked", "hiding" })
    private <E extends Enum<E>> E get(Class<E> type, int v) {  
        Method method = null;  
        E result = null;  
        try {  
            method = type.getMethod("get", int.class);  
            result = (E)method.invoke(type, v);  
        } catch (NoSuchMethodException e) {  
            result = Enum.valueOf(type, String.valueOf(v));  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        } catch (InvocationTargetException e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
}  