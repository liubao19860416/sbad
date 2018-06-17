package com.yhjr.basic.dao;

import java.util.List;
import java.util.Map;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 */
public interface BaseDao<T> {

    void saveN(T t);

    void saveN(Map<String, Object> map);

    void saveBatch(List<T> list);

    int updateN(T t);

    int updateN(Map<String, Object> map);

    int deleteN(Object id);

    int deleteN(Map<String, Object> map);

    int deleteBatch(Object[] id);

    T queryObject(Object id);

    List<T> queryList(Map<String, Object> map);

    List<T> queryList(Object id);

    int queryTotal(Map<String, Object> map);

    int queryTotal();
}
