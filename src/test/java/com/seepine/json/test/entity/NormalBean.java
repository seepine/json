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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getMoney() {
    return money;
  }

  public void setMoney(Double money) {
    this.money = money;
  }

  public BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
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
