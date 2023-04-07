package com.seepine.json.exception;

/**
 * 转换异常类
 *
 * @author seepine
 * @since 0.2.0
 */
public class JsonException extends RuntimeException {
  public JsonException(String msg) {
    super(msg);
  }

  public JsonException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonException(Throwable cause) {
    super(cause.getMessage(), cause);
  }
}
