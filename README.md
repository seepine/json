## json

基于jackJson封装json常用方法，版本对标SpringBoot:2.6.7

## 依赖

```xml

<dependency>
  <groupId>com.seepine</groupId>
  <artifactId>json</artifactId>
  <version>0.0.1</version>
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
