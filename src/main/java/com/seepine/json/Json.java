package com.seepine.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author seepine
 */
public class Json {
  public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
  public static final String NORM_TIME_PATTERN = "HH:mm:ss";
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    // 忽略未知的JSON字段
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 原始值不能为空，例如int的属性，传递了null，报错
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
    OBJECT_MAPPER.setLocale(Locale.CHINA);
    OBJECT_MAPPER.registerModule(new JavaTimeModule());
    OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
    // 序列化以get/set后的函数名为主，例如getXFG则是XFG
    OBJECT_MAPPER.setPropertyNamingStrategy(
        new PropertyNamingStrategy() {
          private static final long serialVersionUID = 1L;

          @Override
          public String nameForSetterMethod(
              MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return method.getName().substring(3);
          }

          @Override
          public String nameForGetterMethod(
              MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return method.getName().substring(3);
          }
        });
  }

  public static JsonNodeFactory getNodeFactory() {
    return OBJECT_MAPPER.getNodeFactory();
  }

  public static void setPropertyNamingStrategy(PropertyNamingStrategy s) {
    OBJECT_MAPPER.setPropertyNamingStrategy(s);
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static class JavaTimeModule extends SimpleModule {
    public JavaTimeModule() {
      super(PackageVersion.VERSION);
      this.addSerializer(
          LocalDateTime.class,
          new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
      this.addSerializer(
          LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(NORM_DATE_PATTERN)));
      this.addSerializer(
          LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(NORM_TIME_PATTERN)));
    }
  }

  public static String toJson(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static JsonNode parse(String jsonStr) {
    try {
      return OBJECT_MAPPER.readTree(jsonStr);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static ObjectNode objectNode() {
    return new ObjectNode(OBJECT_MAPPER.getNodeFactory());
  }

  public static ArrayNode arrayNode() {
    return new ArrayNode(OBJECT_MAPPER.getNodeFactory());
  }

  public static JsonObject parseObj(String jsonStr) {
    return new JsonObject((ObjectNode) parse(jsonStr));
  }

  public static ArrayNode parseArray(String jsonStr) {
    return (ArrayNode) parse(jsonStr);
  }

  public static <T> T parse(String jsonStr, Class<T> toValueType) {
    return OBJECT_MAPPER.convertValue(parse(jsonStr), toValueType);
  }

  public static <T> T parse(Object obj, Class<T> toValueType) {
    return OBJECT_MAPPER.convertValue(obj, toValueType);
  }

  public static <T> T parse(Object obj, TypeReference<T> toValueTypeRef) {
    return OBJECT_MAPPER.convertValue(obj, toValueTypeRef);
  }
}
