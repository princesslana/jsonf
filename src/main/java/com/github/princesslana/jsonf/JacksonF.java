package com.github.princesslana.jsonf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;

public class JacksonF implements JsonF {

  private final JsonNode json;

  private JacksonF(JsonNode json) {
    this.json = json;
  }

  @Override
  public Optional<String> asString() {
    return json.isTextual() ? Optional.of(json.asText()) : Optional.empty();
  }

  public static JacksonF parse(String json) {
    try {
      return from(new ObjectMapper().readTree(json));
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public static JacksonF from(JsonNode json) {
    return new JacksonF(json);
  }
}
