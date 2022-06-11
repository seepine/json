package com.seepine.json.test.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/** must be set public or add getXXX method */
public class CustomBean {
  @JsonProperty("PaYTeL")
  public String PAYTEL;

  @JsonProperty("payVLD")
  public String PAYVLD;

  public CustomBean() {}

  public CustomBean(String PAYTEL, String PAYVLD) {
    this.PAYTEL = PAYTEL;
    this.PAYVLD = PAYVLD;
  }

  @Override
  public String toString() {
    return "Bean{" + "PAYTEL='" + PAYTEL + '\'' + ", PAYVLD='" + PAYVLD + '\'' + '}';
  }
}
