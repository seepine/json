package com.seepine.json.mapper;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

/**
 * 序列化以get/set后的函数名为主，例如getXFG则是XFG
 *
 * @author seepine
 * @since 0.0.2
 */
public class GetterPropertyNamingStrategy extends PropertyNamingStrategy {
  private static final long serialVersionUID = 1L;

  @Override
  public String nameForSetterMethod(
      MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
    return method.getName().substring(3);
  }

  @Override
  public String nameForGetterMethod(
      MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
    return method.getName().substring(3);
  }
}
