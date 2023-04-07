package com.seepine.json.test;

import com.seepine.json.Json;
import com.seepine.json.test.entity.NormalBean;

public class JsonToStringIgnoreTest {
  public static void main(String[] args) {
    NormalBean bean = new NormalBean();
    bean.setId(5L);
    bean.setName("");
    System.out.println(Json.toJson(bean));
    System.out.println(Json.toJsonIgnoreNull(bean));
    System.out.println(Json.toJsonIgnoreEmpty(bean));
  }
}
