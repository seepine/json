package com.seepine.json.test.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** must be set public or add getXXX method */
public class NormalBean {
  public Long id;
  public String name;
  public Double money;
  public BigDecimal weight;
  public Integer age;
  public LocalDate birthday;
  public LocalDateTime createTime;

  public static NormalBean build() {
    NormalBean bean = new NormalBean();
    bean.id = 1999999L;
    bean.name = "Cat";
    bean.money = 122.88;
    bean.weight = new BigDecimal("57.8");
    bean.age = 26;
    bean.birthday = LocalDate.now();
    bean.createTime = LocalDateTime.now();
    return bean;
  }

  @Override
  public String toString() {
    return "NormalBean{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", money="
        + money
        + ", weight="
        + weight
        + ", age="
        + age
        + ", birthday="
        + birthday
        + ", createTime="
        + createTime
        + '}';
  }
}
