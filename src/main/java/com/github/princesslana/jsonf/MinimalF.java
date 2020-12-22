package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import java.math.BigDecimal;
import java.util.Optional;

public class MinimalF implements JsonF {

  private final JsonValue json;

  private MinimalF(JsonValue json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return json.isBoolean() ? Optional.of(json.asBoolean()) : Optional.empty();
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return json.isNumber() ? Optional.of(new BigDecimal(json.toString())) : Optional.empty();
  }

  @Override
  public Optional<String> asString() {
    return json.isString() ? Optional.of(json.asString()) : Optional.empty();
  }

  public static MinimalF parse(String json) {
    return from(Json.parse(json));
  }

  public static MinimalF from(JsonValue json) {
    return new MinimalF(json);
  }
}
