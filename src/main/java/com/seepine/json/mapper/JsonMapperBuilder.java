package com.seepine.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.seepine.json.mapper.JavaTimeModule;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class JsonMapperBuilder {
  public static ObjectMapper build(JsonInclude.Include... includes) {
    ObjectMapper objectMapper =
        JsonMapper.builder()
            .build()
            // ignore bean not field
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // int or double can not be null
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
            // enum using toString
            .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
            .setLocale(Locale.CHINA)
            .registerModule(new JavaTimeModule())
            .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
    for (JsonInclude.Include include : includes) {
      objectMapper.setSerializationInclusion(include);
    }
    return objectMapper;
  }
}
