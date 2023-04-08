package com.seepine.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * copy from objectNode and enhance get/set
 *
 * @author seepine
 */
public class JsonObject extends ContainerNode<JsonObject> implements java.io.Serializable {
  private static final long serialVersionUID = 1L;

  // Note: LinkedHashMap for backwards compatibility
  protected final Map<String, JsonNode> _children;

  public JsonObject() {
    this(Json.getNodeFactory());
  }

  public JsonObject(Map<String, JsonNode> kids) {
    super(Json.getNodeFactory());
    this._children = kids;
  }

  public JsonObject(ObjectNode objectNode) {
    this(Json.getNodeFactory());
    Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields();
    while (it.hasNext()) {
      Map.Entry<String, JsonNode> entry = it.next();
      _children.put(entry.getKey(), entry.getValue());
    }
  }

  public JsonObject(JsonNode jsonNode) {
    this(Json.getNodeFactory());
    if (!jsonNode.isObject()) {
      throw new UnsupportedOperationException("Not object, cannot be cast to JsonObject");
    }
    Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
    while (it.hasNext()) {
      Map.Entry<String, JsonNode> entry = it.next();
      _children.put(entry.getKey(), entry.getValue());
    }
  }

  public JsonObject(JsonNodeFactory nc) {
    super(nc);
    _children = new LinkedHashMap<>();
  }

  @Override
  public int hashCode() {
    return _children.hashCode();
  }

  @Override
  public JsonToken asToken() {
    return JsonToken.START_OBJECT;
  }

  @Override
  public void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
    @SuppressWarnings("deprecation")
    boolean trimEmptyArray =
        (provider != null) && !provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
    g.writeStartObject(this);
    for (Map.Entry<String, JsonNode> en : _children.entrySet()) {
      BaseJsonNode value = (BaseJsonNode) en.getValue();
      if (trimEmptyArray && value.isArray() && value.isEmpty(provider)) {
        continue;
      }
      g.writeFieldName(en.getKey());
      value.serialize(g, provider);
    }
    g.writeEndObject();
  }

  @Override
  public void serializeWithType(
      JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
    boolean trimEmptyArray =
        (provider != null) && !provider.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS);
    WritableTypeId typeIdDef =
        typeSer.writeTypePrefix(g, typeSer.typeId(this, JsonToken.START_OBJECT));
    for (Map.Entry<String, JsonNode> en : _children.entrySet()) {
      BaseJsonNode value = (BaseJsonNode) en.getValue();
      if (trimEmptyArray && value.isArray() && value.isEmpty(provider)) {
        continue;
      }
      g.writeFieldName(en.getKey());
      value.serialize(g, provider);
    }
    typeSer.writeTypeSuffix(g, typeIdDef);
  }

  @Override
  @SuppressWarnings("unchecked")
  public JsonObject deepCopy() {
    JsonObject ret = new JsonObject(_nodeFactory);
    for (Map.Entry<String, JsonNode> entry : _children.entrySet())
      ret._children.put(entry.getKey(), entry.getValue().deepCopy());
    return ret;
  }

  @Override
  public int size() {
    return _children.size();
  }

  @Override
  public JsonNode get(int index) {
    return null;
  }

  @Override
  public JsonNode get(String fieldName) {
    return _children.get(fieldName);
  }

  @Override
  public JsonNode path(String fieldName) {
    JsonNode n = _children.get(fieldName);
    if (n != null) {
      return n;
    }
    return MissingNode.getInstance();
  }

  @Override
  public JsonNode path(int index) {
    return MissingNode.getInstance();
  }

  @Override
  protected JsonNode _at(JsonPointer ptr) {
    return get(ptr.getMatchingProperty());
  }

  @Override
  public JsonNodeType getNodeType() {
    return JsonNodeType.OBJECT;
  }

  @Override
  public JsonNode findValue(String fieldName) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (fieldName.equals(entry.getKey())) {
        return entry.getValue();
      }
      JsonNode value = entry.getValue().findValue(fieldName);
      if (value != null) {
        return value;
      }
    }
    return null;
  }

  @Override
  public JsonObject findParent(String fieldName) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (fieldName.equals(entry.getKey())) {
        return this;
      }
      JsonNode value = entry.getValue().findParent(fieldName);
      if (value != null) {
        return (JsonObject) value;
      }
    }
    return null;
  }

  @Override
  public List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (fieldName.equals(entry.getKey())) {
        if (foundSoFar == null) {
          foundSoFar = new ArrayList<>();
        }
        foundSoFar.add(this);
      } else { // only add children if parent not added
        foundSoFar = entry.getValue().findParents(fieldName, foundSoFar);
      }
    }
    return foundSoFar;
  }

  @Override
  public List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (fieldName.equals(entry.getKey())) {
        if (foundSoFar == null) {
          foundSoFar = new ArrayList<>();
        }
        foundSoFar.add(entry.getValue());
      } else { // only add children if parent not added
        foundSoFar = entry.getValue().findValues(fieldName, foundSoFar);
      }
    }
    return foundSoFar;
  }

  @Override
  public List<String> findValuesAsText(String fieldName, List<String> foundSoFar) {
    for (Map.Entry<String, JsonNode> entry : _children.entrySet()) {
      if (fieldName.equals(entry.getKey())) {
        if (foundSoFar == null) {
          foundSoFar = new ArrayList<>();
        }
        foundSoFar.add(entry.getValue().asText());
      } else {
        foundSoFar = entry.getValue().findValuesAsText(fieldName, foundSoFar);
      }
    }
    return foundSoFar;
  }

  protected boolean _childrenEqual(JsonObject other) {
    return _children.equals(other._children);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o instanceof JsonObject) {
      return _childrenEqual((JsonObject) o);
    }
    return false;
  }

  @Override
  public JsonObject removeAll() {
    _children.clear();
    return this;
  }

  @Override
  public boolean equals(Comparator<JsonNode> comparator, JsonNode o) {
    if (!(o instanceof JsonObject)) {
      return false;
    }
    JsonObject other = (JsonObject) o;
    Map<String, JsonNode> m1 = _children;
    Map<String, JsonNode> m2 = other._children;

    final int len = m1.size();
    if (m2.size() != len) {
      return false;
    }
    for (Map.Entry<String, JsonNode> entry : m1.entrySet()) {
      JsonNode v2 = m2.get(entry.getKey());
      if ((v2 == null) || !entry.getValue().equals(comparator, v2)) {
        return false;
      }
    }
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public ArrayNode withArray(String propertyName) {
    JsonNode n = _children.get(propertyName);
    if (n != null) {
      if (n instanceof ArrayNode) {
        return (ArrayNode) n;
      }
      throw new UnsupportedOperationException(
          "Property '"
              + propertyName
              + "' has value that is not of type ArrayNode (but "
              + n.getClass().getName()
              + ")");
    }
    ArrayNode result = arrayNode();
    _children.put(propertyName, result);
    return result;
  }

  @Override
  public JsonNode required(String propertyName) {
    JsonNode n = _children.get(propertyName);
    if (n != null) {
      return n;
    }
    return _reportRequiredViolation("No value for property '%s' of `JsonObject`", propertyName);
  }

  /**
   * Method to use for accessing all properties (with both names and values) of this JSON Object.
   */
  @Override
  public Iterator<Map.Entry<String, JsonNode>> fields() {
    return _children.entrySet().iterator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public JsonObject with(String propertyName) {
    JsonNode n = _children.get(propertyName);
    if (n != null) {
      if (n instanceof JsonObject) {
        return (JsonObject) n;
      }
      throw new UnsupportedOperationException(
          "Property '"
              + propertyName
              + "' has value that is not of type ObjectNode (but "
              + n.getClass().getName()
              + ")");
    }
    JsonObject result = new JsonObject(_nodeFactory);
    _children.put(propertyName, result);
    return result;
  }

  /*
  /**********************************************************
  /* Internal methods (overridable)
  /**********************************************************
   */
  protected JsonObject _set(String fieldName, JsonNode value) {
    _children.put(fieldName, value);
    return this;
  }

  /*
  /**********************************************************
  /* Extended ObjectNode API, mutators, typed
  /**********************************************************
   */

  /**
   * jsonObject.setArray("list").add(1).add(2).add(3);
   *
   * @param propertyName propertyName
   * @return Newly constructed ArrayNode (NOT the old value, which could be of any type)
   */
  public ArrayNode setArray(String propertyName) {
    ArrayNode n = arrayNode();
    _set(propertyName, n);
    return n;
  }

  public JsonObject setArray(String propertyName, ArrayNode arrayNode) {
    _set(propertyName, arrayNode);
    return this;
  }
  /**
   * jsonObject.setObject("user").set("username","cat").set("age",26);
   *
   * @param propertyName propertyName
   * @return Newly constructed ObjectNode (NOT the old value, which could be of any type)
   */
  public JsonObject setObject(String propertyName) {
    JsonObject n = new JsonObject(_nodeFactory);
    _set(propertyName, n);
    return n;
  }

  public JsonObject setObject(String propertyName, JsonObject jsonObject) {
    _set(propertyName, jsonObject);
    return this;
  }

  public JsonObject setNull(String propertyName) {
    _children.put(propertyName, nullNode());
    return this;
  }

  public JsonObject set(String propertyName, Object pojo) {
    return _set(propertyName, pojoNode(pojo));
  }

  public JsonObject set(String propertyName, RawValue raw) {
    return _set(propertyName, rawValueNode(raw));
  }

  public JsonObject set(String propertyName, short v) {
    return _set(propertyName, numberNode(v));
  }

  public JsonObject set(String fieldName, Short v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v.shortValue()));
  }

  public JsonObject set(String fieldName, int v) {
    return _set(fieldName, numberNode(v));
  }

  public JsonObject set(String fieldName, Integer v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v.intValue()));
  }

  public JsonObject set(String fieldName, long v) {
    return _set(fieldName, numberNode(v));
  }

  public JsonObject set(String fieldName, Long v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v.longValue()));
  }

  public JsonObject set(String fieldName, float v) {
    return _set(fieldName, numberNode(v));
  }

  public JsonObject set(String fieldName, Float v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v.floatValue()));
  }

  public JsonObject set(String fieldName, double v) {
    return _set(fieldName, numberNode(v));
  }

  public JsonObject set(String fieldName, Double v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v.doubleValue()));
  }

  public JsonObject set(String fieldName, BigDecimal v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v));
  }

  public JsonObject set(String fieldName, BigInteger v) {
    return _set(fieldName, (v == null) ? nullNode() : numberNode(v));
  }

  public JsonObject set(String fieldName, String v) {
    return _set(fieldName, (v == null) ? nullNode() : textNode(v));
  }

  public JsonObject set(String fieldName, boolean v) {
    return _set(fieldName, booleanNode(v));
  }

  public JsonObject set(String fieldName, Boolean v) {
    return _set(fieldName, (v == null) ? nullNode() : booleanNode(v));
  }

  public JsonObject set(String fieldName, byte[] v) {
    return _set(fieldName, (v == null) ? nullNode() : binaryNode(v));
  }

  public ArrayNode getArray(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isArray() ? (ArrayNode) get(fieldName) : null;
  }

  public JsonObject getObj(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isObject() ? new JsonObject(get(fieldName)) : null;
  }

  public String getStr(String fieldName) {
    return this.has(fieldName) ? get(fieldName).asText() : null;
  }

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

  public Boolean getBool(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isBoolean() ? get(fieldName).asBoolean() : null;
  }

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

  public BigDecimal getBigDecimal(String fieldName) {
    if (!this.has(fieldName)) {
      return null;
    }
    return get(fieldName).isDouble() || get(fieldName).isBigDecimal() || get(fieldName).isInt()
        ? new BigDecimal(get(fieldName).asText())
        : null;
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
   * 构建JsonObject并设置值，等同 new JsonObject().set(field,v)
   *
   * @return jsonObject
   * @since 0.2.1
   */
  public static JsonObject build(String fieldName, String v) {
    return new JsonObject().set(fieldName, v);
  }
}
