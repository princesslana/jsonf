package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface GetContract extends WithJsonFParser {

  @Property
  default void get_whenJsonObject_shouldFetch(@ForAll String key, @ForAll String value) {
    var json = Json.object().add(key, value).toString();
    Assertions.assertThat(parse(json).get(key).asString()).isPresent().contains(value);
  }

  @Property
  default void get_whenNestedField_shouldFetch(@ForAll String key, @ForAll String value) {
    var json = Json.object().add(key, Json.object().add(key, value)).toString();
    Assertions.assertThat(parse(json).get(key).get(key).asString()).isPresent().contains(value);
  }

  @Property
  default void get_whenNoSuchField_shouldFetchEmpty(@ForAll String key) {
    var json = Json.object().toString();
    Assertions.assertThat(parse(json).get(key).asString()).isEmpty();
  }

  @Property
  default void get_whenNoSuchNestedField_shouldFetchEmpty(
      @ForAll String key, @ForAll String value) {
    var json = Json.object().add(key, Json.object()).toString();
    Assertions.assertThat(parse(json).get(key).get(key).asString()).isEmpty();
  }

  @Property
  default void get_whenJsonArray_shouldFetch(@ForAll String value) {
    var json = Json.array(value).toString();
    Assertions.assertThat(parse(json).get(0).asString()).isPresent().contains(value);
  }

  @Property
  default void get_whenNoSuchIndex_shouldFetchEmpty(@ForAll int idx) {
    var json = Json.array().toString();
    Assertions.assertThat(parse(json).get(idx).asString()).isEmpty();
  }
}
