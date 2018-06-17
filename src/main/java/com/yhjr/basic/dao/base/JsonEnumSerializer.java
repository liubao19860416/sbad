package com.yhjr.basic.dao.base;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 枚举类型显示
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年11月10日
 */
public class JsonEnumSerializer extends JsonSerializer<UploadStatus> {

    @Override
    public void serialize(UploadStatus data, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeString(data.getKey());
        generator.writeEndObject();
    }
    
}