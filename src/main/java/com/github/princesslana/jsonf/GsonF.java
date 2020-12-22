package com.github.princesslana.jsonf;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;

public class GsonF implements JsonF {
  private final Optional<JsonElement> json;

  private GsonF(Optional<JsonElement> json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return Optionals.mapIf(
        json, isPrimitiveAnd(JsonPrimitive::isBoolean), JsonElement::getAsBoolean);
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return Optionals.mapIf(
        json, isPrimitiveAnd(JsonPrimitive::isNumber), JsonElement::getAsBigDecimal);
  }

  @Override
  public Optional<String> asString() {
    return Optionals.mapIf(json, isPrimitiveAnd(JsonPrimitive::isString), JsonElement::getAsString);
  }

  @Override
  public GsonF get(String key) {
    return new GsonF(
        Optionals.flatMapNullableIf(
            json, JsonElement::isJsonObject, j -> j.getAsJsonObject().get(key)));
  }

  public static GsonF parse(String json) {
    return from(JsonParser.parseString(json));
  }

  public static GsonF from(JsonElement json) {
    return new GsonF(Optional.of(json));
  }

  private static Predicate<JsonElement> isPrimitiveAnd(Predicate<JsonPrimitive> p) {
    return el -> el.isJsonPrimitive() && p.test(el.getAsJsonPrimitive());
  }
}
