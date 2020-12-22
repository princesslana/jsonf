package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface AsStringContract extends WithJsonFParser {

  @Property
  default void asString_whenJsonString_shouldFetch(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).asString()).isPresent().contains(s);
  }

  @Property
  default void asString_whenJsonLong_shouldFetchEmpty(@ForAll long l) {
    Assertions.assertThat(parse(Json.value(l).toString()).asString()).isEmpty();
  }

  @Property
  default void asString_whenJsonDouble_shouldFetchEmpty(@ForAll double d) {
    Assertions.assertThat(parse(Json.value(d).toString()).asString()).isEmpty();
  }

  @Property
  default void asString_whenJsonBoolean_shouldFetchEmpty(@ForAll boolean b) {
    Assertions.assertThat(parse(Json.value(b).toString()).asString()).isEmpty();
  }
}
