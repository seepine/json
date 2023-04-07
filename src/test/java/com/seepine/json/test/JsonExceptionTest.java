package com.seepine.json.test;

import com.seepine.json.Json;
import com.seepine.json.test.entity.CustomBean;

public class JsonExceptionTest {
  public static void main(String[] args) {
    System.out.println(Json.parse("[{\"a\":\"1\",\"b\":\"2\"}]", CustomBean.class));
  }
}
