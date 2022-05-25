package com.seepine.json.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.seepine.json.Json;
import com.seepine.json.test.entity.NormalBean;

import java.util.ArrayList;
import java.util.List;

public class JsonArrayTest {
  public static void main(String[] args) {
    List<NormalBean> list = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      list.add(NormalBean.build());
    }
    String json = Json.toJson(list);
    System.out.println("json");
    System.out.println(json);

    List<NormalBean> list2 = Json.parse(json, new TypeReference<List<NormalBean>>() {});
    System.out.println("\nlist2");
    System.out.println(list2);
    list2.forEach(System.out::println);

    List<NormalBean> list3 = Json.parse(json, List.class, NormalBean.class);
    System.out.println("\nlist3");
    System.out.println(list3);
    list3.forEach(System.out::println);

    ArrayNode arrayNode = Json.parseArray(json);
    System.out.println("\narrayNode");
    System.out.println(arrayNode);
  }
}
