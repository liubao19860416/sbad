package com.yhjr.basic.dao.base;

import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = { UploadStatus.class })
public class MyEnumTypeHandler<E extends Enum<E>> extends BaseEnumTypeHandler<E> {

    public MyEnumTypeHandler(Class<E> type) {
        super(type);
    }

}