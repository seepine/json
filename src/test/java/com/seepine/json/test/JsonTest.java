package com.seepine.json.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.seepine.json.Json;
import com.seepine.json.JsonObject;

import java.util.Collections;

public class JsonTest {

  public static void main(String[] args) {
    beanToJson();
    jsonToBean();
  }

  public static void beanToJson() {
    Bean bean = new Bean();
    ObjectNode objectNode = Json.objectNode();
    objectNode.putPOJO("EAIAGPAYX", Collections.singletonList(bean));
    System.out.println(objectNode);
    System.out.println(Json.toJson(objectNode));
    System.out.println();
  }

  public static void jsonToBean() {
    String json = "{\"EAIAGPAYX\":[{\"PAYTEL\":null,\"fsfsf\":\"gsdg\"}]}";
    JsonObject jsonObject = Json.parseObj(json);
    System.out.println(jsonObject);
    System.out.println(Json.parse(jsonObject.getArray("EAIAGPAYX").get(0), Bean.class).toString());
  }

  /** 测试大小写敏感 */
  static class Bean {
    String PAYTEL;

    @JsonProperty("fsfsf")
    String PAYVLD = "gsdg";

    @Override
    public String toString() {
      return "Bean{" + "PAYTEL='" + PAYTEL + '\'' + ", PAYVLD='" + PAYVLD + '\'' + '}';
    }
  }
}
