package com.seepine.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * @author seepine
 */
public class JsonObject extends ObjectNode implements Serializable {
  private static final long serialVersionUID = 1L;

  public JsonObject() {
    super(Json.getNodeFactory());
  }

  public JsonObject(Map<String, JsonNode> kids) {
    super(Json.getNodeFactory(), kids);
  }

  public JsonObject(ObjectNode objectNode) {
    super(Json.getNodeFactory());
    Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields();
    while (it.hasNext()) {
      Map.Entry<String, JsonNode> entry = it.next();
      _children.put(entry.getKey(), entry.getValue().deepCopy());
    }
  }

  public void set(String name, String value) {
    put(name, value);
  }

  public void set(String name, Integer value) {
    put(name, value);
  }

  public void set(String name, Double value) {
    put(name, value);
  }

  public void set(String name, BigDecimal value) {
    put(name, value);
  }

  public void set(String name, Long value) {
    put(name, value);
  }

  public void set(String name, Boolean value) {
    put(name, value);
  }

  public void set(String name, ArrayNode arrayNode) {
    putArray(name).addAll(arrayNode);
  }

  public void set(String name, Object o) {
    putPOJO(name, o);
  }

  public ArrayNode getArray(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isArray() ? (ArrayNode) super.get(fieldName) : null;
  }

  public JsonObject getObj(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isObject()
        ? new JsonObject((ObjectNode) super.get(fieldName))
        : null;
  }

  public JsonObject getObj(int index) {
    if (!this.has(index)) {
      return null;
    }
    return super.get(index).isObject() ? new JsonObject((ObjectNode) super.get(index)) : null;
  }

  public String getStr(String fieldName) {
    return this.has(fieldName) ? super.get(fieldName).asText() : null;
  }

  public Integer getInt(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isInt() ? super.get(fieldName).asInt() : null;
  }

  public Boolean getBool(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isBoolean() ? super.get(fieldName).asBoolean() : null;
  }

  public Long getLong(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isLong() || super.get(fieldName).isInt()
        ? super.get(fieldName).asLong()
        : null;
  }

  public BigDecimal getBigDecimal(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return super.get(fieldName).isDouble()
            || super.get(fieldName).isBigDecimal()
            || super.get(fieldName).isInt()
        ? new BigDecimal(super.get(fieldName).asText())
        : null;
  }
}
