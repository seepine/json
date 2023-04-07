package com.seepine.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seepine.json.exception.JsonException;

/**
 * @author seepine
 */
public class Json {
  /** 默认 mapper 除了 toJsonIgnoreNull 和 toJsonIgnoreEmpty 皆使用此对象 */
  public static final ObjectMapper OBJECT_MAPPER = JsonMapperBuilder.build();
  /** nonNull mapper 目前仅供 toJsonIgnoreNull 使用 */
  public static final ObjectMapper NULL_OBJECT_MAPPER =
      JsonMapperBuilder.build(JsonInclude.Include.NON_NULL);
  /** nonEmpty mapper 目前仅供 toJsonIgnoreEmpty 使用 */
  public static final ObjectMapper EMPTY_OBJECT_MAPPER =
      JsonMapperBuilder.build(JsonInclude.Include.NON_EMPTY);

  /**
   * 获取JsonNodeFactory, 即ObjectMapper.getNodeFactory()
   *
   * @return JsonNodeFactory
   */
  public static JsonNodeFactory getNodeFactory() {
    return OBJECT_MAPPER.getNodeFactory();
  }

  /**
   * 获取默认ObjectMapper
   *
   * @return ObjectMapper
   */
  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * 构建可操作ObjectNode对象
   *
   * @return ObjectNode对象
   */
  public static ObjectNode objectNode() {
    return new ObjectNode(OBJECT_MAPPER.getNodeFactory());
  }
  /**
   * 构建可操作ArrayNode对象
   *
   * @return ArrayNode对象
   */
  public static ArrayNode arrayNode() {
    return new ArrayNode(OBJECT_MAPPER.getNodeFactory());
  }

  /**
   * 对象转 json字符串, 忽略 null 字段
   *
   * @param obj 对象
   * @return json字符串
   * @since 0.2.0
   */
  public static String toJsonIgnoreNull(Object obj) {
    try {
      return NULL_OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转json字符串，忽略null和empty属性
   *
   * @param obj 对象
   * @return json字符串
   * @since 0.2.0
   */
  public static String toJsonIgnoreEmpty(Object obj) {
    try {
      return EMPTY_OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转json字符串
   *
   * @param obj 对象
   * @return json字符串
   */
  public static String toJson(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转JsonObject
   *
   * @param jsonStr json字符串
   * @return JsonObject
   */
  public static JsonObject parseObj(String jsonStr) {
    try {
      return new JsonObject(parse(jsonStr));
    } catch (Exception e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转ArrayNode
   *
   * @param jsonStr json字符串
   * @return ArrayNode
   */
  public static ArrayNode parseArray(String jsonStr) {
    try {
      return (ArrayNode) parse(jsonStr);
    } catch (Exception e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转JsonNode
   *
   * @param jsonStr json字符串
   * @return JsonNode
   */
  public static JsonNode parse(String jsonStr) {
    try {
      return OBJECT_MAPPER.readTree(jsonStr);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转对象
   *
   * @param jsonStr json字符串
   * @param toValueType 转换对象JavaType
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T parse(String jsonStr, JavaType toValueType) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转对象
   *
   * @param jsonStr json字符串
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T parse(String jsonStr, Class<T> toValueType) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转对象
   *
   * @param jsonStr json字符串
   * @param toValueTypeRef 转换对象TypeReference
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T parse(String jsonStr, TypeReference<T> toValueTypeRef) {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueTypeRef);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转复杂对象
   *
   * @code List<NormalBean> list = Json.parse(json, List.class, NormalBean.class);
   * @code Map<Long,String> map = Json.parse(json, Map.class, Long.class, String.class);
   * @param jsonStr json字符串
   * @param parametrized parentClass
   * @param parameterClasses tClass
   * @param <T> t 转换对象类型
   * @return 转换后对象
   */
  public static <T> T parse(String jsonStr, Class<?> parametrized, Class<?>... parameterClasses) {
    try {
      return OBJECT_MAPPER.readValue(
          jsonStr,
          OBJECT_MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses));
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转换
   *
   * @param fromValue 原始对象
   * @param toValueType 转换对象JavaType
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T convert(Object fromValue, JavaType toValueType) {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转换
   *
   * @param fromValue 原始对象
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T convert(Object fromValue, Class<T> toValueType) {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转换
   *
   * @param fromValue 原始对象
   * @param toValueTypeRef 转换对象TypeReference
   * @return 转换对象
   * @param <T> 转换对象类型
   */
  public static <T> T convert(Object fromValue, TypeReference<T> toValueTypeRef) {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }
}
