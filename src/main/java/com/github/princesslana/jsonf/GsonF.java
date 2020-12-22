package com.github.princesslana.jsonf;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.Optional;

public class GsonF implements JsonF {
  private final JsonElement json;

  private GsonF(JsonElement json) {
    this.json = json;
  }

  @Override
  public Optional<String> asString() {
    return json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()
        ? Optional.of(json.getAsString())
        : Optional.empty();
  }

  public static GsonF parse(String json) {
    return from(JsonParser.parseString(json));
  }

  public static GsonF from(JsonElement json) {
    return new GsonF(json);
  }
}
