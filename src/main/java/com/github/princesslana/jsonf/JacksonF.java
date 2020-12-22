package com.github.princesslana.jsonf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Implementation of JsonF using Jackson. */
public class JacksonF implements JsonF {

  private final Optional<JsonNode> json;

  private JacksonF(Optional<JsonNode> json) {
    this.json = json;
  }

  @Override
  public Optional<Boolean> asBoolean() {
    return Optionals.mapIf(json, JsonNode::isBoolean, JsonNode::asBoolean);
  }

  @Override
  public Optional<BigDecimal> asNumber() {
    return Optionals.mapIf(json, JsonNode::isNumber, JsonNode::decimalValue);
  }

  @Override
  public Optional<String> asString() {
    return Optionals.mapIf(json, JsonNode::isTextual, JsonNode::asText);
  }

  @Override
  public JacksonF get(String key) {
    return new JacksonF(json.flatMap(j -> Optional.ofNullable(j.get(key))));
  }

  @Override
  public JacksonF get(int idx) {
    return new JacksonF(json.flatMap(j -> Optional.ofNullable(j.get(idx))));
  }

  @Override
  public Stream<JsonF> stream() {
    return json.stream()
        .filter(JsonNode::isArray)
        .flatMap(j -> StreamSupport.stream(j.spliterator(), false))
        .map(JacksonF::from);
  }

  /**
   * Parse the given string using Jackson.
   *
   * @param json the input string
   * @return created JacksonF instance
   * @throws JsonFException if parsing failed
   */
  public static JacksonF parse(String json) {
    try {
      return from(new ObjectMapper().readTree(json));
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * Create a JacksonF instance from a Jackson value.
   *
   * @param json the Jackson value
   * @return created JacksonF instance
   */
  public static JacksonF from(JsonNode json) {
    return new JacksonF(Optional.of(json));
  }
}
