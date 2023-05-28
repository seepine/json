package com.seepine.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.seepine.json.exception.JsonException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

/**
 * enhance objectNode get/set, the get will return null when there are no values or mismatched types
 *
 * @author seepine
 */
public class JsonObject extends JsonNode implements Serializable {
  private static final long serialVersionUID = 1L;
  private final ObjectNode objectNode;

  /** 构造空json对象 */
  public JsonObject() {
    objectNode = Json.objectNode();
  }

  /**
   * 通过 jsonNode 构造，必须是ObjectNode
   *
   * @param jsonNode jsonNode
   */
  public JsonObject(JsonNode jsonNode) {
    if (jsonNode == null) {
      throw new JsonException("jsonNode not be null");
    } else if (!jsonNode.isObject()) {
      throw new JsonException("jsonNode not object");
    } else {
      this.objectNode = (ObjectNode) jsonNode;
    }
  }

  /**
   * 通过objectNode构造，不能为空
   *
   * @param objectNode objectNode
   */
  public JsonObject(ObjectNode objectNode) {
    if (objectNode == null) {
      throw new JsonException("objectNode not be null");
    } else {
      this.objectNode = objectNode;
    }
  }

  /**
   * 判断是否相等
   *
   * @param o 对象
   * @return 主要比较内部objectNode
   */
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }
    if (o instanceof JsonObject) {
      return objectNode.equals(((JsonObject) o).toObjectNode());
    }
    return objectNode.equals(o);
  }

  /**
   * 返回内部objectNode的hashCode
   *
   * @return hashCode
   */
  public int hashCode() {
    return objectNode.hashCode();
  }

  /**
   * 输出json字符串，同toJson()
   *
   * @return json串
   */
  @Override
  public String toString() {
    return objectNode.toString();
  }

  /**
   * 输出json字符串，同toString()
   *
   * @return json串
   */
  @Nonnull
  public String toJson() {
    return objectNode.toString();
  }

  /**
   * 获取ObjectNode对象
   *
   * @return objectNode
   */
  @Nonnull
  public ObjectNode toObjectNode() {
    return objectNode;
  }

  /**
   * 深拷贝
   *
   * @return jsonObject
   */
  @Nonnull
  public JsonObject deepCopy() {
    return new JsonObject(objectNode.deepCopy());
  }

  /**
   * 转对象
   *
   * @param toValueType bean class
   * @return T
   * @param <T> 类
   */
  @Nonnull
  public <T> T toObject(@Nonnull Class<T> toValueType) {
    return Json.parse(objectNode, toValueType);
  }

  /**
   * 获取迭代器
   *
   * @return Iterator<JsonNode>
   */
  @Nonnull
  public Iterator<JsonNode> iterators() {
    return objectNode.iterator();
  }

  /**
   * 构建JsonObject，等同 new JsonObject()
   *
   * @code JsonObject.build()
   * @return jsonObject
   * @since 0.2.1
   */
  @Nonnull
  public static JsonObject build() {
    return new JsonObject();
  }

  /**
   * 构建JsonObject并设置值，等同 new JsonObject().set(field,v)
   *
   * @code JsonObject.build("age",15)
   * @param fieldName fieldName
   * @param v value
   * @return jsonObject
   * @since 0.2.1
   */
  @Nonnull
  public static JsonObject build(String fieldName, Object v) {
    return new JsonObject().set(fieldName, v);
  }

  /**
   * 获取jsonNode
   *
   * @param fieldName 属性
   * @return jsonNode/null
   */
  @Nullable
  public JsonNode get(@Nonnull String fieldName) {
    return objectNode.get(fieldName);
  }

  /**
   * 如果属性不存在，则设置值
   *
   * @param fieldName 属性
   * @param value 值
   * @return jsonObject
   */
  @Nonnull
  public JsonObject setIfAbsent(@Nonnull String fieldName, @Nonnull Object value) {
    if (!has(fieldName)) {
      set(fieldName, value);
    }
    return this;
  }

  /**
   * 设置值，如果属性存在则覆盖
   *
   * @param fieldName 属性
   * @param value 值
   * @return jsonObject
   */
  @Nonnull
  public JsonObject set(@Nonnull String fieldName, @Nullable Object value) {
    if (value == null) {
      objectNode.putNull(fieldName);
    } else if (value instanceof String) {
      objectNode.put(fieldName, (String) value);
    } else if (value instanceof Integer) {
      objectNode.put(fieldName, (Integer) value);
    } else if (value instanceof Boolean) {
      objectNode.put(fieldName, (Boolean) value);
    } else if (value instanceof JsonObject) {
      objectNode.set(fieldName, ((JsonObject) value).objectNode);
    } else if (value instanceof Double) {
      objectNode.put(fieldName, (Double) value);
    } else if (value instanceof Long) {
      objectNode.put(fieldName, (Long) value);
    } else if (value instanceof Float) {
      objectNode.put(fieldName, (Float) value);
    } else if (value instanceof BigDecimal) {
      objectNode.put(fieldName, (BigDecimal) value);
    } else if (value instanceof BigInteger) {
      objectNode.put(fieldName, (BigInteger) value);
    } else if (value instanceof Short) {
      objectNode.put(fieldName, (Short) value);
    } else if (value instanceof RawValue) {
      objectNode.putRawValue(fieldName, (RawValue) value);
    } else if (value instanceof JsonNode) {
      objectNode.set(fieldName, (JsonNode) value);
    } else {
      objectNode.putPOJO(fieldName, value);
    }
    return this;
  }

  /**
   * 属性是否存在
   *
   * @param fieldName 属性
   * @return 是否存在
   */
  public boolean has(@Nonnull String fieldName) {
    return objectNode.has(fieldName);
  }

  /**
   * 获取数组
   *
   * @param fieldName 属性
   * @return 数组/null
   * @exception JsonException not array
   */
  @Nullable
  public ArrayNode getArray(@Nonnull String fieldName) throws JsonException {
    JsonNode get = get(fieldName);
    if (get == null) {
      return null;
    }
    if (!get.isArray()) {
      throw new JsonException("'" + fieldName + "' not array");
    }
    return (ArrayNode) get(fieldName);
  }

  /**
   * 获取对象
   *
   * @param fieldName 属性
   * @return 对象/null
   * @exception JsonException not object
   */
  @Nullable
  public JsonObject getObj(@Nonnull String fieldName) throws JsonException {
    JsonNode get = get(fieldName);
    if (get == null) {
      return null;
    }
    if (!get.isObject()) {
      throw new JsonException("'" + fieldName + "' not object");
    }
    return new JsonObject(get(fieldName));
  }
  /**
   * 获取对象
   *
   * @param fieldName 属性
   * @return 对象/null
   * @exception JsonException not object
   */
  @Nullable
  public <T> T getObject(@Nonnull String fieldName, @Nonnull Class<T> toValueType)
      throws JsonException {
    JsonNode get = get(fieldName);
    if (get == null) {
      return null;
    }
    if (!get.isObject()) {
      throw new JsonException("'" + fieldName + "' not object");
    }
    return Json.parse(get(fieldName), toValueType);
  }

  /**
   * 获取字符串
   *
   * @param fieldName 属性
   * @return 字符串/null
   */
  @Nullable
  public String getStr(@Nonnull String fieldName) {
    return this.has(fieldName) ? get(fieldName).asText() : null;
  }

  /**
   * 获取整数
   *
   * @param fieldName 属性
   * @return 整数/null
   */
  @Nullable
  public Integer getInt(@Nonnull String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    if (get(fieldName).isInt()) {
      return get(fieldName).asInt();
    }
    try {
      return Integer.valueOf(get(fieldName).asText());
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取bool
   *
   * @param fieldName 属性
   * @return bool/null
   */
  @Nullable
  public Boolean getBool(@Nonnull String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    if (get(fieldName).isBoolean()) {
      return get(fieldName).asBoolean();
    }
    String val = get(fieldName).asText();
    if ("true".equals(val)) {
      return true;
    }
    if ("false".equals(val)) {
      return false;
    }
    return null;
  }

  /**
   * 获取long
   *
   * @param fieldName 属性
   * @return long/null
   */
  @Nullable
  public Long getLong(@Nonnull String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    if (get(fieldName).isLong() || get(fieldName).isInt()) {
      return get(fieldName).asLong();
    }
    try {
      return Long.valueOf(get(fieldName).asText());
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取bigDecimal
   *
   * @param fieldName 属性
   * @return BigDecimal/null
   */
  @Nullable
  public BigDecimal getBigDecimal(@Nonnull String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isDouble()
            || get(fieldName).isBigDecimal()
            || get(fieldName).isBigInteger()
            || get(fieldName).isLong()
            || get(fieldName).isInt()
        ? new BigDecimal(get(fieldName).asText())
        : null;
  }

  /**
   * 获取BigInteger
   *
   * @param fieldName 属性
   * @return BigInteger/null
   */
  @Nullable
  public BigInteger getBigInteger(@Nonnull String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isBigInteger() || get(fieldName).isLong() || get(fieldName).isInt()
        ? new BigInteger(get(fieldName).asText())
        : null;
  }

  @Override
  @Deprecated
  public JsonNode get(int i) {
    return objectNode.get(i);
  }

  @Override
  @Deprecated
  public JsonNode path(String s) {
    return objectNode.path(s);
  }

  @Override
  @Deprecated
  public JsonNode path(int i) {
    return objectNode.path(i);
  }

  @Override
  protected JsonNode _at(JsonPointer jsonPointer) {
    return this.get(jsonPointer.getMatchingProperty());
  }

  @Override
  @Nonnull
  public JsonNodeType getNodeType() {
    return objectNode.getNodeType();
  }

  @Override
  @Deprecated
  public String asText() {
    return objectNode.asText();
  }

  @Override
  public JsonNode findValue(String s) {
    return objectNode.findValue(s);
  }

  @Override
  public JsonNode findPath(String s) {
    return objectNode.findPath(s);
  }

  @Override
  public JsonNode findParent(String s) {
    return objectNode.findParent(s);
  }

  @Override
  public List<JsonNode> findValues(String s, List<JsonNode> list) {
    return objectNode.findValues(s, list);
  }

  @Override
  public List<String> findValuesAsText(String s, List<String> list) {
    return objectNode.findValuesAsText(s, list);
  }

  @Override
  public List<JsonNode> findParents(String s, List<JsonNode> list) {
    return objectNode.findParents(s, list);
  }

  @Override
  public JsonToken asToken() {
    return objectNode.asToken();
  }

  @Override
  public JsonParser.NumberType numberType() {
    return objectNode.numberType();
  }

  @Override
  public JsonParser traverse() {
    return objectNode.traverse();
  }

  @Override
  public JsonParser traverse(ObjectCodec objectCodec) {
    return objectNode.traverse(objectCodec);
  }

  @Override
  public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    objectNode.serialize(jsonGenerator, serializerProvider);
  }

  @Override
  public void serializeWithType(
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider,
      TypeSerializer typeSerializer)
      throws IOException {
    objectNode.serializeWithType(jsonGenerator, serializerProvider, typeSerializer);
  }
}
