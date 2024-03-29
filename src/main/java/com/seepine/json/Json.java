package com.seepine.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seepine.json.exception.JsonException;
import com.seepine.json.mapper.JsonMapperBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  @Nonnull
  public static JsonNodeFactory getNodeFactory() {
    return OBJECT_MAPPER.getNodeFactory();
  }

  /**
   * 获取默认ObjectMapper
   *
   * @return ObjectMapper
   */
  @Nonnull
  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  /**
   * 构建可操作ObjectNode对象
   *
   * @return ObjectNode对象
   */
  @Nonnull
  public static ObjectNode objectNode() {
    return new ObjectNode(OBJECT_MAPPER.getNodeFactory());
  }
  /**
   * 构建可操作ArrayNode对象
   *
   * @return ArrayNode对象
   */
  @Nonnull
  public static ArrayNode arrayNode() {
    return new ArrayNode(OBJECT_MAPPER.getNodeFactory());
  }

  /**
   * 对象转 json字符串, 忽略 null 字段
   *
   * @code Json.toJsonIgnoreNull(new User()) = {"id":"1","nickname":""}
   * @param obj 对象
   * @return json字符串
   * @since 0.2.0
   * @exception JsonException jsonProcessingException
   */
  @Nonnull
  public static String toJsonIgnoreNull(@Nullable Object obj) throws JsonException {
    try {
      return NULL_OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转json字符串，忽略null和empty属性
   *
   * @code Json.toJsonIgnoreNull(new User()) = {"id":"1"}
   * @param obj 对象
   * @return json字符串
   * @since 0.2.0
   * @exception JsonException jsonProcessingException
   */
  @Nonnull
  public static String toJsonIgnoreEmpty(@Nullable Object obj) throws JsonException {
    try {
      return EMPTY_OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转json字符串，不忽略null和empty属性
   *
   * @code Json.toJsonIgnoreNull(new User()) = {"id":"1","nickname":"","age":null}
   * @param obj 对象
   * @return json字符串
   * @exception JsonException jsonProcessingException
   */
  @Nonnull
  public static String toJson(@Nullable Object obj) throws JsonException {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转JsonObject
   *
   * @code JsonObject jsonObject = Json.parseObj( {"id":"1","nickname":"","age":null} )
   * @param jsonStr json字符串
   * @return JsonObject
   * @exception JsonException e
   */
  @Nonnull
  public static JsonObject parseObj(@Nonnull String jsonStr) throws JsonException {
    try {
      return new JsonObject(parseObject(jsonStr));
    } catch (Exception e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转ObjectNode
   *
   * @code ObjectNode objectNode = Json.parseObject( {"id":"1","nickname":"","age":null} )
   * @param jsonStr json字符串
   * @return ObjectNode
   * @exception JsonException e
   */
  @Nonnull
  public static ObjectNode parseObject(@Nonnull String jsonStr) throws JsonException {
    try {
      return (ObjectNode) parse(jsonStr);
    } catch (Exception e) {
      throw new JsonException(e);
    }
  }
  /**
   * json字符串转ArrayNode
   *
   * @code ArrayNode arrayNode = Json.parseObject( [{"id":"1","nickname":"","age":null}] )
   * @param jsonStr json字符串
   * @return ArrayNode
   * @exception JsonException e
   */
  @Nonnull
  public static ArrayNode parseArray(@Nonnull String jsonStr) throws JsonException {
    try {
      return (ArrayNode) parse(jsonStr);
    } catch (Exception e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转JsonNode
   *
   * @code JsonNode jsonNode = Json.parse( {"id":"1","nickname":"","age":null} )
   * @param jsonStr json字符串
   * @return JsonNode
   * @exception JsonException jsonProcessingException
   */
  @Nonnull
  public static JsonNode parse(@Nonnull String jsonStr) throws JsonException {
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
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(@Nonnull String jsonStr, @Nonnull JavaType toValueType)
      throws JsonException {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }
  /**
   * node转对象
   *
   * @code User user = Json.parse( {"id":"1","nickname":"","age":null} , User.class)
   * @param treeNode 例如JsonNode或ArrayNode等等
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   * @since 0.2.2
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(TreeNode treeNode, Class<T> toValueType) throws JsonException {
    try {
      return OBJECT_MAPPER.treeToValue(treeNode, toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * jsonObject转对象
   *
   * @code User res = Json.parse(new JsonObject() , User.class)
   * @param jsonObject jsonObject
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   * @since 0.2.2
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(JsonObject jsonObject, Class<T> toValueType) throws JsonException {
    try {
      return OBJECT_MAPPER.treeToValue(jsonObject.toObjectNode(), toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转对象
   *
   * @code User user = Json.parse("{"nickname":"bob"}" , User.class)
   * @param jsonStr json字符串
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(String jsonStr, Class<T> toValueType) throws JsonException {
    try {
      return OBJECT_MAPPER.readValue(jsonStr, toValueType);
    } catch (JsonProcessingException e) {
      throw new JsonException(e);
    }
  }

  /**
   * json字符串转对象
   *
   * @code User user = Json.parse("{"nickname":"bob"}" , new TypeReference<User>(){})
   * @param jsonStr json字符串
   * @param toValueTypeRef 转换对象TypeReference
   * @return 转换对象
   * @param <T> 转换对象类型
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(String jsonStr, TypeReference<T> toValueTypeRef) throws JsonException {
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
   * @exception JsonException jsonProcessingException
   */
  public static <T> T parse(String jsonStr, Class<?> parametrized, Class<?>... parameterClasses)
      throws JsonException {
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
   * @code AuthUser authUser = Json.convert(new User(), JavaType)
   * @param fromValue 原始对象
   * @param toValueType 转换对象JavaType
   * @return 转换对象
   * @param <T> 转换对象类型
   * @exception JsonException illegalArgumentException
   */
  public static <T> T convert(Object fromValue, JavaType toValueType) throws JsonException {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转换
   *
   * @code AuthUser authUser = Json.convert(new User(), AuthUser.class)
   * @param fromValue 原始对象
   * @param toValueType 转换对象Class
   * @return 转换对象
   * @param <T> 转换对象类型
   * @exception JsonException illegalArgumentException
   */
  public static <T> T convert(Object fromValue, Class<T> toValueType) throws JsonException {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }

  /**
   * 对象转换
   *
   * @code AuthUser authUser = Json.convert(new User(), new TypeReference<AuthUser>(){})
   * @param fromValue 原始对象
   * @param toValueTypeRef 转换对象TypeReference
   * @return 转换对象
   * @param <T> 转换对象类型
   * @exception JsonException illegalArgumentException
   */
  public static <T> T convert(Object fromValue, TypeReference<T> toValueTypeRef)
      throws JsonException {
    try {
      return OBJECT_MAPPER.convertValue(fromValue, toValueTypeRef);
    } catch (IllegalArgumentException e) {
      throw new JsonException(e);
    }
  }
}
