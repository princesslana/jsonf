package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Implementation of JsonF using minimal-json lib. */
public class MinimalF implements JsonF {

  private final Optional<JsonValue> json;

  private MinimalF(Optional<JsonValue> json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return json.filter(JsonValue::isBoolean).map(JsonValue::asBoolean);
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return json.filter(JsonValue::isNumber).map(j -> new BigDecimal(j.toString()));
  }

  @Override
  public Optional<String> asString() {
    return json.filter(JsonValue::isString).map(JsonValue::asString);
  }

  @Override
  public MinimalF get(String key) {
    return new MinimalF(
        json.filter(JsonValue::isObject).flatMap(j -> Optional.ofNullable(j.asObject().get(key))));
  }

  @Override
  public MinimalF get(int idx) {
    var array = json.filter(JsonValue::isArray).map(JsonValue::asArray);

    return new MinimalF(array.filter(j -> 0 <= idx && idx < j.size()).map(j -> j.get(idx)));
  }

  @Override
  public Stream<JsonF> stream() {
    return json.stream()
        .filter(JsonValue::isArray)
        .flatMap(j -> StreamSupport.stream(j.asArray().spliterator(), false))
        .map(MinimalF::from);
  }

  /**
   * Parse the given string using minimal-json.
   *
   * @param json input string
   * @return created MinimalF instance
   * @throws JsonFException if parsing failed
   */
  public static MinimalF parse(String json) {
    if (json == null) {
      throw new JsonFException("Refusing to parse null input");
    }

    try {
      return from(Json.parse(json));
    } catch (ParseException e) {
      throw new JsonFException(e);
    }
  }

  /**
   * Create a MinimalF instance from a minimal-json value.
   *
   * @param json the minimal-json value
   * @return created MinimalF instance
   */
  public static MinimalF from(JsonValue json) {
    return new MinimalF(Optional.of(json));
  }
}
