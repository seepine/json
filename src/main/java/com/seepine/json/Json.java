package com.seepine.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seepine.json.mapper.GetterPropertyNamingStrategy;
import com.seepine.json.mapper.JavaTimeModule;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author seepine
 */
public class Json {
  private static final ObjectMapper OBJECT_MAPPER =
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
          .setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
          .setPropertyNamingStrategy(new GetterPropertyNamingStrategy());

  public static JsonNodeFactory getNodeFactory() {
    return OBJECT_MAPPER.getNodeFactory();
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static ObjectNode objectNode() {
    return new ObjectNode(OBJECT_MAPPER.getNodeFactory());
  }

  public static ArrayNode arrayNode() {
    return new ArrayNode(OBJECT_MAPPER.getNodeFactory());
  }

  public static String toJson(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static JsonObject parseObj(String jsonStr) {
    return new JsonObject(parse(jsonStr));
  }

  public static ArrayNode parseArray(String jsonStr) {
    return (ArrayNode) parse(jsonStr);
  }

  public static JsonNode parse(String jsonStr) {
    try {
      return OBJECT_MAPPER.readTree(jsonStr);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static <T> T parse(String jsonStr, Class<T> toValueType) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueType);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static <T> T parse(String jsonStr, TypeReference<T> toValueTypeRef) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueTypeRef);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * support complex object
   *
   * <p>List<NormalBean> list = Json.parse(json, List.class, NormalBean.class);
   *
   * <p>Map<Long,String> map = Json.parse(json, Map.class, Long.class, String.class);
   *
   * @param jsonStr jsonStr
   * @param parametrized parentClass
   * @param parameterClasses tClass
   * @param <T> t
   * @return t
   */
  public static <T> T parse(String jsonStr, Class<?> parametrized, Class<?>... parameterClasses) {
    try {
      return OBJECT_MAPPER.readValue(
          jsonStr,
          OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public static <T> T convert(Object fromValue, JavaType toValueType) {
    return OBJECT_MAPPER.convertValue(fromValue, toValueType);
  }

  public static <T> T convert(Object fromValue, Class<T> toValueType) {
    return OBJECT_MAPPER.convertValue(fromValue, toValueType);
  }

  public static <T> T convert(Object fromValue, TypeReference<T> toValueTypeRef) {
    return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
  }
}
