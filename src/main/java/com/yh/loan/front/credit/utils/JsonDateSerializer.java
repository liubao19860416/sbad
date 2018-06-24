package com.yh.loan.front.credit.utils;

import java.io.IOException;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class JsonDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        String formattedDate = DateTimeUtil.formatDate2Str(date, DateTimeUtil.DATE_PATTON_1);
        generator.writeStartObject();
        generator.writeString(formattedDate);
        generator.writeEndObject();
    }

}