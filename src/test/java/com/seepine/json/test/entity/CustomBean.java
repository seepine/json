package com.seepine.json.test.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/** must be set public or add getXXX method */
public class CustomBean {
  public String PAYTEL;

  @JsonProperty("payVLD")
  public String PAYVLD;

  @Override
  public String toString() {
    return "Bean{" + "PAYTEL='" + PAYTEL + '\'' + ", PAYVLD='" + PAYVLD + '\'' + '}';
  }
}
