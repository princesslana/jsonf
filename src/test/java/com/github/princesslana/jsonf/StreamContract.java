package com.github.princesslana.jsonf;

import com.eclipsesource.json.Json;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import org.assertj.core.api.Assertions;

public interface StreamContract extends WithJsonFParser {

  @Property
  default void stream_whenJsonArray_shouldStreamElements(@ForAll String[] els) {
    Assertions.assertThat(parse(Json.array(els).toString()).flatMap(JsonF::asString))
        .containsSequence(els);
  }

  @Property
  default void stream_whenJsonPrimitive_shouldBeEmpty(@ForAll String s) {
    Assertions.assertThat(parse(Json.value(s).toString()).stream()).isEmpty();
  }

  @Property
  default void stream_whenJsonObject_shouldBeEmpty(@ForAll String key, @ForAll String value) {
    Assertions.assertThat(parse(Json.object().add(key, value).toString()).stream()).isEmpty();
  }
}
