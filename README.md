## json

基于jackson封装json常用方法

## 一、安装

#### LatestVersion

[![Maven Central](https://img.shields.io/maven-central/v/com.seepine/json.svg)](https://search.maven.org/search?q=g:com.seepine%20a:json)

#### Maven

```xml

<dependency>
  <groupId>com.seepine</groupId>
  <artifactId>json</artifactId>
  <version>${latestVersion}</version>
</dependency>
```

#### Gradle

```gradle
implementation("com.seepine:json:${lastVersion}")
```

## 二、用法

```java
import com.seepine.json.Json;

class Test {
  public static void main(String[] args) {
    User user = new User();
    // 对象转json字符串
    String jsonStr = Json.toJson(user);

    // 对象转json字符串，忽略null字段
    String jsonStr = Json.toJsonIgnoreNull(user);

    // json字符串转对象
    User user = Json.parse("jsonStr", User.class);

    // json字符串转JsonObject
    JsonObject jsonObject = Json.parseObj("jsonStr");
    // 获取某个属性值
    jsonObject.getStr("name");
    // 转对象
    jsonObject.toObject(User.class);


    // json字符串转ArrayNode
    ArrayNode arrayNode = Json.parseArray("jsonStr");
  }
}
```
