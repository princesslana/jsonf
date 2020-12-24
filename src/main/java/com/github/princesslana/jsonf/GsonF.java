package com.github.princesslana.jsonf;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Implementation of JsonF using Gson. */
public class GsonF implements JsonF {

  private static final TypeAdapter<JsonElement> STRICT_GSON_ADAPTER =
      new Gson().getAdapter(JsonElement.class);

  private final Optional<JsonElement> json;

  private GsonF(Optional<JsonElement> json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return json.filter(isPrimitiveAnd(JsonPrimitive::isBoolean)).map(JsonElement::getAsBoolean);
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return json.filter(isPrimitiveAnd(JsonPrimitive::isNumber)).map(JsonElement::getAsBigDecimal);
  }

  @Override
  public Optional<String> asString() {
    return json.filter(isPrimitiveAnd(JsonPrimitive::isString)).map(JsonElement::getAsString);
  }

  @Override
  public GsonF get(String key) {
    return new GsonF(
        json.filter(JsonElement::isJsonObject)
            .flatMap(j -> Optional.ofNullable(j.getAsJsonObject().get(key))));
  }

  @Override
  public GsonF get(int idx) {
    var array = json.filter(JsonElement::isJsonArray).map(JsonElement::getAsJsonArray);

    return new GsonF((array.filter(j -> 0 <= idx && idx < j.size()).map(j -> j.get(idx))));
  }

  @Override
  public Stream<JsonF> stream() {
    return json.stream()
        .filter(JsonElement::isJsonArray)
        .flatMap(j -> StreamSupport.stream(j.getAsJsonArray().spliterator(), false))
        .map(GsonF::from);
  }

  /**
   * Parse the given String using Gson. See https://stackoverflow.com/a/47890960 for an explanation
   * of why this can not be simpler.
   *
   * @param json input string
   * @return created GsonF instance
   * @throws JsonFException if parsing failed
   */
  public static GsonF parse(String json) {
    if (json == null) {
      throw new JsonFException("Refusing to parse blank input");
    }

    try {
      try (var reader = new JsonReader(new StringReader(json))) {
        var result = STRICT_GSON_ADAPTER.read(reader);
        reader.hasNext();
        return GsonF.from(result);
      }
    } catch (IOException e) {
      throw new JsonFException(e);
    }
  }

  /**
   * Create GsonF instance from a Gson value.
   *
   * @param json the Gson value
   * @return created Gson instance
   */
  public static GsonF from(JsonElement json) {
    return new GsonF(Optional.of(json));
  }

  private static Predicate<JsonElement> isPrimitiveAnd(Predicate<JsonPrimitive> p) {
    return el -> el.isJsonPrimitive() && p.test(el.getAsJsonPrimitive());
  }
}
