package com.github.princesslana.jsonf;

/** Exception thrown for JsonF errors. */
public class JsonFException extends RuntimeException {

  /** Constructs a new instance with no error message or cause. */
  public JsonFException() {
    super();
  }

  /**
   * Constructs a new instance with the given message.
   *
   * @param msg the message
   */
  public JsonFException(String msg) {
    super(msg);
  }

  /**
   * Constructs a new instance with the given cause.
   *
   * @param cause the cause
   */
  public JsonFException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new instance with the given message and cause.
   *
   * @param msg the message
   * @param cause the cause
   */
  public JsonFException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
