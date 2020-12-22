package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MinimalF implements JsonF {

  private final Optional<JsonValue> json;

  private MinimalF(Optional<JsonValue> json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return Optionals.mapIf(json, JsonValue::isBoolean, JsonValue::asBoolean);
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return Optionals.mapIf(json, JsonValue::isNumber, j -> new BigDecimal(j.toString()));
  }

  @Override
  public Optional<String> asString() {
    return Optionals.mapIf(json, JsonValue::isString, JsonValue::asString);
  }

  @Override
  public MinimalF get(String key) {
    return new MinimalF(
        Optionals.flatMapNullableIf(json, JsonValue::isObject, j -> j.asObject().get(key)));
  }

  @Override
  public MinimalF get(int idx) {
    var array = Optionals.mapIf(json, JsonValue::isArray, JsonValue::asArray);

    return new MinimalF(Optionals.mapIf(array, j -> 0 <= idx && idx < j.size(), j -> j.get(idx)));
  }

  @Override
  public Stream<JsonF> stream() {
    return json.stream()
        .filter(JsonValue::isArray)
        .flatMap(j -> StreamSupport.stream(j.asArray().spliterator(), false))
        .map(MinimalF::from);
  }

  public static MinimalF parse(String json) {
    return from(Json.parse(json));
  }

  public static MinimalF from(JsonValue json) {
    return new MinimalF(Optional.of(json));
  }
}
