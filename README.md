## json

基于jackJson封装json常用方法，版本对标SpringBoot:2.6.7

## 依赖

- Latest
  Version: [![Maven Central](https://img.shields.io/maven-central/v/com.seepine/json.svg)](https://search.maven.org/search?q=g:com.seepine%20a:json)
- Maven:

```xml

<dependency>
  <groupId>com.seepine</groupId>
  <artifactId>json</artifactId>
  <version>${latest.version}</version>
</dependency>
```

## 使用

```java
import com.seepine.json.Json;

class Test {
  public static void main(String[] args) {
    JsonObject jsonObject = Json.parseObj("jsonStr");
    ArrayNode arrayNode = Json.parseArray("jsonStr");
    Bean bean = Json.parse("jsonStr", Bean.class);
    String jsonStr = Json.toJson(bean);
  }
}
```
