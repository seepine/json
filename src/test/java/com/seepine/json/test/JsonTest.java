package com.seepine.json.test;

import com.seepine.json.Json;
import com.seepine.json.JsonObject;
import com.seepine.json.test.entity.NormalBean;

public class JsonTest {
  public static void main(String[] args) {
    String normalJson = Json.toJson(NormalBean.build());
    System.out.println("normalJson:");
    System.out.println(normalJson);
    JsonObject jsonObject = Json.parseObj(normalJson);
    System.out.println("\njsonObject:");
    System.out.println(jsonObject);
    NormalBean normalBean = Json.parse(normalJson, NormalBean.class);
    System.out.println("\nnormalBean:");
    System.out.println(normalBean);
  }
}
