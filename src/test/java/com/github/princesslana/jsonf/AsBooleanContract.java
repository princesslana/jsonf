package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface AsBooleanContract extends WithJsonFParser {

  @Property
  default void asBoolean_whenJsonBoolean_shouldFetch(@ForAll boolean b) {
    Assertions.assertThat(parse(Json.value(b).toString()).asBoolean()).isPresent().contains(b);
  }

  @Property
  default void asBoolean_whenJsonString_shouldFetchEmpty(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).asBoolean()).isEmpty();
  }

  @Property
  default void asBoolean_whenJsonLong_shouldFetchEmpty(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).asBoolean()).isEmpty();
  }

  @Property
  default void asBoolean_whenJsonDouble_shouldFetchEmpty(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).asBoolean()).isEmpty();
  }
}
