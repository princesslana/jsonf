package com.github.princesslana.jsonf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Supports calling an appropriate Implementation using reflection.
 *
 * <p>Maintains a registry of classes that implement JsonF and upon initialization detects those
 * that are available based upon the contents of the classpath.
 *
 * <p>It is vitally important that this class does not force loading of any classes from json libs
 * until they are known to be present on the classpath.
 */
public class Implementation {

  private Implementation() {}

  private static final Logger LOG = LogManager.getLogger(Implementation.class);

  private static final Def JACKSON =
      new Def("com.github.princesslana.jsonf.JacksonF", "com.fasterxml.jackson.databind.JsonNode");

  private static final Def MINIMAL =
      new Def("com.github.princesslana.jsonf.MinimalF", "com.eclipsesource.json.JsonValue");

  private static final Def GSON =
      new Def("com.github.princesslana.jsonf.GsonF", "com.google.gson.JsonElement");

  private static final List<Installed> INSTALLED = new ArrayList<>();

  static {
    for (var def : List.of(JACKSON, MINIMAL, GSON)) {
      try {
        var jsonClass = Class.forName(def.jsonClass);
        var implClass = Class.forName(def.implClass);

        var parse = implClass.getMethod("parse", String.class);
        var from = implClass.getMethod("from", jsonClass);

        INSTALLED.add(new Installed(jsonClass, parse, from));

        LOG.atInfo().log("Installed jsonf implementation: {}", def.implClass);
      } catch (ClassNotFoundException | NoSuchMethodException e) {
        LOG.atDebug().log("Skipped jsonf implementation: {}", e.getMessage());
      }
    }

    if (INSTALLED.isEmpty()) {
      LOG.atWarn().log("Unable to find any json libs to use with jsonf");
    }
  }

  /**
   * Parse the given string using an available Json lib.
   *
   * @param json input string
   * @return created JsonF instance
   * @throws JsonFException if parsing failed
   */
  public static JsonF parse(String json) {
    if (INSTALLED.isEmpty()) {
      throw new JsonFException("Uanble to find any json libs to use with jsonf");
    }

    return INSTALLED.get(0).parse(json);
  }

  /**
   * Create a JsonF instance from the given Json value. The implementation chosen will be based upon
   * the type of the provided obj.
   *
   * @param json the json value
   * @return created JsonF instance
   */
  public static JsonF from(Object json) {
    return INSTALLED.stream()
        .filter(i -> i.supportsJsonInstance(json))
        .findFirst()
        .map(i -> i.from(json))
        .orElseThrow(
            () ->
                new JsonFException("Unable to find implementation supporting: " + json.getClass()));
  }

  /**
   * An implementation defintion. Class names kept as Strings as we want to avoid initializing any
   * classes that may not exist.
   */
  private static class Def {
    public final String implClass;
    public final String jsonClass;

    public Def(String implClass, String jsonClass) {
      this.implClass = implClass;
      this.jsonClass = jsonClass;
    }
  }

  /**
   * An implementation that we know is available. In this class we assume that we have performed
   * class detection already. Hence, we can go ahead and start initializing classes and performing
   * reflection upon them.
   */
  private static class Installed {
    private final Class<?> jsonClass;
    private final Method parse;
    private final Method from;

    public Installed(Class<?> jsonClass, Method parse, Method from) {
      this.jsonClass = jsonClass;
      this.parse = parse;
      this.from = from;
    }

    public boolean supportsJsonInstance(Object obj) {
      return jsonClass.isInstance(obj);
    }

    public JsonF parse(String s) {
      return invoke(parse, s);
    }

    public JsonF from(Object obj) {
      return invoke(from, obj);
    }

    private JsonF invoke(Method m, Object... args) {
      try {
        return (JsonF) m.invoke(null, args);
      } catch (IllegalAccessException e) {
        throw new IllegalStateException(e);
      } catch (InvocationTargetException e) {
        throw rethrow(e);
      }
    }

    private static RuntimeException rethrow(InvocationTargetException e) {
      var cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else {
        throw new JsonFException(cause);
      }
    }
  }
}
