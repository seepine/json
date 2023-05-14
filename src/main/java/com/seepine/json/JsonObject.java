package com.seepine.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.seepine.json.exception.JsonException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

/**
 * enhance objectNode get/set, the get will return null when there are no values or mismatched types
 *
 * @author seepine
 */
public class JsonObject implements Serializable {
  private static final long serialVersionUID = 1L;
  ObjectNode objectNode;

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
  public String toJson() {
    return objectNode.toString();
  }

  /**
   * 获取ObjectNode对象
   *
   * @return objectNode
   */
  public ObjectNode toObjectNode() {
    return objectNode;
  }

  /**
   * 深拷贝
   *
   * @return jsonObject
   */
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
  public <T> T toObject(Class<T> toValueType) {
    return Json.parse(objectNode, toValueType);
  }

  /**
   * 获取迭代器
   *
   * @return Iterator<JsonNode>
   */
  public Iterator<JsonNode> iterator() {
    return objectNode.iterator();
  }

  /**
   * 构建JsonObject，等同 new JsonObject()
   *
   * @return jsonObject
   * @since 0.2.1
   */
  public static JsonObject build() {
    return new JsonObject();
  }

  /**
   * 构建JsonObject并设置值，等同 new JsonObject().set(field,v)
   *
   * @return jsonObject
   * @since 0.2.1
   */
  public static JsonObject build(String fieldName, Object v) {
    return new JsonObject().set(fieldName, v);
  }

  /**
   * 获取jsonNode
   *
   * @param fieldName 属性
   * @return jsonNode
   */
  public JsonNode get(String fieldName) {
    return objectNode.get(fieldName);
  }

  /**
   * 如果属性不存在，则设置值
   *
   * @param fieldName 属性
   * @param value 值
   * @return jsonObject
   */
  public JsonObject setIfAbsent(String fieldName, Object value) {
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
  public JsonObject set(String fieldName, Object value) {
    if (value == null) {
      objectNode.putNull(fieldName);
    } else if (value instanceof String) {
      objectNode.put(fieldName, (String) value);
    } else if (value instanceof Integer) {
      objectNode.put(fieldName, (Integer) value);
    } else if (value instanceof Boolean) {
      objectNode.put(fieldName, (Boolean) value);
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
  public boolean has(String fieldName) {
    return objectNode.has(fieldName);
  }

  /**
   * 获取数组
   *
   * @param fieldName 属性
   * @return 数组/null
   */
  public ArrayNode getArray(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isArray() ? (ArrayNode) get(fieldName) : null;
  }

  /**
   * 获取对象
   *
   * @param fieldName 属性
   * @return 对象/null
   */
  public JsonObject getObj(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isObject() ? new JsonObject(get(fieldName)) : null;
  }
  /**
   * 获取对象
   *
   * @param fieldName 属性
   * @return 对象/null
   */
  public <T> T getObject(String fieldName, Class<T> toValueType) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isObject() ? Json.parse(get(fieldName), toValueType) : null;
  }

  /**
   * 获取字符串
   *
   * @param fieldName 属性
   * @return 字符串/null
   */
  public String getStr(String fieldName) {
    return this.has(fieldName) ? get(fieldName).asText() : null;
  }

  /**
   * 获取整数
   *
   * @param fieldName 属性
   * @return 整数/null
   */
  public Integer getInt(String fieldName) {
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
  public Boolean getBool(String fieldName) {
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
  public Long getLong(String fieldName) {
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
  public BigDecimal getBigDecimal(String fieldName) {
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
  public BigInteger getBigInteger(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isBigInteger() || get(fieldName).isLong() || get(fieldName).isInt()
        ? new BigInteger(get(fieldName).asText())
        : null;
  }
}
