package com.seepine.json.mapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * 序列化以get/set后的函数名为主，例如getXFG则是XFG
 *
 * <p>当firstToLowerCase设为true时，getXFG则是xFG
 *
 * @author seepine
 * @since 0.0.2
 */
public class GetterPropertyNamingStrategy extends PropertyNamingStrategy {
  private static final long serialVersionUID = 1L;
  private final boolean firstToLowerCase;

  public GetterPropertyNamingStrategy() {
    this(false);
  }
  /**
   * 首字母是否小写
   *
   * @param firstToLowerCase 首字母是否小写
   */
  public GetterPropertyNamingStrategy(boolean firstToLowerCase) {
    this.firstToLowerCase = firstToLowerCase;
  }

  public String firstLetterToLowerCase(String str) {
    if (!firstToLowerCase) {
      return str;
    }
    String retStr = str.substring(0, 1);
    return retStr.toLowerCase() + str.substring(1);
  }

  @Override
  public String nameForSetterMethod(
      MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
    try {
      return firstLetterToLowerCase(method.getName().substring(3));
    } catch (Exception e) {
      return defaultName;
    }
  }

  @Override
  public String nameForGetterMethod(
      MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
    try {
      return firstLetterToLowerCase(method.getName().substring(3));
    } catch (Exception e) {
      return defaultName;
    }
  }
}
